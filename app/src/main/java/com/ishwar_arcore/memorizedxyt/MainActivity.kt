package com.ishwar_arcore.memorizedxyt

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ishwar_arcore.memorizedxyt.model.BoardSize
import com.ishwar_arcore.memorizedxyt.model.MemoryCard
import com.ishwar_arcore.memorizedxyt.model.MemoryGame
import com.ishwar_arcore.memorizedxyt.views.MemoryBoardAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var clRoot: CoordinatorLayout
    private lateinit var rvBoard: RecyclerView
    private lateinit var tvNumMoves: TextView
    private lateinit var tvNumPairs: TextView

    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var memoryGame: MemoryGame


    private var boardSize: BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setViews()
    }

    private fun setRecyclerView() {

        memoryGame = MemoryGame(boardSize)

        adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards,
            object : MemoryBoardAdapter.CardClickListener {
                override fun onCardClick(position: Int) {
                    Log.d("tag", "Hi I am from $position")
                    updateGameFlip(position)
                }
            })
        rvBoard.adapter = adapter
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
        rvBoard.hasFixedSize()

    }

    private fun updateGameFlip(position: Int) {
        if (memoryGame.haveWonTehGame()) {
            Snackbar.make(clRoot, "You already won", Snackbar.LENGTH_LONG).show()
            return
        }

        if (memoryGame.isCardFlipUp(position)) {
            Snackbar.make(clRoot, "Invalid move", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (memoryGame.flipCard(position)) {
            tvNumPairs.text = "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getPairs()}"
            val color = ArgbEvaluator().evaluate(
                memoryGame.numPairsFound.toFloat() / boardSize.getPairs(),
                ContextCompat.getColor(this, R.color.color_progress_zero),
                ContextCompat.getColor(this, R.color.color_progress_full)
            ) as Int
            tvNumPairs.setTextColor(color)
            if (memoryGame.haveWonTehGame()) {
                Snackbar.make(clRoot, "You won! Congratulations", Snackbar.LENGTH_LONG).show()
            }
        }
        tvNumMoves.text = "Moves: ${memoryGame.getNumMoves()}"
        adapter.notifyDataSetChanged()
    }

    private fun setViews() {
        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)
        tvNumPairs.setTextColor(ContextCompat.getColor(this, R.color.color_progress_zero))
        setRecyclerView()
    }
}