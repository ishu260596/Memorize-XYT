package com.ishwar_arcore.memorizedxyt.views

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.jinatonic.confetti.CommonConfetti
import com.google.android.material.snackbar.Snackbar
import com.ishwar_arcore.memorizedxyt.R
import com.ishwar_arcore.memorizedxyt.model.BoardSize
import com.ishwar_arcore.memorizedxyt.model.MemoryGame


class MainActivity : AppCompatActivity() {
    private lateinit var clRoot: CoordinatorLayout
    private lateinit var rvBoard: RecyclerView
    private lateinit var tvNumMoves: TextView
    private lateinit var tvNumPairs: TextView

    //    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var audioManager: AudioManager

    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var memoryGame: MemoryGame

    private var boardSize: BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_refresh -> {
                if (memoryGame.getNumMoves() > 0 && !memoryGame.haveWonTehGame()) {
                    showAlertDialogueBox(
                        "Want to quite your current game?",
                        null,
                        View.OnClickListener {
                            setUpBoard()
                        }
                    )
                } else {
                    setUpBoard()
                }
            }

            R.id.mi_new_size -> {
                showNewSizeAlertDialogue()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showNewSizeAlertDialogue() {
        val boardSizeView = LayoutInflater.from(this)
            .inflate(R.layout.dialoge_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroupSize)

        when (boardSize) {
            BoardSize.EASY -> radioGroupSize.check(R.id.rbEasy)
            BoardSize.MEDIUM -> radioGroupSize.check(R.id.rbMedium)
            BoardSize.HARD -> radioGroupSize.check(R.id.rbHard)
        }

        showAlertDialogueBox("Choose new size",
            boardSizeView,
            View.OnClickListener {
                boardSize = when (radioGroupSize.checkedRadioButtonId) {
                    R.id.rbEasy -> BoardSize.EASY
                    R.id.rbMedium -> BoardSize.MEDIUM
                    else -> BoardSize.HARD
                }
                setUpBoard()
            })

    }

    private fun showAlertDialogueBox(
        title: String,
        view: View?,
        positiveClickListener: View.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK") { _, _ ->
                positiveClickListener.onClick(null)
            }.show()

    }

    private fun setViews() {
        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)
//        mediaPlayer = MediaPlayer.create(this, R.raw.)

        audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        setUpBoard()
    }

    private fun setUpBoard() {
        tvNumPairs.setTextColor(ContextCompat.getColor(this, R.color.color_progress_zero))

        when (boardSize) {
            BoardSize.EASY -> {
                tvNumMoves.text = "Easy: 4 x 2"
                tvNumPairs.text = "Pairs: 0/4"
            }
            BoardSize.MEDIUM -> {
                tvNumMoves.text = "Medium: 6 x 3"
                tvNumPairs.text = "Pairs: 0/9"
            }
            BoardSize.HARD -> {
                tvNumMoves.text = "Easy: 6 x 4"
                tvNumPairs.text = "Pairs: 0/12"
            }
        }

        memoryGame = MemoryGame(boardSize)

        adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards,
            object : MemoryBoardAdapter.CardClickListener {
                override fun onCardClick(position: Int) {
                    audioManager.playSoundEffect(SoundEffectConstants.CLICK)
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
                showConfetti()
                Snackbar.make(clRoot, "You won! Congratulations", Snackbar.LENGTH_LONG).show()
            }
        }
        tvNumMoves.text = "Moves: ${memoryGame.getNumMoves()}"
        adapter.notifyDataSetChanged()
    }

    private fun showConfetti() {

        CommonConfetti.rainingConfetti(
            clRoot, intArrayOf(
                Color.BLUE,
                Color.WHITE,
                Color.RED,
                Color.YELLOW,
                Color.MAGENTA,
                Color.CYAN,
                Color.GREEN,
                Color.DKGRAY
            )
        ).oneShot()
            .setVelocityX(5000f)
            .setEmissionDuration(1000)

    }

}