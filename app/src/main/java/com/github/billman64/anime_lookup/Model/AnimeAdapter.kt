package com.github.billman64.anime_lookup.Model

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.billman64.anime_lookup.R
import kotlinx.android.synthetic.main.list_item.view.*

class AnimeAdapter(private val animeList:ArrayList<AnimeShowData>): RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {
    val TAG = this.javaClass.simpleName + "--demo"
    private lateinit var title:TextView

    class AnimeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        // View references of an individual item
        val titleTv:TextView = itemView.title
        val synopsisTv:TextView = itemView.synopsis
        val ratedTv:TextView = itemView.rated
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeAdapter.AnimeViewHolder {
        Log.d(TAG, "onCreateViewHolder()")
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return AnimeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AnimeAdapter.AnimeViewHolder, position: Int) {
        // The place for an onClickListener for each view, dynamically.
        // Warning: Doing too much here may degrade app performance.

        val currentItem = animeList[position]

        holder.titleTv.text = currentItem.title
        holder.synopsisTv.text = currentItem.synopsis

        holder.ratedTv.text = "Rating: " + currentItem.rated
        //TODO: create and populate other views
    }

    override fun getItemCount(): Int {
        return animeList.count()
    }

}