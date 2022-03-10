package com.anjumohant.chrome_controller

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import java.util.*

class PopUpWindow : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {
    var time_picker_from: TimePickerDialog? = null
    var time_picker_to: TimePickerDialog? = null
    var isFromtime: Boolean = false
    val mcurrentTime: Calendar = Calendar.getInstance()
    val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
    val minute = mcurrentTime.get(Calendar.MINUTE)

    var selected_from_hour :Int? =null
    var selected_from_minute:Int ?= null

    var selected_to_hour:Int ?=null
    var selected_to_minute :Int? = null
    private var select_time_from: TextView? = null
    private var select_time_to: TextView? = null
    private var save_time_btn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up_window)
        select_time_from = findViewById<TextView>(R.id.from_time) as TextView
        select_time_to = findViewById<TextView>(R.id.to_time) as TextView
        save_time_btn = findViewById<Button>(R.id.save_btn)
        select_time_from!!.setOnClickListener {
            time_picker_from =
                TimePickerDialog(this@PopUpWindow, this@PopUpWindow, hour, minute, false)
            time_picker_from!!.show()
            isFromtime = true

        }
        select_time_to!!.setOnClickListener {
            time_picker_to =
                TimePickerDialog(this@PopUpWindow, this@PopUpWindow, hour, minute, false)
            time_picker_to!!.show()
            isFromtime = false

        }
        save_time_btn!!.setOnClickListener {
save_time()
        }
    }

    private fun save_time() {
        val sharedPreference=getSharedPreferences("Time",Context.MODE_PRIVATE)
        var editor=sharedPreference.edit()
        editor.putString("from", select_time_from!!.text.toString())
        editor.putString("to", select_time_to!!.text.toString())
        editor.commit()
        val intent = Intent(this, Switching_mode::class.java)
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if (isFromtime == true) {
            selected_from_hour=hourOfDay
            selected_from_minute=minute
            select_time_from!!.text = "$hourOfDay:$minute"
        } else {
            selected_to_hour=hourOfDay
            selected_to_minute=minute
            select_time_to!!.text = "$hourOfDay:$minute"
        }
    }


}