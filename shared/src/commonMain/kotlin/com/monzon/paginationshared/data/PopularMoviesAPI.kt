package com.monzon.paginationshared.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class PopularMoviesAPI {

    companion object {
        const val ACCESS_TOKEN =
            "YOUR_TOKEN"
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
            level = LogLevel.ALL
        }
    }

    suspend fun getMovies(page: Int = 1): MovieResponse {
        return client.get(BASE_URL + "discover/movie") {
            parameter("page", page.toString())
            headers {
                append("Authorization", "Bearer $ACCESS_TOKEN")

            }
        }.body()
    }
}