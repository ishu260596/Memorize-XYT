package com.ishwar_arcore.memorizedxyt.model

import com.ishwar_arcore.memorizedxyt.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize) {

    val cards: List<MemoryCard>
    var numPairsFound = 0

    private var numCardFlips = 0
    private var indexOfSingleSelectedCard: Int? = null

    init {
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getPairs())
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        cards = randomizedImages.map { MemoryCard(it) }
    }


    fun flipCard(position: Int): Boolean {
        val card = cards[position]

        /**
        Three cases
        0 cards previously flipped over => restore cards + flip over the selected card
        1 card previously flipped over => flip over the selected card + check if the images match
        2 cards previously flipped over => restore cards + flip over the selected card
         **/
        var foundMatch = false
        if (indexOfSingleSelectedCard == null) {

            restoreCards()
            indexOfSingleSelectedCard = position

        } else {
            //exactly ne card is flipped over
            val foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }

        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if (cards[position1].identifier != cards[position2].identifier) {
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        for (card in cards) {

            if (!card.isMatched) {
                card.isFaceUp = false
            }

        }
    }


}