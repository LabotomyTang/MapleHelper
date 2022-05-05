package com.bytedance.jstu.demo

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bytedance.jstu.demo.growthhelpermenu.GrowthHelperMenuActivity
import com.bytedance.jstu.demo.minigames.MiniGames
import com.bytedance.jstu.demo.starstrategy.StarStrategy
import java.util.stream.DoubleStream.builder
import java.util.stream.IntStream.builder

class MainActivity : AppCompatActivity() {


    private val btnGrowthHelper: ImageButton by lazy {
        findViewById<ImageButton>(R.id.mainactivity_imgbtn_growthhelper)
    }
    private val btnStarStrategy: ImageButton by lazy {
        findViewById<ImageButton>(R.id.mainactivity_imgBtn_starstrategy)
    }
    private val btnMinigames: ImageButton by lazy {
        findViewById<ImageButton>(R.id.imgBtn_MiniGames)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGrowthHelper.setOnClickListener(View.OnClickListener {
            startActivity(Intent().apply {
                setClass(this@MainActivity, GrowthHelperMenuActivity::class.java)
            })
        })


        btnStarStrategy.setOnClickListener(View.OnClickListener {
            startActivity(Intent().apply {
                setClass(this@MainActivity, StarStrategy::class.java)
            })
        })
        btnMinigames.setOnClickListener(View.OnClickListener {
            startActivity(Intent().apply {
                setClass(this@MainActivity, MiniGames::class.java)
            })
        })

    }


}