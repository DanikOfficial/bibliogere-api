package com.pete.bibliogere.api;

import java.util.LinkedHashMap;
import java.util.Map;

public class ApiResponseObject {

    Map<String, Object> data = new LinkedHashMap<String, Object>();

    public ApiResponseObject(Boolean error, Object message, Object object) {
        this.data.put("status", error);
        this.data.put("message", message);
        this.data.put("data", object);
    }

    public ApiResponseObject() {
    }

    public Map<String, Object> buildValidationError(String message, String status, Object error) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("message", message);
        data.put("status", status);
        data.put("errors", error);
        return data;
    }


    public Map<String, Object> buildSimpleError(String message, String status) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("message", message);
        data.put("status", status);
        return data;
    }

    public Map<String, Object> buildSimpleError(String message, String status, int code) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("message", message);
        data.put("status", status);
        data.put("code", code);
        return data;
    }

    public Map<String, Object> buildLoginResponse(Boolean error, String status, Object object) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("error", error);
        data.put("status", status);
        data.put("data", object);
        return data;
    }

    public Map<String, Object> getResponseObject() {
        return this.data;
    }

}
