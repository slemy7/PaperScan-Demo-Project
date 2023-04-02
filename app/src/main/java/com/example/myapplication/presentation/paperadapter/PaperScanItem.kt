package com.example.myapplication.presentation.paperadapter

data class PaperScanItem(
    val displayText: String,
    var children: List<PaperScanItem> = listOf()
) {
    companion object {
        fun getNumberOfChildren(item: PaperScanItem): Int {
            var count = 0
            for (child in item.children) {
                count += getNumberOfChildren(child)
            }
            return count + item.children.size
        }
    }
}

