package com.ishwar_arcore.memorizedxyt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ishwar_arcore.memorizedxyt.model.BoardSize
import com.ishwar_arcore.memorizedxyt.utils.DEFAULT_ICONS
import com.ishwar_arcore.memorizedxyt.views.MemoryBoardAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var clRoot: CoordinatorLayout
    private lateinit var rvBoard: RecyclerView
    private lateinit var tvNumMoves: TextView
    private lateinit var tvNumPairs: TextView

    private lateinit var adapter: MemoryBoardAdapter


    private var boardSize: BoardSize = BoardSize.MEDIUM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setViews()
    }

    private fun setRecyclerView() {
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getPairs())
        val randomizedImages = (chosenImages + chosenImages).shuffled()



        adapter = MemoryBoardAdapter(this, boardSize, randomizedImages,
            object : MemoryBoardAdapter.CardClickListener {
                override fun onCardClick(position: Int) {
                    Toast.makeText(
                        this@MainActivity,
                        "Hi There I am on$position",
                        Toast.LENGTH_SHORT
                    ).show()
//                    updateCardFlip(position)
                }
            })
        rvBoard.adapter = adapter
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())
        rvBoard.hasFixedSize()


    }

    private fun updateCardFlip(position: Int) {

    }

    private fun setViews() {
        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)
        setRecyclerView()

    }
}