package com.project.news.client;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class NewsClient {

    private HttpClient httpClient;


    public NewsClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    public String httpRequestCall(HttpRequest request)  {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.print("Status Code of the call -> " + response.statusCode());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // New method: returns raw bytes for large/binary responses (avoids any string-size/truncation issues)
    public byte[] httpRequestCallBytes(HttpRequest request) {
        try {
            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            System.out.print("Status Code of the call -> " + response.statusCode());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpRequest createHtpGetRequest (String completeUrl){
        // Defensive: ensure spaces in the URL are encoded to %20 to avoid IllegalArgumentException from URI.create
        String safeUrl = (completeUrl == null) ? null : completeUrl.replace(" ", "%20");
        return HttpRequest.newBuilder()
                .uri(URI.create(safeUrl))
                .GET()
                .build();
    }
}
