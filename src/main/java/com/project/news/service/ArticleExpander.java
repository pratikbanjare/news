package com.project.news.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.news.client.NewsClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleExpander {

    private final NewsClient newsClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public ArticleExpander(NewsClient newsClient) {
        this.newsClient = newsClient;
    }

    /**
     * If expandFull is true, attempts to fetch article.url and replace 'content' and 'description' with extracted text.
     * If expansion fails for an article, original snippet is preserved.
     * Returns the modified JSON as bytes (UTF-8).
     */
    public byte[] expandArticles(byte[] originalJsonBytes, boolean expandFull) {
        if (!expandFull || originalJsonBytes == null || originalJsonBytes.length == 0) {
            return originalJsonBytes;
        }

        try {
            JsonNode root = mapper.readTree(originalJsonBytes);
            if (!root.has("articles") || !root.get("articles").isArray()) {
                return originalJsonBytes;
            }

            ArrayNode articles = (ArrayNode) root.get("articles");

            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < articles.size(); i++) indices.add(i);

            for (int idx : indices) {
                JsonNode art = articles.get(idx);
                if (art == null || !art.isObject()) continue;
                ObjectNode artObj = (ObjectNode) art;
                String url = artObj.hasNonNull("url") ? artObj.get("url").asText(null) : null;
                if (url == null || url.isEmpty()) continue;

                try {
                    // Use NewsClient to fetch bytes for the article URL
                    HttpRequest req = HttpRequest.newBuilder()
                            .uri(URI.create(url.replace(" ", "%20")))
                            .timeout(Duration.ofSeconds(10))
                            .GET()
                            .build();

                    byte[] htmlBytes = newsClient.httpRequestCallBytes(req);
                    if (htmlBytes == null || htmlBytes.length == 0) continue;

                    String html = new String(htmlBytes, StandardCharsets.UTF_8);
                    String extracted = extractMainText(html);
                    if (extracted != null && !extracted.isEmpty()) {
                        // put full content; description keep shorter
                        artObj.put("content", extracted);
                        String shortDesc = extracted.length() > 400 ? extracted.substring(0, 400) : extracted;
                        artObj.put("description", shortDesc);
                    }
                } catch (Exception e) {
                    // on any error, skip expansion for this article
                    // keep original snippet
                }
            }

            return mapper.writeValueAsBytes(root);
        } catch (IOException e) {
            // parsing failure: return original
            return originalJsonBytes;
        }
    }

    // Simple extraction heuristics using Jsoup
    private String extractMainText(String html) {
        if (html == null || html.isEmpty()) return null;
        Document doc = Jsoup.parse(html);

        // 1) prefer <article>
        Element article = doc.selectFirst("article");
        if (article != null) {
            String t = article.text();
            if (t != null && !t.isEmpty()) return t;
        }

        // 2) common container patterns
        String[] selectors = new String[]{
                "div[class*=article]",
                "div[class*=content]",
                "div[id*=article]",
                "div[id*=content]",
                "main"
        };
        for (String sel : selectors) {
            Element el = doc.selectFirst(sel);
            if (el != null) {
                String t = el.text();
                if (t != null && !t.isEmpty()) return t;
            }
        }

        // 3) fallback: join <p> tags inside the largest content-like container
        Elements paragraphs = doc.select("p");
        if (paragraphs != null && !paragraphs.isEmpty()) {
            // try to pick paragraphs that are within a common parent
            List<String> texts = paragraphs.stream()
                    .map(Element::text)
                    .filter(s -> s != null && !s.isBlank())
                    .collect(Collectors.toList());
            if (!texts.isEmpty()) {
                return String.join("\n\n", texts);
            }
        }

        return null;
    }
}

