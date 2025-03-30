package com.monzon.paginationshared

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.monzon.paginationshared.data.Movie
import com.monzon.paginationshared.data.PopularMoviesAPI

internal class PopularsMoviesRepository(private val api: PopularMoviesAPI) :
    PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val pageNumber = params.key ?: 1
        return try {
            val response = api.getMovies(page = pageNumber)
            val prevKey = if (pageNumber == 1) null else pageNumber - 1
            val nextKey = if (pageNumber < response.totalPages) pageNumber + 1 else null
            LoadResult.Page(data = response.results, prevKey = prevKey, nextKey = nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}