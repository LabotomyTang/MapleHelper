package com.bytedance.jstu.demo.growthhelpermenu.growthhelpereasy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.jstu.demo.R

class GrowthHelperEasyResultActivity: AppCompatActivity() {
    var data: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_growthhelpereasyresult)


        var bundle = intent.extras
        data = bundle?.getString("data")

    }
}