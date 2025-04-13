package com.example.sporttimerapp


import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.media.MediaPlayer
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class TimerActivity : AppCompatActivity() {

    private lateinit var timerView: TextView
    private lateinit var stageView: TextView
    private lateinit var setsCount: TextView

    private val handler = Handler(Looper.getMainLooper())
    private var pauseRunnable: Runnable? = null

    private var sets = 0
    private var preparationTime = 0
    private var workTime = 0
    private var chillTime = 0

    private var currentSet = 1
    private var timeLeft = 0
    private var isPaused = false

    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var setPlayer: MediaPlayer
    private lateinit var endPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_timer)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.timer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        timerView = findViewById(R.id.timer1)
        stageView = findViewById(R.id.textView6)
        setsCount = findViewById(R.id.setsCount)

        mediaPlayer = MediaPlayer.create(this@TimerActivity, R.raw.countdown)
        setPlayer = MediaPlayer.create(this@TimerActivity, R.raw.setend)
        endPlayer = MediaPlayer.create(this@TimerActivity, R.raw.end)

        findViewById<Button>(R.id.pause).setOnClickListener {
            togglePause()
        }

        findViewById<Button>(R.id.stopExit).setOnClickListener {
            handler.removeCallbacksAndMessages(null)
            finish()
        }

        sets = intent.extras?.getInt("sets") ?: 1
        preparationTime = intent.extras?.getInt("preparationTime") ?: 1
        workTime = intent.extras?.getInt("workTime") ?: 1
        chillTime = intent.extras?.getInt("chillTime") ?: 1

        startPreparation()
    }

    private fun togglePause() {
        isPaused = !isPaused
        val pauseButton = findViewById<Button>(R.id.pause)
        pauseButton.text = if (isPaused) "Старт" else "Пауза"

        if (!isPaused && pauseRunnable != null) {
            handler.postDelayed(pauseRunnable!!, 1000)
        } else if (isPaused) {
            handler.removeCallbacks(pauseRunnable!!)
        }
    }

    private fun startPreparation() {
        timeLeft = preparationTime
        stageView.text = "Подготовка"
        var backgrounColor : ConstraintLayout = findViewById(R.id.timer)
        backgrounColor.setBackgroundColor(Color.BLACK)
        timerView.setTextColor(Color.WHITE)
        stageView.setTextColor(Color.WHITE)
        setsCount.setTextColor(Color.WHITE)
        timerView.text = toMinSec(timeLeft)

        setsCount.text = "Сет: $currentSet/$sets"

        val runnable = object : Runnable {
            override fun run() {
                if (!isPaused) {
                    timeLeft--
                    if (timeLeft >= 0) {
                        when(timeLeft){
                            3 -> mediaPlayer.start()
                            2 -> mediaPlayer.start()
                            1 -> mediaPlayer.start()
                        }
                        if (timeLeft == 0){
                            setPlayer.start()
                        }
                        timerView.text = toMinSec(timeLeft)
                        handler.postDelayed(this, 1000)
                    } else {
                        startWork()
                    }
                } else {
                    pauseRunnable = this
                }
            }
        }

        pauseRunnable = runnable
        handler.postDelayed(runnable, 1000)
    }

    private fun startWork() {
        setsCount.text = "Сет: $currentSet/$sets"
        timeLeft = workTime
        stageView.text = "Работа"
        var backgrounColor : ConstraintLayout = findViewById(R.id.timer)
        backgrounColor.setBackgroundColor(Color.WHITE)
        stageView.setTextColor(Color.BLACK)
        timerView.setTextColor(Color.BLACK)
        setsCount.setTextColor(Color.BLACK)
        timerView.text = toMinSec(timeLeft)

        val runnable = object : Runnable {
            override fun run() {
                if (!isPaused) {
                    timeLeft--
                    if (timeLeft >= 0) {
                        when(timeLeft){
                            3 -> mediaPlayer.start()
                            2 -> mediaPlayer.start()
                            1 -> mediaPlayer.start()
                        }
                        if (timeLeft == 0 && currentSet != sets){
                            setPlayer.start()
                        }
                        timerView.text = toMinSec(timeLeft)
                        handler.postDelayed(this, 1000)
                    } else {
                        if (currentSet < sets) {
                            startChill()
                        } else {
                            endPlayer.start()
                            stageView.text = "Готово"
                            val backgrounColor : ConstraintLayout = findViewById(R.id.timer)
                            stageView.setTextColor(Color.WHITE)
                            backgrounColor.setBackgroundColor(Color.GRAY)
                            timerView.setTextColor(Color.WHITE)
                            setsCount.setTextColor(Color.WHITE)
                            timerView.text = "00:00"
                        }
                    }
                } else {
                    pauseRunnable = this
                }
            }
        }

        pauseRunnable = runnable
        handler.postDelayed(runnable, 1000)
    }

    private fun startChill() {
        timeLeft = chillTime
        stageView.text = "Отдых"
        var backgrounColor : ConstraintLayout = findViewById(R.id.timer)
        stageView.setTextColor(Color.WHITE)
        backgrounColor.setBackgroundColor(Color.BLACK)
        timerView.setTextColor(Color.WHITE)
        setsCount.setTextColor(Color.WHITE)
        timerView.text = toMinSec(timeLeft)

        val runnable = object : Runnable {
            override fun run() {
                if (!isPaused) {
                    timeLeft--
                    if (timeLeft >= 0) {
                        when(timeLeft){
                            3 -> mediaPlayer.start()
                            2 -> mediaPlayer.start()
                            1 -> mediaPlayer.start()
                        }
                        if (timeLeft == 0){
                            setPlayer.start()
                        }
                        timerView.text = toMinSec(timeLeft)
                        handler.postDelayed(this, 1000)
                    } else {
                        currentSet++
                        startWork()
                    }
                } else {
                    pauseRunnable = this
                }
            }
        }

        pauseRunnable = runnable
        handler.postDelayed(runnable, 1000)
    }

    private fun toMinSec(seconds: Int): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", mins, secs)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized){
            mediaPlayer?.release()
        }
        if (::setPlayer.isInitialized){
            setPlayer?.release()
        }
        if (::endPlayer.isInitialized){
            endPlayer?.release()
        }
    }
}
