package xyz.liamsho.mdauth.httpapi.models;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.UUID;

public class AddPlayerRequestModel {
    @JSONField(name = "uid")
    public String Uid;
    @JSONField(name = "uuid")
    public String Uuid;
    @JSONField(name = "platform")
    public String Platform;
    @JSONField(name = "player_name")
    public String PlayerName;
    @JSONField(name = "is_legit_copy")
    public boolean IsLegitCopy;

    public String getUid() {
        return Uid;
    }
    public void setUid(String uid) {
        Uid = uid;
    }

    public String getUuid() {
        return Uuid;
    }
    public void setUuid(String uuid) {
        Uuid = uuid;
    }

    public String getPlatform() {
        return Platform;
    }
    public void setPlatform(String platform) {
        Platform = platform;
    }

    public String getPlayerName() {
        return PlayerName;
    }
    public void setPlayerName(String playerName) {
        PlayerName = playerName;
    }

    public boolean getLegitCopy() { return IsLegitCopy; }
    public void setLegitCopy(boolean legitCopy) {
        IsLegitCopy = legitCopy;
    }
}
