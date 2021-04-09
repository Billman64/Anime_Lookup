package com.github.billman64.anime_lookup.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.github.billman64.anime_lookup.Model.AnimeAPI
import com.github.billman64.anime_lookup.Model.AnimeAdapter
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
    private var inputAnime = ""
    private var responseCode = ""
    private var responseLength = 0
    private var animeList = ArrayList<AnimeShowData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        // Call to get anime data, assuming no savedInstanceState
        savedInstanceState?.let{ Log.d(TAG, " onCreate() with a savedInstanceState") }?: getAnimeData()
    }

    private fun getAnimeData(){

        val bundle:Bundle? = intent.extras
        bundle?.let{
            inputAnime = bundle.getString("anime").toString()

            // Set up UI

            supportActionBar?.setTitle(inputAnime)
            progressBar.visibility = View.VISIBLE

            // Retrofit builder

            val animeAPI = Retrofit.Builder()
                    .baseUrl("https://api.jikan.moe/v3/search/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(AnimeAPI::class.java)

            // Coroutine for network call

            GlobalScope.launch(Dispatchers.IO){

                try{
                    Log.v(TAG, "coroutine $animeAPI anime: $inputAnime")
                    Log.d(TAG, " url: ${animeAPI.getData(inputAnime).request().url()}")

                    // GET data


                    responseCode = animeAPI.getData(inputAnime).awaitResponse().code().toString()
                    responseLength = animeAPI.getData(inputAnime).awaitResponse().code().toString().length  // may not be needed


                    val response = animeAPI.getData(inputAnime).awaitResponse()
                    Log.d(TAG, " reponse code: ${response.code()}")
//                    Log.d(TAG, " response length: ${response.message().length}")

                    if(response.isSuccessful) Log.d(TAG, "body: ${response.body().toString().substring(0..50)}")
                            else Log.d(TAG, "errorBody: ${response.errorBody()}")


                    // Parse data ...

                    val resultsArray = response.body()?.getAsJsonArray("results")
                    Log.d(TAG, "resultsArray  count: ${resultsArray?.count()}  body: ${resultsArray.toString().substring(0..20)}")

                    for(i in 0 until resultsArray!!.size()){

                        var r = resultsArray[i].asJsonObject // Refactored for code readability

                        Log.v(TAG, "r  title: ${r.get("title").toString().substring(0..10)} id: ${r.get("mal_id")}")

                        var animeShowData = AnimeShowData(
                                r.get("mal_id").asInt,
                                r.get("url").asString.toString(),   // trim quotes off of Json strings
                                r.get("image_url").asString.toString(),
                                r.get("title").asString.toString(),
                                r.get("airing").asBoolean,
                                r.get("synopsis").asString.toString(),
                                r.get("type").asString.toString(),
                                r.get("episodes").asInt,
                                r.get("score").asDouble,
                                r.get("start_date").asString.toString(),
//                                r.get("end_date").asString.toString(),    //TODO: null handling for end_date
                                "endDate placeholder for null bug",
                                r.get("members").asInt,
                                r.get("rated").asString.toString()
                        )
                        //TODO: trim quotes
                            //  .subSequence(1.. r.get("rated").asString.length-2).toString()  quote-trimming like this can't be universally applied. ie: rating:"G"


//                        )
                        animeList.add(animeShowData)
                        Log.v(TAG, "i: $i  title: ${r.get("title")}")
                    }

                    Log.v(TAG, " dataList  last record:  ${animeList.get(it.size()-1)}")


                    // Update UI

                    withContext(Dispatchers.Main){
                        progressBar.visibility = View.GONE
                        Log.d(TAG, "")  // log spacer
                        Log.d(TAG, "Main dispacher. animeList size: ${animeList.size}")
                        Log.v(TAG, "rv child count (before): ${recyclerView.childCount}")

                        val animeAdapter = AnimeAdapter(animeList)
                        recyclerView.adapter = animeAdapter
                        Log.d(TAG, "rv child count: ${recyclerView.childCount}")
//                        Toast.makeText(this@listActivity, animeList[0].toString().substring(0..150), Toast.LENGTH_LONG)
                        Toast.makeText(applicationContext,"asdf",Toast.LENGTH_SHORT)
                    }
                    
                } catch(e: Exception){
                    Log.e(TAG," Error. message: ${e.message}")
                    Log.v(TAG, " response code: $responseCode")

                    withContext(Dispatchers.Main){
                        progressBar.visibility = View.GONE
                    }

                    val i = Intent(applicationContext, MainActivity::class.java)

                    if(responseCode == "404") responseCode = "${getString(R.string.not_found)}: $inputAnime"     // If not found in API, it returns '{"status":404,"type":"BadResponseException","'
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

        outState.putString("anime", inputAnime)  // save input anime
//        if(animeList.count()>0) outState.putParcelableArrayList ("animeList", animeList)    // save list of found anime search results
        Log.d(TAG, "onSaveInstanceState() animeList count: ${outState.getStringArrayList("animeList")?.count()}")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        inputAnime = savedInstanceState.getString("anime")?:""
        supportActionBar?.setTitle(inputAnime)

        animeList = savedInstanceState.getStringArrayList("animeList") as ArrayList<AnimeShowData>
        val restoreAdapter = AnimeAdapter(animeList)
        recyclerView.adapter = restoreAdapter

        Log.d(TAG, "onRestoreInstanceState() animeList count: ${animeList.count()}")
    }
}