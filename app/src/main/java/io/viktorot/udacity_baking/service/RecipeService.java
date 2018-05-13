package io.viktorot.udacity_baking.service;

import java.util.List;

import io.reactivex.Single;
import io.viktorot.udacity_baking.data.Recipe;
import retrofit2.http.GET;

public interface RecipeService {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Single<List<Recipe>> getRecipes();
}
