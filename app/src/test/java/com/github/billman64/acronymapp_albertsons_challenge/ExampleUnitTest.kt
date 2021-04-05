package com.github.billman64.acronymapp_albertsons_challenge

import com.github.billman64.acronymapp_albertsons_challenge.Model.AcroAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
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

        kotlinx.coroutines.GlobalScope.launch(Dispatchers.IO) {
            try{
                val url = acroAPI.getData("aa").request().url()
                assertEquals(url, "http://www.nactem.ac.uk/software/acromine/dictionary.py?sf=aa")
            } catch (e:Exception){
                fail("Network/API exception $e")
            }

        }

    }
}