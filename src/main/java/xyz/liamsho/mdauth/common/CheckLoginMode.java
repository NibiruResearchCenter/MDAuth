package xyz.liamsho.mdauth.common;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class CheckLoginMode {
    /**
     * Check if a given uuid and player name is in offline mode by calculate its uuid with player name.
     * @param uuid Player Unique Identifier
     * @param playerName Player name
     * @return true if player is in offline mode
     */
    public static boolean isOffline(UUID uuid, String playerName) {
        var offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(StandardCharsets.UTF_8));
        return offlineUUID.equals(uuid);
    }
}
