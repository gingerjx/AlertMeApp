package com.example.alertmeapp.api.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAdapter {
    private static Retrofit retrofit = null;
    private static AlertMeService alertMeService;
    //https://alertme-app.herokuapp.com/
    //http://10.0.2.2:8080/
    public static AlertMeService getAlertMeService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://alertme-app.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            alertMeService = retrofit.create(AlertMeService.class);
        }
        return alertMeService;
    }
}
