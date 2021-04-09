package com.github.billman64.anime_lookup.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.billman64.anime_lookup.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG:String = this.javaClass.simpleName + "--demo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI setup

        acronym.setText("dragon ball z")

        error.visibility = View.GONE
        button.setOnClickListener{
            getAnimeData()
        }


        // Error handling

        val bundle:Bundle? = intent.extras
        bundle?.let{
            val errorCode = it.getString("error")

            error.text = errorCode
            error.visibility = View.VISIBLE
        }
    }

    private fun getAnimeData(){

        if(acronym.text.isNullOrBlank()) return     //reject blank input
        else{

            // intent for list activity
            val i = Intent(this, listActivity::class.java)
            val acro = acronym.text.toString().trim()

            i.putExtra("anime", acro)
            startActivity(i)
        }
    }
}