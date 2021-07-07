package xyz.liamsho.mdauth.httpapi;

import com.alibaba.fastjson.JSON;
import xyz.liamsho.mdauth.MDAuth;
import xyz.liamsho.mdauth.httpapi.models.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class ApiRequest {
    public static ResponseModel AddPlayerRequest(AddPlayerRequestModel addPlayerRequestModel){
        var url = Objects.requireNonNull(MDAuth.MDAuthConfiguration.getString("endPoints.add")) +
                "?code=" + Objects.requireNonNull(MDAuth.MDAuthConfiguration.getString("apiKeys.add"));
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JSON.toJSONString(addPlayerRequestModel)))
                .build();

        return getResponseModel(url, request);
    }

    public static ResponseModel GetPlayerRequest(UUID uuid, String playerName){
        var url = Objects.requireNonNull(MDAuth.MDAuthConfiguration.getString("endPoints.get")) +
                "?code=" + Objects.requireNonNull(MDAuth.MDAuthConfiguration.getString("apiKeys.get")) +
                "&uuid=" + uuid.toString();
        if(playerName != null){
            url = url + "&player_name=" + playerName;
        }

        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return getResponseModel(url, request);
    }

    public static GetCrackedPlayerResponseModel GetCrackedPlayer(UUID uuid, String password) {
        var url = Objects.requireNonNull(MDAuth.MDAuthConfiguration.getString("endPoints.crack")) +
                "?code=" + Objects.requireNonNull(MDAuth.MDAuthConfiguration.getString("apiKeys.crack")) +
                "&uuid=" + uuid.toString() +
                "&pass=" + password;
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }

        if(response.statusCode() == 401){
            MDAuth.MDAuthLogger.log(Level.WARNING, "Inner error: Received HTTP 401 Unauthorized with request: " + url);
            return null;
        }

        return JSON.parseObject(response.body(), GetCrackedPlayerResponseModel.class);
    }

    public static AddCrackedPlayerResponseModel AddCrackedPlayer(UUID uuid, String password) {
        var url = Objects.requireNonNull(MDAuth.MDAuthConfiguration.getString("endPoints.crack")) +
                "?code=" + Objects.requireNonNull(MDAuth.MDAuthConfiguration.getString("apiKeys.crack"));
        var bodyModel = new AddCrackedPlayerRequestModel();
        bodyModel.setUuid(uuid.toString());
        bodyModel.setPassword(password);
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JSON.toJSONString(bodyModel)))
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }

        if(response.statusCode() == 401){
            MDAuth.MDAuthLogger.log(Level.WARNING, "Inner error: Received HTTP 401 Unauthorized with request: " + url);
            return null;
        }

        return JSON.parseObject(response.body(), AddCrackedPlayerResponseModel.class);
    }

    private static ResponseModel getResponseModel(String url, HttpRequest request) {
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }

        if(response.statusCode() == 401){
            MDAuth.MDAuthLogger.log(Level.WARNING, "Inner error: Received HTTP 401 Unauthorized with request: " + url);
            return null;
        }

        return JSON.parseObject(response.body(), ResponseModel.class);
    }
}
