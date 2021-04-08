package com.github.billman64.anime_lookup.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.github.billman64.anime_lookup.Model.AcroAPI
import com.github.billman64.anime_lookup.Model.AcroAdapter
import com.github.billman64.anime_lookup.Model.AnimeShowData
import com.github.billman64.anime_lookup.R
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
    private var responseLength = 0
    private var acroList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        // Call to get acronym data, assuming no savedInstanceState
        savedInstanceState?.let{ Log.d(TAG, " onCreate() with a savedInstanceState") }?: getAcronymData()
    }

    private fun getAcronymData(){

        val bundle:Bundle? = intent.extras
        bundle?.let{
            acro = bundle.getString("acronym").toString()

            // Set up UI

            supportActionBar?.setTitle(acro)
            progressBar.visibility = View.VISIBLE

            // Retrofit builder

            val acroAPI = Retrofit.Builder()
                    .baseUrl("https://api.jikan.moe/v3/search/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(AcroAPI::class.java)
            // i.e: http://www.nactem.ac.uk/software/acromine/dictionary.py?sf=HMM

            // Coroutine for network call

            GlobalScope.launch(Dispatchers.IO){

                try{
                    Log.v(TAG, "coroutine $acroAPI acro: $acro")
                    Log.d(TAG, " url: ${acroAPI.getData(acro).request().url()}")

                    // GET data


                    responseCode = acroAPI.getData(acro).awaitResponse().code().toString()
                    responseLength = acroAPI.getData(acro).awaitResponse().code().toString().length


                    val response = acroAPI.getData(acro).awaitResponse()
                    Log.d(TAG, " reponse code: ${response.code()}")
                    Log.d(TAG, " response length: ${response.message().length}")

                    if(response.isSuccessful) Log.d(TAG, "body: ${response.body().toString().substring(0..50)}")
                            else Log.d(TAG, "errorBody: ${response.errorBody()}")


                    // Parse data ...

                    val resultsArray = response.body()?.getAsJsonArray("results")
                    Log.d(TAG, "resultsArray  count: ${resultsArray?.count()}  body: ${resultsArray.toString().substring(0..100)}")

                    var dataList = ArrayList<AnimeShowData>()
                    for(i in 0 until resultsArray!!.size()){

                        var animeShowData = AnimeShowData(
                            resultsArray[i].asInt,
                            resultsArray[i].asString,
                            resultsArray[i].asString,
                            resultsArray[i].asString,
                            resultsArray[i].asBoolean,
                            resultsArray[i].asString,
                            resultsArray[i].asString,
                            resultsArray[i].asInt,
                            resultsArray[i].asDouble,
                            resultsArray[i].asString,
                            resultsArray[i].asString,
                            resultsArray[i].asInt,
                            resultsArray[i].asString
                        )
                        dataList.add(animeShowData)
                    }

                    Log.d(TAG, " dataList  first record:  ${dataList[0].toString()}")

//
//                    // loop through individual results
//                    for(i in 0 until lfsJsonArray!!.size()){
//                        var lf = lfsJsonArray.get(i).asJsonObject.get("lf").toString()
//                        lf = lf.subSequence(1.. lf.length-2).toString()  // trims quotes
//                        Log.d(TAG, "lf: $lf")
//
//                        acroList.add(lf)
//                        Log.d(TAG, lf)
//                    }

                    // Update UI

                    withContext(Dispatchers.Main){
                        progressBar.visibility = View.GONE
//                        val acroAdapter = AcroAdapter(baseContext, acroList)
//                        listView.adapter = acroAdapter
                        Toast.makeText(this@listActivity, dataList[0].toString().substring(0..150), Toast.LENGTH_LONG)
                    }
                    
                } catch(e: Exception){
                    Log.e(TAG," Error. message: ${e.message} printStackTrace: ${e.printStackTrace()}")
                    Log.v(TAG, " response code: $responseCode length: $responseLength")

                    withContext(Dispatchers.Main){
                        progressBar.visibility = View.GONE
                    }

                    val i = Intent(applicationContext, MainActivity::class.java)

                    if(responseLength==3) responseCode = "${getString(R.string.not_found)}: $acro"     // API returns "[]" if acronym not in its database
                    if(responseCode.isBlank()) responseCode = getString(R.string.net_error)     // fallback generic error

                    i.putExtra("error", responseCode)
                    startActivity(i)
                    finish()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("acro", acro)  // save input acronym
        if(acroList.count()>0) outState.putStringArrayList("acroList", acroList)    // save list of found longforms
        Log.d(TAG, "onSaveInstanceState() acroList count: ${outState.getStringArrayList("acroList")?.count()}")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        acro = savedInstanceState.getString("acro")?:""
        supportActionBar?.setTitle(acro)

        acroList = savedInstanceState.getStringArrayList("acroList") as ArrayList<String>
        val restoreAdapter = AcroAdapter(applicationContext, acroList)
        listView.adapter = restoreAdapter

        Log.d(TAG, "onRestoreInstanceState() acroList count: ${acroList.count()}")
    }
}