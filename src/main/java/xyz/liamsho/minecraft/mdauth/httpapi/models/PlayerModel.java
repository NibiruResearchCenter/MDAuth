package xyz.liamsho.minecraft.mdauth.httpapi.models;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.UUID;

public class PlayerModel {
    @JSONField(name = "kaiheila_username")
    private String kaiheilaUsername;
    @JSONField(name = "kaiheila_user_identify_number")
    private String kaiheilaUserIdentifyNumber;
    @JSONField(name = "bilibili_guard_level")
    private long bilibiliGuardLevel;
    @JSONField(name = "minecraft_uuid")
    private UUID minecraftUUID;
    @JSONField(name = "minecraft_player_name")
    private String minecraftPlayerName;
    @JSONField(name = "element")
    private long element;

    public String getKaiheilaUsername() { return kaiheilaUsername; }
    public void setKaiheilaUsername(String value) { this.kaiheilaUsername = value; }

    public String getKaiheilaUserIdentifyNumber() { return kaiheilaUserIdentifyNumber; }
    public void setKaiheilaUserIdentifyNumber(String value) { this.kaiheilaUserIdentifyNumber = value; }

    public long getBilibiliGuardLevel() { return bilibiliGuardLevel; }
    public void setBilibiliGuardLevel(long value) { this.bilibiliGuardLevel = value; }

    public UUID getMinecraftUUID() { return minecraftUUID; }
    public void setMinecraftUUID(UUID value) { this.minecraftUUID = value; }

    public String getMinecraftPlayerName() { return minecraftPlayerName; }
    public void setMinecraftPlayerName(String value) { this.minecraftPlayerName = value; }

    public long getElement() { return element; }
    public void setElement(long value) { this.element = value; }
}
