package com.github.billman64.anime_lookup.Model

data class AnimeShowData(
    val mal_id: Int,
    val url: String,
    val image_url: String,
    val title: String,
    val airing: Boolean,
    val synopsis: String,
    val type: String,
    val episodes: Int,
    val score: Double,
    val start_date: String,
    val end_date: String,
    val members: Int,
    val rated: String
)
