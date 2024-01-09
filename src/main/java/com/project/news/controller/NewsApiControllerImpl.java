package com.project.news.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@RestController
public class NewsApiControllerImpl implements NewsApiController {

    @Value("${api.key}")
    private String apiKey;

    @Value(("${url.headline}"))
    private String healineUrl;


    @GetMapping(path = "/api/get")
    public String firsController() {
        return "got headline of ";
    }

    @GetMapping(path = "api/getCustom")
    public String getCustomHeadline(@RequestParam String sources) {
        return "request param " + sources;
    }

    @Override
    @GetMapping(path = "api/headlinesOf", params = "sources")
    public String getNewsHeadlineOf(String sources) {

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

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

}
