package xyz.liamsho.mdauth.httpapi;

import xyz.liamsho.mdauth.MDAuth;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CheckPlayerName {
    public static boolean ExistInOnlinePlayerId(String playerName) {
        var url = "https://playerdb.co/api/player/minecraft/" + playerName;

        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            var response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                MDAuth.MDAuthLogger.info("Offline Player Name Check: Player name " + playerName + " has been used by online player.");
                throw new Exception("Player name has been used by online player.");
            }
        } catch (Exception e) {
            MDAuth.MDAuthLogger.info(e.getMessage());
            return true;
        }
        return false;
    }
}
