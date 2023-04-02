package com.example.myapplication.presentation.pagination

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.myapplication.presentation.PAPER_SCAN_MAX_ITEMS_COUNT
import com.example.myapplication.presentation.paperadapter.PaperScanItem
import kotlinx.coroutines.flow.Flow

class PaperScanMockRepository {
    fun paperScanItemsFlow(pagingConfig: PagingConfig = getDefaultPagingConfig()): Flow<PagingData<PaperScanItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { PaperScanMockPagingSource() }
        ).flow
    }

    private fun getDefaultPagingConfig(): PagingConfig {
        return PagingConfig(pageSize = PAPER_SCAN_MAX_ITEMS_COUNT, enablePlaceholders = false)
    }
}