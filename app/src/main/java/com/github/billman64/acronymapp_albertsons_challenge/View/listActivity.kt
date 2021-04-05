package com.github.billman64.acronymapp_albertsons_challenge.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import com.github.billman64.acronymapp_albertsons_challenge.Model.AcroAPI
import com.github.billman64.acronymapp_albertsons_challenge.Model.AcroAdapter
import com.github.billman64.acronymapp_albertsons_challenge.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class listActivity : AppCompatActivity() {

    private val TAG:String = this.javaClass.simpleName + "--demo"
    private var acro = ""
    private var responseCode = ""
    var acroList = ArrayList<String>()

    val mockData:Boolean = true //temp for dev purposes only

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        savedInstanceState?.let{ Log.d(TAG, " onCreate() with a savedInstanceState") }?: getAcronymData()
    }

    private fun getAcronymData(){


        Log.v(TAG, "mockData: $mockData")
        if(mockData){   // temporarily enable mock data. Dev purposes only.

            acroList.add("American Automobile Association")
            acroList.add("Any Analogy for an Acronym")
            acroList.add("Awesome Aliens for Acronyms")

            Log.d(TAG, "mock data used. acroList size: ${acroList.size}")

            val acroAdapter = AcroAdapter(baseContext, acroList)
            Log.d(TAG, " acroAdapter count: ${acroAdapter.count}")
            listView.adapter = acroAdapter

            return
        }


        val bundle:Bundle? = intent.extras
        bundle?.let{
            acro = bundle.getString("acronym").toString()
            supportActionBar?.setTitle(acro)

            progressBar.visibility = View.VISIBLE


            // Retrofit builder
            val gson: Gson = GsonBuilder()
                .setLenient()
                .create()
            val acroAPI = Retrofit.Builder()
                    .baseUrl(" http://www.nactem.ac.uk/software/acromine/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(AcroAPI::class.java)
            // i.e: http://www.nactem.ac.uk/software/acromine/dictionary.py?sf=HMM



            // Coroutine for network call
            GlobalScope.launch(Dispatchers.IO){

                try{
                    Log.v(TAG, "coroutine ${acroAPI.toString()}")
                    Log.d(TAG, " url: ${acroAPI.getData(acro).request().url()}")
                    responseCode = acroAPI.getData(acro).awaitResponse().message().toString()  //TODO: fix error here
                    Log.d(TAG, " Response code: $responseCode")
                    val response = acroAPI.getData(acro).awaitResponse()
                    Log.d(TAG, " reponse code: ${response.code()} body: ${response.body().toString().substring(0..25)} errorBody: ${response.errorBody()}")
                    Log.d(TAG, " ${response.body().toString().substring(0..100)}...")


                    // Parse data ...


                } catch(e: Exception){
                    Log.e(TAG," Error. message: ${e.message} printStackTrace: ${e.printStackTrace()}")
                    Log.e(TAG, " response code: $responseCode")

                    withContext(Dispatchers.Main){
                        progressBar.visibility = View.GONE
                    }

                    val i = Intent(applicationContext, MainActivity::class.java)
                    i.putExtra("error", responseCode)
                    startActivity(i)
                    finish()

                }

            }

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if(acroList.count()>0) outState.putStringArrayList("acroList", acroList)
        Log.d(TAG, "onSaveInstanceState() acroList count: ${outState.getStringArrayList("acroList")?.count()}")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        acroList = savedInstanceState.getStringArrayList("acroList") as ArrayList<String>
        val restoreAdapter = AcroAdapter(this, acroList)
        listView.adapter = restoreAdapter

        Log.d(TAG, "onRestoreInstanceState() acroList count: ${acroList.count()}")

    }
}