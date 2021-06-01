package com.ishwar_arcore.memorizedxyt.model

data class MemoryCard(
    val identifier: Int,
    var isFaceUp: Boolean = false,
    var isMatched: Boolean = false
)
