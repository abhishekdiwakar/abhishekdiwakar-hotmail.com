package com.abkan.consulting.coronavirustrackingapp.webclient;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class WebClient {
    private HttpClient httpClient;

    public WebClient() {
        httpClient = HttpClient.newHttpClient();
    }

    public HttpResponse<String> sendRequest(String uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
