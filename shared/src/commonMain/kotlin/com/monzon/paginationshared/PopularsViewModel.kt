package com.monzon.paginationshared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ItemSnapshotList
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingDataEvent
import androidx.paging.PagingDataPresenter
import androidx.paging.cachedIn
import com.monzon.paginationshared.data.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PopularsViewModel : ViewModel(), KoinComponent {
    private val repository: PopularsMoviesRepository by inject()

    private val pager: Pager<Int, Movie> = run {
        val config = PagingConfig(pageSize = 20)
        Pager(config) {
            repository
        }
    }

    val pagerFlow = pager.flow.cachedIn(viewModelScope)


    //region used by iOS
    private val pagingDataPresenter = object : PagingDataPresenter<Movie>() {
        override suspend fun presentPagingDataEvent(event: PagingDataEvent<Movie>) {
            updateMoviesSnapshotList()
        }
    }
    val moviesSnapshotList: MutableStateFlow<ItemSnapshotList<Movie>> =
        MutableStateFlow(pagingDataPresenter.snapshot())

    val loadStateFlow = pagingDataPresenter.loadStateFlow

    init {
        viewModelScope.launch {
            pagerFlow.collectLatest {
                pagingDataPresenter.collectFrom(it)
            }
        }
    }

    private fun updateMoviesSnapshotList() {
        moviesSnapshotList.value = pagingDataPresenter.snapshot()
    }

    fun loadMore() {
        val index = pagingDataPresenter.size - 1
        // This triggers to get next pages behind the scenes
        pagingDataPresenter[index]
    }

    fun retry() {
        pagingDataPresenter.retry()
    }
    //endregion
}