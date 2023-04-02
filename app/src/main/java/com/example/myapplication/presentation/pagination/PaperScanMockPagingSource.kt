package com.example.myapplication.presentation.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplication.presentation.PAPER_SCAN_MAX_ITEMS_COUNT
import com.example.myapplication.presentation.paperadapter.PaperScanItem

class PaperScanMockPagingSource : PagingSource<Int, PaperScanItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PaperScanItem> {
        val page = params.key ?: 0
        val startIndex = page * PAPER_SCAN_MAX_ITEMS_COUNT + 1
        val endIndex = (page + 1) * PAPER_SCAN_MAX_ITEMS_COUNT

        val listItems = (startIndex..endIndex).toList().map {
            PaperScanItem(it.toString())
        }
        val prevKey = if (page == 0) {
            null
        } else {
            page - 1
        }
        val nextKey = page + 1
        return LoadResult.Page(listItems, prevKey, nextKey)
    }

    override fun getRefreshKey(state: PagingState<Int, PaperScanItem>): Int? {
        return null
    }

}
