package com.example.sporttimerapp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class TimerPickerDialog(timeText: TextView,
                        timeText1: TextView,
                        timeText2: TextView,
                        timeText3: TextView,
                        setsCount: TextView,
                        prepTime: TextView) : DialogFragment() {

    private val time: TextView = timeText
    private val time1: TextView = timeText1
    private val time2: TextView = timeText2
    private val time3: TextView = timeText3
    private val sets: TextView = setsCount
    private val prepTime: TextView = prepTime

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_custom, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            (resources.displayMetrics.widthPixels * 0.7).toInt(),
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val npMinutes: NumberPicker = view.findViewById(R.id.numberPicker1)
        val npSeconds: NumberPicker = view.findViewById(R.id.numberPicker2)

        npMinutes.minValue = 0
        npMinutes.maxValue = 59

        npSeconds.minValue = 0
        npSeconds.maxValue = 59

        npMinutes.value = time.text.toString().split(":")[0].toInt()
        npSeconds.value = time.text.toString().split(":")[1].toInt()

        val exitButton: Button = view.findViewById(R.id.timeButton1)
        val saveButton: Button = view.findViewById(R.id.timeButton2)

        saveButton.setOnClickListener {
            var minutes = npMinutes.value.toString()
            var seconds = npSeconds.value.toString()

            if (minutes.length != 2){
                minutes = "0$minutes"
            }

            if (seconds.length != 2){
                seconds = "0$seconds"
            }

            this.time.text = "$minutes:$seconds"
            totalTime(time, time1, time2, time3, sets, prepTime)
            dismiss()
        }

        exitButton.setOnClickListener {
            dismiss()
        }
    }

    private fun totalTime(time: TextView,
                          time1: TextView,
                          time2: TextView,
                          time3: TextView,
                          sets: TextView,
                          prepTime: TextView) {

        val ttime1: Int = (time.text.split(":")[0].toInt() * 60) + time.text.split(":")[1].toInt()
        val ttime2: Int = (time1.text.split(":")[0].toInt() * 60) + time1.text.split(":")[1].toInt()
        val ttime3: Int = (time2.text.split(":")[0].toInt() * 60) + time2.text.split(":")[1].toInt()
        val sets: Int = sets.text.toString().toInt()
        val prepTime: Int = (prepTime.text.split(":")[0].toInt() * 60) + prepTime.text.split(":")[1].toInt()

        val ttime: Int = (ttime1 + ttime2 + ttime3) * sets - prepTime * sets

        var tminutes: String = (ttime / 60).toString()
        var tseconds: String = (ttime % 60).toString()

        if (tminutes.length < 2){
            tminutes = "0$tminutes"
        }

        if (tseconds.length < 2){
            tseconds = "0$tseconds"
        }

        time3.text = "$tminutes:$tseconds"
    }
}