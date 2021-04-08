package com.github.billman64.anime_lookup

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.billman64.anime_lookup.Model.AcroAdapter

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

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
        assertEquals("com.github.billman64.anime_lookup", appContext.packageName)
    }

    @Test
    fun AcroAdapter_can_hold_data(){
        val list : ArrayList<String> = arrayListOf("a", "b", "c")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val acroAdapter = AcroAdapter(appContext ,list)
        assert(acroAdapter.count==3)
    }
}