package com.github.billman64.acronymapp_albertsons_challenge

import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.billman64.acronymapp_albertsons_challenge.Model.AcroAPI
import com.github.billman64.acronymapp_albertsons_challenge.Model.AcroAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private val TAG = this.javaClass.simpleName + "--demo"

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.github.billman64.acronymapp_albertsons_challenge", appContext.packageName)
    }

    @Test
    fun AcroAdapter_can_hold_data(){
        val list : ArrayList<String> = arrayListOf("a", "b", "c")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val acroAdapter = AcroAdapter(appContext ,list)
        assert(acroAdapter.count==3)
    }
}