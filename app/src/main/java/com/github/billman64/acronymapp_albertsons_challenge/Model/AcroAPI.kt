package com.github.billman64.acronymapp_albertsons_challenge.Model

import com.google.gson.JsonArray
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Query

interface AcroAPI {

    @GET("dictionary.py")
    fun getData(@Query("sf") acro:String):retrofit2.Call<JsonArray>
}