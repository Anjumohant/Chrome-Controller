package com.anjumohant.chrome_controller

import android.accessibilityservice.AccessibilityService
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import java.util.*

class ServiceHandling : AccessibilityService() {
    val blocked_website_list = listOf<String>(
        "m.facebook.com",
        "mobile.twitter.com", "instagram.com/?hl=en", "instagram.com", "reddit.com", "9gag.com"
    )
    var time_set=PopUpWindow()
    private var whitelist: Boolean? = null
    private var time_from: String? = null
    private var time_to: String? = null
    var mcurrentTime: Calendar? = Calendar.getInstance()
    val hour = mcurrentTime!!.get(Calendar.HOUR_OF_DAY)
    val minute = mcurrentTime!!.get(Calendar.MINUTE)
    var sharedPreferences: SharedPreferences? = null
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val eventType = event.eventType
        when (eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED, AccessibilityEvent.TYPE_WINDOWS_CHANGED, AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                val parentNodeInfo = event.source ?: return
                val packageName = event.packageName.toString()
                var browserConfig: SupportedBrowserConfig? = null
                for (supportedConfig in supportedBrowsers) {
                    if (supportedConfig.packageName == packageName) {
                        browserConfig = supportedConfig
                    }
                }
                //this is not supported browser, so exit
                if (browserConfig == null) {
                    return
                }
                val capturedUrl = captureUrl(parentNodeInfo, browserConfig)
                parentNodeInfo.recycle()
                if (capturedUrl == null) {
                    return
                }
                sharedPreferences = getSharedPreferences("Time", Context.MODE_PRIVATE)
                whitelist = sharedPreferences!!.getBoolean("whitelist", true)
                time_from = sharedPreferences!!.getString("from", " ")
                time_to = sharedPreferences!!.getString("to", " ")
               //currentTime = "$hour:$minute".toInt()


                analyzeCapturedUrl(capturedUrl, browserConfig.packageName)
            }
        }
    }

    private fun analyzeCapturedUrl(capturedUrl: String, browserPackage: String) {
        val redirectUrl = " "

    if (whitelist == true) {

        if (!blocked_website_list.contains(capturedUrl) && capturedUrl != browserPackage && capturedUrl != "Search or type web address") {
            //block the website
            performRedirect(redirectUrl, browserPackage)
        }

    } else {
        if (whitelist == false) {
            if (blocked_website_list.contains(capturedUrl) && capturedUrl != browserPackage && capturedUrl != "Search or type web address") {
                //block the website
                performRedirect(redirectUrl, browserPackage)
            }
        }
    }
//        if (blocked_website_list.contains(capturedUrl)==true ) {
//            performRedirect(redirectUrl, browserPackage)
//        }


    }

    private fun performRedirect(redirectUrl: String, browserPackage: String) {
        try {
            val intent = Intent(this, Access_Denied::class.java)
            intent.setPackage(packageName)
            intent.putExtra(BuildConfig.APPLICATION_ID, packageName)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // the expected browser is not installed
            val i = Intent(this, Access_Denied::class.java)
            startActivity(i)
        }
    }

    private class SupportedBrowserConfig(var packageName: String, var addressBarId: String)

    private fun getChild(info: AccessibilityNodeInfo) {
        val i = info.childCount
        for (p in 0 until i) {
            val n = info.getChild(p)
            if (n != null) {
                val strres = n.viewIdResourceName
                if (n.text != null) {
                    val txt = n.text.toString()
                    Log.d("Track", "$strres  :  $txt")
                }
                getChild(n)
            }
        }
    }

    private fun captureUrl(info: AccessibilityNodeInfo, config: SupportedBrowserConfig): String? {

        //getChild(info);
        val nodes = info.findAccessibilityNodeInfosByViewId(config.addressBarId)
        if (nodes == null || nodes.size <= 0) {
            return null
        }
        val addressBarNodeInfo = nodes[0]
        var url: String? = null
        if (addressBarNodeInfo.text != null) {
            url = addressBarNodeInfo.text.toString()
        }
        addressBarNodeInfo.recycle()
        return url
    }

    override fun onInterrupt() {}
    public override fun onServiceConnected() {

    }

    companion object {
        /** @return a list of supported browser configs
         * This list could be instead obtained from remote server to support future browser updates without updating an app
         */
        private val supportedBrowsers: List<SupportedBrowserConfig>
            get() {
                val browsers: MutableList<SupportedBrowserConfig> = ArrayList()
                browsers.add(
                    SupportedBrowserConfig(
                        "com.android.chrome",
                        "com.android.chrome:id/url_bar"
                    )
                )
                return browsers
            }
    }
}