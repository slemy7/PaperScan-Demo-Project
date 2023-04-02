package com.example.myapplication.presentation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.PaperscanFragmentBinding
import com.example.myapplication.extensions.viewBinding
import com.example.myapplication.presentation.paperadapter.PaperScanAdapter
import com.example.myapplication.presentation.paperadapter.PaperScanItem
import com.example.myapplication.presentation.paperadapter.PaperScanPaginatedPaginatedAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

const val PAPER_SCAN_MAX_ITEMS_COUNT = 10

class PaperScanFragment : Fragment() {

    private var viewBinding: PaperscanFragmentBinding by viewBinding()
    private val viewModel by viewModels<PaperScanViewModel>()
    private lateinit var paperScanAdapter: PaperScanAdapter
    private lateinit var paginatedPaginatedAdapter: PaperScanPaginatedPaginatedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return PaperscanFragmentBinding.inflate(inflater, container, false)
            .also {
                viewBinding = it
                viewBinding.viewModel = viewModel
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        //setupPaginatedAdapter()
    }

    private fun setupPaginatedAdapter() {
        paginatedPaginatedAdapter = PaperScanPaginatedPaginatedAdapter { selectedItemsCount ->
            viewModel.updateMenuItemVisibility(selectedItemsCount > 1)
        }
        viewBinding.rvPaperScan.adapter = paginatedPaginatedAdapter
        fetchPaperScanItems()
    }

    private fun setupListAdapter() {
        val itemsList =
            (1..PAPER_SCAN_MAX_ITEMS_COUNT).toList().map { PaperScanItem(it.toString()) }
        paperScanAdapter = PaperScanAdapter { selectedItemsCount ->
            viewModel.updateMenuItemVisibility(selectedItemsCount > 1)
        }
        paperScanAdapter.setAdapterItems(itemsList.toMutableList())
        viewBinding.rvPaperScan.adapter = paperScanAdapter

        viewBinding.tvOrganize.setOnClickListener {
            paperScanAdapter.organizeSelectedItem()
            paperScanAdapter.fillWithItems(PAPER_SCAN_MAX_ITEMS_COUNT)
        }
    }

    private fun fetchPaperScanItems() {
        lifecycleScope.launch {
            viewModel.fetchMockPaperScanItems().distinctUntilChanged().collectLatest {
                paginatedPaginatedAdapter.submitData(it)
            }
        }
    }
}