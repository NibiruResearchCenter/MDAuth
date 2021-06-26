package xyz.liamsho.mdauth;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RestrictedPlayers {
    private final static List<UUID> players = new ArrayList<>();

    public static void Add(UUID uuid){
        if(!Check(uuid)){
            players.add(uuid);
        }
    }

    public static void Remove(UUID uuid) {
        if (Check(uuid)) {
            players.remove(uuid);
        }
    }

    public static boolean Check(UUID uuid) {
        return players.contains(uuid);
    }
}
