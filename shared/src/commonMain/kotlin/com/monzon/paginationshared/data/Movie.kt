package com.monzon.paginationshared.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val title: String,
    @SerialName("poster_path")
    val posterPath: String?,
    val overview: String
)

@Serializable
data class MovieResponse(
    val page: Int,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int,
    val results: List<Movie>
)