package com.aqwas.androidtest.api;

import com.google.gson.Gson;

public class GsonProvider {
    private static Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }
}
