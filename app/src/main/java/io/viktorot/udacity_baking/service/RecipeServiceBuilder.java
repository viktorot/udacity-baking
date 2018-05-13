package io.viktorot.udacity_baking.service;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeServiceBuilder {
    private RecipeServiceBuilder() {
    }

    public static RecipeService build() {
        String endpoint = "https://d17h27t6h515a5.cloudfront.net";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RecipeService.class);
    }
}
