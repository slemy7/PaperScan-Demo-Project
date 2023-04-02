package com.example.myapplication.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.myapplication.presentation.pagination.PaperScanMockRepository
import com.example.myapplication.presentation.paperadapter.PaperScanPaginatedPaginatedAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PaperScanViewModel : ViewModel() {
    private val paperScanMockRepository = PaperScanMockRepository()

    private val _shouldDisplayMenuItem = MutableLiveData<Boolean>(false)
    val shouldDisplayMenuItem: LiveData<Boolean> = _shouldDisplayMenuItem

    fun updateMenuItemVisibility(isVisible: Boolean) {
        _shouldDisplayMenuItem.value = isVisible
    }

    fun fetchMockPaperScanItems(): Flow<PagingData<PaperScanPaginatedPaginatedAdapter.PaperScanItemWrapper>> {
        return paperScanMockRepository.paperScanItemsFlow().map { pagingData ->
            pagingData.map { PaperScanPaginatedPaginatedAdapter.PaperScanItemWrapper(it, false, -1) }
        }.cachedIn(viewModelScope)
    }
}