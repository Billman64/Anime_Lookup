package com.github.billman64.acronymapp_albertsons_challenge.Model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.github.billman64.acronymapp_albertsons_challenge.R

class AcroAdapter(private val context: Context, private val acroList:ArrayList<String>): BaseAdapter() {
    private lateinit var lf:TextView

    override fun getCount(): Int {
        return acroList.size
    }

    override fun getItem(p0: Int): Any {
        return acroList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var cv = LayoutInflater.from(context).inflate(R.layout.list_item, parent,false)
        lf = cv.findViewById(R.id.lf)
        lf.text = acroList[position]
        return cv
    }
}