package xyz.liamsho.mdauth.httpapi.models;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.UUID;

public class AddPlayerRequestModel {
    @JSONField(name = "kaiheila_uid")
    private String kaiheilaUid;
    @JSONField(name = "uuid")
    private UUID uuid;
    @JSONField(name = "player_name")
    private String playerName;

    public String getKaiheilaUid() { return kaiheilaUid; }
    public void setKaiheilaUid(String value) { this.kaiheilaUid = value; }

    public UUID getUUID() { return uuid; }
    public void setUUID(UUID value) { this.uuid = value; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String value) { this.playerName = value; }
}
