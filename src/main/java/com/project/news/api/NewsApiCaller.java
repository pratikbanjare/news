package com.project.news.api;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class NewsApiCaller {

    @Value("${api.key}")
    private String apiKey;

    @Value(("${url.headline}"))
    private String healineUrl;

    @Value("${url.everything}")
    private String everythingUrl;

    private HttpClient httpClient;

    public NewsApiCaller() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }


    public String getTopNewsHeadlineFrom(String sources) {

        try {
            StringBuilder sb = new StringBuilder();
            String completeUrl = sb.append(healineUrl)
                    .append("?")
                    .append("sources=")
                    .append(sources)
                    .append("&")
                    .append("apiKey=")
                    .append(apiKey).toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(completeUrl))
                    .GET()
                    .build();
            System.out.println("Request URI --> " + request.uri());
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.print("Status Code of the call -> " + response.statusCode());
            return response.body();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getEverythingWithCaller(String q) {

        StringBuilder sb = new StringBuilder();
        String completeUrl = sb.append(everythingUrl)
                .append("?")
                .append("q=")
                .append(q)
                .append("&")
                .append("apiKey=")
                .append(apiKey)
                .toString();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(completeUrl))
                .GET()
                .build();
        System.out.println("Request URI --> " + request.uri());
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.print("Status Code of the call -> " + response.statusCode());
        return response.body();
    }


}
