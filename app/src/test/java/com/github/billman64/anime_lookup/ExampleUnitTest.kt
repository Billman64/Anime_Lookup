package com.github.billman64.anime_lookup

import com.github.billman64.anime_lookup.Model.AnimeAPI
import com.github.billman64.anime_lookup.Model.AnimeAdapter
import com.github.billman64.anime_lookup.Model.AnimeShowData
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    val TAG = this.javaClass.simpleName + "--demoTest"

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun api_url_isCorrect(){
        val animeAPI = Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v3/search/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnimeAPI::class.java)

        val url = animeAPI.getData("naruto").request().url()
        assertEquals(url.toString(), "https://api.jikan.moe/v3/search/anime?q=naruto")
    }

    @Test
    fun AnimeAPI_method_is_get(){
        val animeAPI = Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v3/search/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnimeAPI::class.java)

        assertEquals(animeAPI.getData("x").request().method(),"GET")
    }

    @Test
    fun AnimeAdapter_can_hold_data(){
        val list : ArrayList<AnimeShowData> = arrayListOf(AnimeShowData(123,
                "https://myanimelist.net",
                "https://cdn.myanimelist.net/images/anime/12/82828.jpg?s=98fde86014a4379263b182edaf19ee32",
                "Dragon Ball: Ossu! Kaettekita Son Gokuu to Nakama-tachi!!",
                false,
                "Stuff happens",
                "special",
                1,
                1.23,
                "2008-09-21T00:00:00+00:00",
                "2008-09-21T00:00:00+00:00",
                12345,
                "PG-13"
        )
        )


        val animeAdapter = AnimeAdapter(list)
        assert(animeAdapter.itemCount==1)
    }
}