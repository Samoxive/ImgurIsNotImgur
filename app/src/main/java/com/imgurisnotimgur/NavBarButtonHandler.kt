package com.imgurisnotimgur

import android.app.Activity
import android.content.Intent
import android.view.View

class NavBarButtonHandler(private val activity: Activity, private val openActivity: Class<*>) : View.OnClickListener {
    override fun onClick(v: View?) {
        val intent = Intent(activity, openActivity)
        activity.startActivity(intent)
        activity.finish()
    }
}