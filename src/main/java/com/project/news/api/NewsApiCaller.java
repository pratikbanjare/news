package com.project.news.api;


import com.project.news.client.NewsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Service
public class NewsApiCaller {

    @Value("${api.key}")
    private String apiKey;

    @Value(("${url.headline}"))
    private String headlineUrl;

    @Value("${url.everything}")
    private String everythingUrl;

    @Autowired
    private NewsClient newsClient;

    public NewsApiCaller() {

    }

    private String encode(String value) {
        if (value == null) return "";
        // URLEncoder encodes spaces as '+', convert to %20 which is safer in URLs
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }

    public String getTopNewsHeadlineFrom(String sources) {

            StringBuilder sb = new StringBuilder();
            String encodedSources = encode(sources);
            String completeUrl = sb.append(headlineUrl)
                    .append("?")
                    .append("sources=")
                    .append(encodedSources)
                    .append("&")
                    .append("apiKey=")
                    .append(apiKey).toString();

            HttpRequest request = newsClient.createHtpGetRequest(completeUrl);
            System.out.println("Request URI --> " + request.uri());

            return newsClient.httpRequestCall(request);
    }

    public String getEverythingWithCaller(String q) {

        StringBuilder sb = new StringBuilder();
        String encodedQ = encode(q);
        String completeUrl = sb.append(everythingUrl)
                .append("?")
                .append("q=")
                .append(encodedQ)
                .append("&")
                .append("apiKey=")
                .append(apiKey)
                .toString();

        HttpRequest request = newsClient.createHtpGetRequest(completeUrl);
        System.out.println("Request URI --> " + request.uri());

        return newsClient.httpRequestCall(request);
    }

    public String getEverythingWithinCaller (String q, String from, String to){
        StringBuilder sb = new StringBuilder();
        String encodedQ = encode(q);
        String encodedFrom = encode(from);
        String encodedTo = encode(to);
        String completeUrl = sb.append(everythingUrl)
                .append("?")
                .append("q=")
                .append(encodedQ)
                .append("&")
                .append("from=")
                .append(encodedFrom)
                .append("&")
                .append("to=")
                .append(encodedTo)
                .append("&")
                .append("apiKey=")
                .append(apiKey)
                .toString();

        HttpRequest request = newsClient.createHtpGetRequest(completeUrl);

        System.out.println("Request URI --> " + request.uri());
        return newsClient.httpRequestCall(request);
    }

    // New: fetch raw JSON from the everything endpoint and return bytes for download
    public byte[] getEverythingBulk(String q, LocalDate from, LocalDate to) {
        StringBuilder sb = new StringBuilder();
        boolean firstParam = true;

        sb.append(everythingUrl);

        if (q != null && !q.isEmpty()) {
            sb.append(firstParam ? "?" : "&");
            sb.append("q=").append(encode(q));
            firstParam = false;
        }

        if (from != null) {
            sb.append(firstParam ? "?" : "&");
            sb.append("from=").append(encode(from.toString()));
            firstParam = false;
        }

        if (to != null) {
            sb.append(firstParam ? "?" : "&");
            sb.append("to=").append(encode(to.toString()));
            firstParam = false;
        }

        // always append apiKey
        sb.append(firstParam ? "?" : "&");
        sb.append("apiKey=").append(apiKey);

        String completeUrl = sb.toString();

        HttpRequest request = newsClient.createHtpGetRequest(completeUrl);
        System.out.println("Request URI --> " + request.uri());

        // Use byte-array handler to avoid String truncation or encoding issues
        return newsClient.httpRequestCallBytes(request);
    }


}
