package com.anjumohant.chrome_controller

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton

class Switching_mode : AppCompatActivity() {
    var toggle_button_mode: ToggleButton? = null
    var starts_time: TextView? = null
    var end_time: TextView? = null
    var sharedPreferences: SharedPreferences? = null
    var in_whitelist:TextView?=null
    var in_blacklist:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switching_mode)
        starts_time = findViewById<TextView>(R.id.from_time_starts) as TextView
        end_time = findViewById<TextView>(R.id.to_time_ends) as TextView
        in_whitelist=findViewById<TextView>(R.id.in_whitelis) as TextView
        in_blacklist=findViewById<TextView>(R.id.in_blacklis) as TextView
        toggle_button_mode = findViewById<ToggleButton>(R.id.toggleButton_mode) as ToggleButton
        sharedPreferences = getSharedPreferences("Time", Context.MODE_PRIVATE)
        val from = sharedPreferences!!.getString("from", " ")
        val to = sharedPreferences!!.getString("to", " ")
        var whiteList = sharedPreferences!!.getBoolean("whitelist", true)
        if(whiteList==true){
            in_whitelist!!.visibility=View.VISIBLE
            in_blacklist!!.visibility=View.INVISIBLE
            toggle_button_mode!!.isChecked=false
        }
        else{
            in_whitelist!!.visibility=View.INVISIBLE
            in_blacklist!!.visibility=View.VISIBLE
            toggle_button_mode!!.isChecked=true
        }
        starts_time!!.text = from
        end_time!!.text = to
        toggle_button_mode!!.setOnCheckedChangeListener { _, isChecked ->
            if (whiteList == true) {
                Toast.makeText(this, "Blacklist Mode On", Toast.LENGTH_SHORT).show()
                //setting the boolean value for black mode
                whiteList = false
                in_whitelist!!.visibility=View.INVISIBLE
                in_blacklist!!.visibility=View.VISIBLE
                val editor = sharedPreferences!!.edit()
                editor.putBoolean("whitelist", whiteList)
                editor.commit()
            }
else{
                Toast.makeText(this, "Whitelist Mode On", Toast.LENGTH_SHORT).show()
                //setting the boolean value for black mode
                whiteList = true
                in_whitelist!!.visibility=View.VISIBLE
                in_blacklist!!.visibility=View.INVISIBLE
                val editor = sharedPreferences!!.edit()
                editor.putBoolean("whitelist", whiteList)
                editor.commit()
            }

        }

    }
}