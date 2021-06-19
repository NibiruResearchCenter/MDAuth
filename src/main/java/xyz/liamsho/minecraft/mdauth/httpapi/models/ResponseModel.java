package xyz.liamsho.minecraft.mdauth.httpapi.models;

import com.alibaba.fastjson.annotation.JSONField;

public class ResponseModel {
    @JSONField(name = "code")
    private int code;
    @JSONField(name = "message")
    private String message;
    @JSONField(name = "data")
    private PlayerModel data;

    public int getCode() { return code; }
    public void setCode(int value) { this.code = value; }

    public String getMessage() { return message; }
    public void setMessage(String value) { this.message = value; }

    public PlayerModel getData() { return data; }
    public void setData(PlayerModel value) { this.data = value; }
}
