package com.example.sporttimerapp


import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    private fun saveSettings(){
        val time: TextView = findViewById(R.id.time)
        val time1: TextView = findViewById(R.id.time1)
        val time2: TextView = findViewById(R.id.time2)
        val time4: TextView = findViewById(R.id.time4)
        val sets: TextView = findViewById(R.id.sets)
        val prefs = getSharedPreferences("TimerPresets", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("preptime", time.text.toString())
            putString("worktime", time1.text.toString())
            putString("chilltime", time2.text.toString())
            putString("totaltime", time4.text.toString())
            putString("sets", sets.text.toString())
            apply()
        }
    }

    private fun getSettings(): List<String?> {
        val prefs = getSharedPreferences("TimerPresets", Context.MODE_PRIVATE)
        val settingsExist = prefs.getString("preptime", null)
        if (settingsExist != null) {
            val prepTime = prefs.getString("preptime", "00:00")
            val workTime = prefs.getString("worktime", "00:00")
            val chillTime = prefs.getString("chilltime", "00:00")
            val totalTime = prefs.getString("totaltime", "00:00")
            val sets = prefs.getString("sets", "1")
            return listOf(prepTime, workTime, chillTime, totalTime, sets)
        } else {
            return listOf("0")
        }
    }

    override fun onPause() {
        super.onPause()
        saveSettings()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val time: TextView = findViewById(R.id.time)
        val time1: TextView = findViewById(R.id.time1)
        val time2: TextView = findViewById(R.id.time2)
        val time4: TextView = findViewById(R.id.time4)
        val sets: TextView = findViewById(R.id.sets)

        val settings = getSettings()

        if (settings[0] != "0"){
            time.text = settings[0]
            time1.text = settings[1]
            time2.text = settings[2]
            time4.text = settings[3]
            sets.text = settings[4]
        }

        time.setOnClickListener {
            var timeChange = TimerPickerDialog(time, time1, time2, time4, sets, time)
            timeChange.show(supportFragmentManager, "TimePickerDialog")
        }

        time1.setOnClickListener {
            var timeChange = TimerPickerDialog(time1, time, time2, time4, sets, time)
            timeChange.show(supportFragmentManager, "TimePickerDialog")
        }

        time2.setOnClickListener {
            var timeChange = TimerPickerDialog(time2, time, time1, time4, sets, time)
            timeChange.show(supportFragmentManager, "TimePickerDialog")
        }

        sets.setOnClickListener {
            var setChange = SetsPickerDialog(time, time1, time2, time4, sets)
            setChange.show(supportFragmentManager, "SetsPickerDialog")
        }

        val startButton: Button = findViewById(R.id.startButton)

        val currentMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        when (currentMode){
            Configuration.UI_MODE_NIGHT_NO -> {startButton.setBackgroundColor(Color.BLACK)}
            Configuration.UI_MODE_NIGHT_YES -> {startButton.setBackgroundColor(Color.WHITE)}
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {startButton.setBackgroundColor(Color.BLACK)}
        }

        when (currentMode){
            Configuration.UI_MODE_NIGHT_NO -> {startButton.setTextColor(Color.WHITE)}
            Configuration.UI_MODE_NIGHT_YES -> {startButton.setTextColor(Color.BLACK)}
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {startButton.setTextColor(Color.WHITE)}
        }

        startButton.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)

            val preparationTime: Int = time.text.split(":")[0].toInt() * 60 + time.text.split(":")[1].toInt()
            val workTime: Int = time1.text.split(":")[0].toInt() * 60 + time1.text.split(":")[1].toInt()
            val chillTime: Int = time2.text.split(":")[0].toInt() * 60 + time2.text.split(":")[1].toInt()
            val textsets: TextView = findViewById(R.id.sets)
            val sets: Int = textsets.text.toString().toInt()

            intent.putExtra("preparationTime", preparationTime)
            intent.putExtra("workTime", workTime)
            intent.putExtra("chillTime", chillTime)
            intent.putExtra("sets", sets)

            startActivity(intent)
        }
    }
}