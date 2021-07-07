package xyz.liamsho.mdauth.httpapi.models;

import com.alibaba.fastjson.annotation.JSONField;

public class Player {
    @JSONField(name = "username")
    private String Username;
    @JSONField(name = "from")
    private String From;
    @JSONField(name = "identify_number")
    private String IdentifyNumber;
    @JSONField(name = "minecraft_uuid")
    private String MinecraftUuid;
    @JSONField(name = "minecraft_player_name")
    private String MinecraftPlayerName;
    @JSONField(name = "element")
    private int Element;
    @JSONField(name = "is_guard")
    private boolean IsGuard;

    public String getUsername() {
        return Username;
    }
    public void setUsername(String username) {
        Username = username;
    }

    public String getFrom() {
        return From;
    }
    public void setFrom(String from) {
        From = from;
    }

    public String getIdentifyNumber() {
        return IdentifyNumber;
    }
    public void setIdentifyNumber(String identifyNumber) {
        IdentifyNumber = identifyNumber;
    }

    public String getMinecraftUuid() {
        return MinecraftUuid;
    }
    public void setMinecraftUuid(String minecraftUuid) {
        MinecraftUuid = minecraftUuid;
    }

    public String getMinecraftPlayerName() {
        return MinecraftPlayerName;
    }
    public void setMinecraftPlayerName(String minecraftPlayerName) {
        MinecraftPlayerName = minecraftPlayerName;
    }

    public int getElement() {
        return Element;
    }
    public void setElement(int element) {
        Element = element;
    }

    public boolean getGuard() {
        return IsGuard;
    }
    public void setGuard(boolean guard) {
        IsGuard = guard;
    }
}
