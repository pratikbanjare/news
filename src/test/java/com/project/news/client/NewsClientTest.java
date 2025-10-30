package com.project.news.client;

import org.junit.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class NewsClientTest {

    private NewsClient newsClient;

    @Test
    public void httpRequestCall() {

        newsClient = new NewsClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.google.com"))
                .GET()
                .build();

        

        String responseBody = newsClient.httpRequestCall(request);

        System.out.println(responseBody);
    }

}
