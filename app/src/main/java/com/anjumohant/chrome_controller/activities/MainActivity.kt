package com.anjumohant.chrome_controller

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import android.content.Intent
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils.SimpleStringSplitter
import android.view.View
import android.widget.Button


class MainActivity : AppCompatActivity() {
    private var Addschedule: View? = null
    private var mStatus: TextView? = null
    private var mLogs: TextView? = null
    private var btnSet: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSet = findViewById<View>(R.id.button) as Button
        mStatus = findViewById<View>(R.id.textView) as TextView
        Addschedule = findViewById<View>(R.id.ellipse_1) as View
        val sharedPreference=getSharedPreferences("Time",Context.MODE_PRIVATE)

if(sharedPreference!!.getString("from"," ") != " "){
    val intent = Intent(this, Switching_mode::class.java)
    startActivity(intent)

}
        Addschedule!!.setOnClickListener {
            val intent = Intent(this, PopUpWindow::class.java)
            startActivity(intent)
        }
        btnSet!!.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            //   Uri uri = Uri.fromParts("package", getPackageName()+"/"+getPackageName()+".NotificationListener", null);
            //  intent.setData(uri);
            startActivity(intent)
        }
        DoInit()
    }

    fun DoInit() {
        val bset = isAccessibilitySettingsOn(this)
        if (bset == true) {
            btnSet!!.isEnabled = false
            mStatus!!.text = "Permission Granted"
        } else {
            btnSet!!.isEnabled = true
            mStatus!!.text = "Permission Pending"
        }
    }

    override fun onResume() {
        DoInit()
//        if (mLogs != null) {
//            mLogs!!.text = strLogs
//            mLogs!!.movementMethod = ScrollingMovementMethod()
//        }
        super.onResume()
    }

    private fun isAccessibilitySettingsOn(mContext: Context): Boolean {
        var accessibilityEnabled = 0
        val service = packageName + "/" + ServiceHandling::class.java.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                mContext.applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: SettingNotFoundException) {
        }
        val mStringColonSplitter = SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                mContext.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        } else {
        }
        return false
    }

}