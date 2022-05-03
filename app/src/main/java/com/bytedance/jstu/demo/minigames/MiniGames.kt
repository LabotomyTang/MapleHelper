package com.bytedance.jstu.demo.minigames

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.jstu.demo.R
import com.bytedance.jstu.demo.minigames.GuessNumber.GuessNumber
import com.bytedance.jstu.demo.minigames.fingerguessing.FingerGuessing

class MiniGames : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minigames)


        findViewById<Button>(R.id.minigames_btn_fingerguessing).setOnClickListener(View.OnClickListener {
            startActivity(Intent().apply {
                setClass(this@MiniGames, FingerGuessing::class.java)
            })
        })

        findViewById<Button>(R.id.minigames_btn_guessnumber).setOnClickListener(View.OnClickListener {
            startActivity(Intent().apply{
                setClass(this@MiniGames, GuessNumber::class.java)
            })
        })

    }


}