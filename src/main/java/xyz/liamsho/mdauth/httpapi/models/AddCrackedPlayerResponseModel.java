package xyz.liamsho.mdauth.httpapi.models;

import com.alibaba.fastjson.annotation.JSONField;

public class AddCrackedPlayerResponseModel {
    @JSONField(name = "uuid")
    private String Uuid;
    @JSONField(name = "code")
    private int Code;

    public int getCode() {
        return Code;
    }

    public String getUuid() {
        return Uuid;
    }

    public void setCode(int code) {
        Code = code;
    }

    public void setUuid(String uuid) {
        Uuid = uuid;
    }
}
