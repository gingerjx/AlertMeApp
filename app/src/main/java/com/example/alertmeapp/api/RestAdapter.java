package com.example.alertmeapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAdapter {
    private static Retrofit retrofit = null;
    private static AlertMeService alertMeService;

    public static AlertMeService getAPIClient() {
        if(retrofit == null){
             retrofit = new Retrofit.Builder()
                    .baseUrl("https://alertme-app.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            System.out.println("API");
            alertMeService = retrofit.create(AlertMeService.class);
        }
        return alertMeService;
    }
}
