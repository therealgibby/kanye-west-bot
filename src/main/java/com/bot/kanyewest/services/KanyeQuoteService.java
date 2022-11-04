package com.bot.kanyewest.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class KanyeQuoteService {

    private final String KANYE_URL = "https://api.kanye.rest/";
    private final HttpClient client = HttpClient.newHttpClient();
    private final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(KANYE_URL)).build();

    public String createResponse() {
        HttpResponse<String> httpResponse = null;

        try {
            httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return httpResponse.body();
    }

}
