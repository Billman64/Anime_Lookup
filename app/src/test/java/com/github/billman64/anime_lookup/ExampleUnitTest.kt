package com.github.billman64.anime_lookup

import com.github.billman64.anime_lookup.Model.AcroAPI
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
        val acroAPI = Retrofit.Builder()
            .baseUrl(" http://www.nactem.ac.uk/software/acromine/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AcroAPI::class.java)

        val url = acroAPI.getData("aa").request().url()
        assertEquals(url.toString(), "http://www.nactem.ac.uk/software/acromine/dictionary.py?sf=aa")
    }

    @Test
    fun AcroAPI_method_is_get(){
        val acroAPI = Retrofit.Builder()
            .baseUrl(" http://www.nactem.ac.uk/software/acromine/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AcroAPI::class.java)

        assertEquals(acroAPI.getData("x").request().method(),"GET")
    }
}