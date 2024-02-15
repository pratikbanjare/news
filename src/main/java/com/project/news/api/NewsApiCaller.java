package com.project.news.api;


import com.project.news.client.NewsClient;
import org.springframework.beans.factory.annotation.Autowired;
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
    private String headlineUrl;

    @Value("${url.everything}")
    private String everythingUrl;

    @Autowired
    private NewsClient newsClient;

    public NewsApiCaller() {

    }

    public String getTopNewsHeadlineFrom(String sources) {

            StringBuilder sb = new StringBuilder();
            String completeUrl = sb.append(headlineUrl)
                    .append("?")
                    .append("sources=")
                    .append(sources)
                    .append("&")
                    .append("apiKey=")
                    .append(apiKey).toString();

            HttpRequest request = newsClient.createHtpGetRequest(completeUrl);
            System.out.println("Request URI --> " + request.uri());

            return newsClient.httpRequestCall(request);
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

        HttpRequest request = newsClient.createHtpGetRequest(completeUrl);
        System.out.println("Request URI --> " + request.uri());

        return newsClient.httpRequestCall(request);
    }

    public String getEverythingWithinCaller (String q, String from, String to){
        StringBuilder sb = new StringBuilder();
        String completeUrl = sb.append(everythingUrl)
                .append("?")
                .append("sources=")
                .append(q)
                .append("&")
                .append("from=")
                .append(from)
                .append("&")
                .append("to=")
                .append(to)
                .append("&")
                .append("apiKey=")
                .append(apiKey)
                .toString();

        HttpRequest request = newsClient.createHtpGetRequest(completeUrl);

        System.out.println("Request URI --> " + request.uri());
        return newsClient.httpRequestCall(request);
    }


}
