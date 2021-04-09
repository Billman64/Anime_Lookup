package com.github.billman64.anime_lookup.Model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimeAPI {

    @GET("anime")
    fun getData(@Query("q") q:String):retrofit2.Call<JsonObject>
}