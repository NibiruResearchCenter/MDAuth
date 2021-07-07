package xyz.liamsho.mdauth.httpapi.models;

import com.alibaba.fastjson.annotation.JSONField;

public class ResponseModel {
    @JSONField(name = "code")
    private int Code;
    @JSONField(name = "message")
    private String Message;
    @JSONField(name = "data")
    private Player Data;

    public int getCode() {
        return Code;
    }
    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }
    public void setMessage(String message) {
        Message = message;
    }

    public Player getData() {
        return Data;
    }
    public void setData(Player data) {
        Data = data;
    }
}
