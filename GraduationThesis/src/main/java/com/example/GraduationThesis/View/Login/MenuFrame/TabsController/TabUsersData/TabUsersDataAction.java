package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.TabUsersData;

import com.example.GraduationThesis.Service.LazySingleton.JsonWebToken.JsonWebTokenManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TabUsersDataAction {

    public static List<Map<String, Object>> Action() {
        HttpClient httpClient = HttpClient.newHttpClient();

        String API = "http://localhost:8080/api/v1/admin/queryUsersData";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API))
                .header("Authorization", "Bearer " + JsonWebTokenManager.getInstance().getJwtToken())
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            System.out.println("Response from API: " + responseBody);

            // Convert JSON into a list of maps
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, new TypeReference<List<Map<String, Object>>>() {
            });

        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
            return Collections.emptyList();
        }
    }
}
