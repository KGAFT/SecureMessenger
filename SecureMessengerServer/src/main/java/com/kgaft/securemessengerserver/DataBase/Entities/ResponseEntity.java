package com.kgaft.securemessengerserver.DataBase.Entities;

import com.google.gson.Gson;

/**
 * @author KGAFT
 * Entity to wrap, using string messages to json, as server request
 */
public class ResponseEntity implements IJsonObject{
    private String response;

    public ResponseEntity(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
