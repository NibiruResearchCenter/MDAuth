package xyz.liamsho.mdauth.httpapi.models;

import com.alibaba.fastjson.annotation.JSONField;

public class AddCrackedPlayerRequestModel {
    @JSONField(name = "uuid")
    private String Uuid;
    @JSONField(name = "password")
    private String Password;

    public String getUuid() {
        return Uuid;
    }

    public String getPassword() {
        return Password;
    }

    public void setUuid(String uuid) {
        Uuid = uuid;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
