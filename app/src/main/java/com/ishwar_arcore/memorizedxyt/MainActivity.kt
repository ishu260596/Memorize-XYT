package com.ishwar_arcore.memorizedxyt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        memoryGame.flipCard(position)
        adapter.notifyDataSetChanged()
    }

    private fun setViews() {
        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)
        setRecyclerView()
    }
}