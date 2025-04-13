package com.example.sporttimerapp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment


class SetsPickerDialog(timeText: TextView,
                        timeText1: TextView,
                        timeText2: TextView,
                        timeText3: TextView,
                        setsCount: TextView) : DialogFragment() {

    private val time: TextView = timeText
    private val time1: TextView = timeText1
    private val time2: TextView = timeText2
    private val time3: TextView = timeText3
    private val sets: TextView = setsCount

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sets_dialog, container, false)
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

        val minButton: Button = view.findViewById(R.id.buttonMinus)
        val plusButton: Button = view.findViewById(R.id.buttonPlus)

        val setsText: TextView = view.findViewById(R.id.textView7)
        var setscount: Int = 1
        setsText.text = sets.text.toString()

        val exitButton: Button = view.findViewById(R.id.timeButton1)
        val saveButton: Button = view.findViewById(R.id.timeButton2)

        minButton.setOnClickListener {
            if (setsText.text.toString().toInt() > 0) {
                setscount--
            }
            setsText.text = setscount.toString()
        }

        plusButton.setOnClickListener {
            setscount++
            setsText.text = setscount.toString()
        }


        saveButton.setOnClickListener {
            this.sets.text = setsText.text.toString()
            totalTime(time, time1, time2, time3, sets)
            dismiss()
        }

        exitButton.setOnClickListener {
            dismiss()
        }
    }

    private fun totalTime(time: TextView, time1: TextView, time2: TextView, time3: TextView, sets: TextView) {

        val ttime1: Int = (time.text.split(":")[0].toInt() * 60) + time.text.split(":")[1].toInt()
        val ttime2: Int = (time1.text.split(":")[0].toInt() * 60) + time1.text.split(":")[1].toInt()
        val ttime3: Int = (time2.text.split(":")[0].toInt() * 60) + time2.text.split(":")[1].toInt()
        val sets: Int = sets.text.toString().toInt()

        val ttime: Int = (ttime1 + ttime2 + ttime3) * sets

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