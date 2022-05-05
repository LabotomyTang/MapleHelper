package com.bytedance.jstu.demo.growthhelpermenu

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bytedance.jstu.demo.R
import com.bytedance.jstu.demo.growthhelpermenu.growthhelpereasy.GrowthHelperEasyActivity

class GrowthHelperMenuActivity: AppCompatActivity() {

    private val btnHelperEasy by lazy {
        findViewById<AppCompatButton>(R.id.growthhelpermenu_btn_easy)
    }

    private val btnHelperComplete by lazy {
        findViewById<AppCompatButton>(R.id.growthhelpermenu_btn_complete)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_growthhelpermenu)

        btnHelperEasy.setOnClickListener(View.OnClickListener {
            startActivity(Intent().apply {
                setClass(this@GrowthHelperMenuActivity, GrowthHelperEasyActivity::class.java)
            })
        })

    }
}