package com.github.billman64.acronymapp_albertsons_challenge.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.billman64.acronymapp_albertsons_challenge.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG:String = this.javaClass.simpleName + "--demo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        error.visibility = View.GONE

        acronym.setText("aaa")   // temp for dev purposes only

        button.setOnClickListener{
            getAcronymData()
        }


        // Error handling

        val bundle:Bundle? = intent.extras
        bundle?.let{
            val errorCode = it.getString("error")


            error.text = errorCode
            error.visibility = View.VISIBLE
        }

    }

    private fun getAcronymData(){

        if(acronym.text.isNullOrBlank()) return
        else{

            // intent for list activity
            val i = Intent(this, listActivity::class.java)
            val acro = acronym.text.toString().trim()

            i.putExtra("acronym", acro)
            startActivity(i)
        }
    }
}