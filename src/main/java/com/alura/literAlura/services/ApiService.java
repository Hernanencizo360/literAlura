package com.alura.literAlura.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.HttpURLConnection;

@Service
public class ApiService {

    public String buscarLibro(String url) throws IOException, InterruptedException {
        String response = consumoApi(url);
        if (!jsonTieneResultados(response)) {
            throw new RuntimeException("La respuesta no contiene resultados.");
        }
        return response;
    }

    private String consumoApi(String url) throws IOException, InterruptedException {
        if (!urlValida(url)) {
            throw new RuntimeException("URL no válida: " + url);
        }
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if (statusCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("La solicitud no se realizó correctamente, código de estado: " + statusCode);
        }
        return response.body();
    }

    private boolean urlValida(String url) {
        try {
            URI.create(url).toURL();
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private boolean jsonTieneResultados(String json) {
        JSONObject jsonObj = new JSONObject(json);
        int count = jsonObj.getInt("count");
        JSONArray results = jsonObj.getJSONArray("results");
        return count > 0 && !results.isEmpty();
    }
}

