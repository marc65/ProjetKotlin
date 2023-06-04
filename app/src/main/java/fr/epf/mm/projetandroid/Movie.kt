package fr.epf.mm.projetandroid

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("backdrop_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val voteAverage: Float
)