package xyz.liamsho.mdauth.httpapi.models;

import com.alibaba.fastjson.annotation.JSONField;

public class GetCrackedPlayerResponseModel {
    @JSONField(name = "code")
    private int Code;
    @JSONField(name = "verify_status")
    private boolean VerifyStatus;

    public int getCode() {
        return Code;
    }

    public boolean getVerifyStatus() {
        return VerifyStatus;
    }

    public void setCode(int code) {
        Code = code;
    }

    public void setVerifyStatus(boolean verifyStatus) {
        VerifyStatus = verifyStatus;
    }
}
