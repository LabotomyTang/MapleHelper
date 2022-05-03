package com.bytedance.jstu.demo.minigames.fingerguessing

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewDebug
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bytedance.jstu.demo.R
import org.w3c.dom.Text
import android.widget.Button as Button1

class FingerGuessing : AppCompatActivity() {
    var stone: Int = 0
    var scissor: Int = 0
    var cloth: Int = 0

    var win: Int = 0
    var lose: Int = 0
    var draw: Int = 0

    var floor: Int = 1
    var score: Int = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fingerguessing)

        setTrigger()

    }

    // 用于设置“石头、剪刀、布”三个按钮的功能
    fun setTrigger() {
        findViewById<Button1>(R.id.fingerguessing_btn_stone).setOnClickListener(View.OnClickListener {
            if (stone == 3 && scissor == 3 && cloth == 3) return@OnClickListener

            var panda = getPanda()
            val result = TextView(this)
            result.setTextSize(20F)

            if (panda == 0) {
                result.setText("你出了石头，熊猫出了石头，平局")
                draw += 1
            } else if (panda == 1) {
                result.setText("你出了石头，熊猫出了剪刀，你赢了")
                win += 1
                if (floor < 5)
                    floor += 1
            } else {
                result.setText("你出了石头，熊猫出了布，你输了")
                lose += 1
                if (floor > 1)
                    floor -= 1
            }

            findViewById<LinearLayout>(R.id.fingerguessing_linearlayout_result).addView(result)
            updateGoal()

            if (stone == 3 && scissor == 3 && cloth == 3) {
                val tmpBtn = Button1(this)
                tmpBtn.setText("重置")
                tmpBtn.setTextSize(20F)
                tmpBtn.setOnClickListener(View.OnClickListener {
                    restart()
                })
                findViewById<LinearLayout>(R.id.fingerguessing_linearlayout_result).addView(tmpBtn)
            }

        })
        findViewById<Button1>(R.id.fingerguessing_btn_scissor).setOnClickListener(View.OnClickListener {
            if (stone == 3 && scissor == 3 && cloth == 3) return@OnClickListener

            var panda = getPanda()
            val result = TextView(this)
            result.setTextSize(20F)

            if (panda == 0) {
                result.setText("你出了剪刀，熊猫出了石头，你输了")
                lose += 1
                if (floor > 1)
                    floor -= 1
            } else if (panda == 1) {
                result.setText("你出了剪刀，熊猫出了剪刀，平局")
                draw += 1
            } else {
                result.setText("你出了剪刀，熊猫出了布，你赢了")
                win += 1
                if (floor < 5)
                    floor += 1
            }

            findViewById<LinearLayout>(R.id.fingerguessing_linearlayout_result).addView(result)
            updateGoal()

            if (stone == 3 && scissor == 3 && cloth == 3) {
                val tmpBtn = Button1(this)
                tmpBtn.setText("重置")
                tmpBtn.setTextSize(20F)
                tmpBtn.setOnClickListener(View.OnClickListener {
                    restart()
                })
                findViewById<LinearLayout>(R.id.fingerguessing_linearlayout_result).addView(tmpBtn)
            }

        })
        findViewById<Button1>(R.id.fingerguessing_btn_cloth).setOnClickListener(View.OnClickListener {
            if (stone == 3 && scissor == 3 && cloth == 3) return@OnClickListener

            var panda = getPanda()
            val result = TextView(this)
            result.setTextSize(20F)

            if (panda == 0) {
                result.setText("你出了布，熊猫出了石头，你赢了")
                win += 1
                if (floor < 5)
                    floor += 1
            } else if (panda == 1) {
                result.setText("你出了布，熊猫出了剪刀，你输了")
                lose += 1
                if (floor > 1)
                    floor -= 1
            } else {
                result.setText("你出了布，熊猫出了布，平局")
                draw += 1
            }

            findViewById<LinearLayout>(R.id.fingerguessing_linearlayout_result).addView(result)
            updateGoal()

            if (stone == 3 && scissor == 3 && cloth == 3) {
                val tmpBtn = Button1(this)
                tmpBtn.setText("重置")
                tmpBtn.setTextSize(20F)
                tmpBtn.setOnClickListener(View.OnClickListener {
                    restart()
                })
                findViewById<LinearLayout>(R.id.fingerguessing_linearlayout_result).addView(tmpBtn)
            }

        })
        findViewById<Button1>(R.id.fingerguessing_btn_rule).setOnClickListener(View.OnClickListener {

            AlertDialog.Builder(this)
                .setTitle("猜拳规则")
                .setMessage("猜拳小游戏是对冒险岛活动小游戏的模拟\n\n" +
                        "玩家与熊猫进行 9 次猜拳游戏\n" +
                        "熊猫会随机出石头、剪刀、布，但每种最多只出 3 次\n\n" +
                        "游戏中有 5 层等级，层数越高分数越高\n" +
                        "游戏开始时玩家处于第 1 层，游戏中胜负会使玩家所处的层数升高或降低 1 层，但不超过上下限")
                .setNegativeButton("关闭") { _: DialogInterface, _: Int ->

                }
                .show()
        })
        findViewById<Button1>(R.id.fingerguessing_btn_restart).setOnClickListener(View.OnClickListener {
            restart()
        })
    }

    // 更新分数
    fun updateGoal() {
        findViewById<TextView>(R.id.fingerguessing_txt_win).setText("胜：${win}")
        findViewById<TextView>(R.id.fingerguessing_txt_lose).setText("负：${lose}")
        findViewById<TextView>(R.id.fingerguessing_txt_draw).setText("平：${draw}")
        findViewById<TextView>(R.id.fingerguessing_txt_floor).setText("层数：${floor}")
        if (floor == 1) score = 100
        else if (floor == 2) score = 200
        else if (floor == 3) score = 500
        else if (floor == 4) score = 1000
        else score = 2000
        findViewById<TextView>(R.id.fingerguessing_txt_score).setText("得分：${score}")
    }


    // 获取熊猫的出拳
    fun getPanda(): Int {
        var panda: Int

        panda = (0..2).random()
        if (stone == 3) panda = (1..2).random()
        if (scissor == 3) panda = (0..1).random() * 2
        if (cloth == 3) panda = (0..1).random()
        if (stone == 3 && scissor == 3) panda = 2
        if (stone == 3 && cloth == 3) panda = 1
        if (scissor == 3 && cloth == 3) panda = 0

        if (panda == 0) stone += 1
        if (panda == 1) scissor += 1
        if (panda == 2) cloth += 1

        return panda
    }

    // 重置
    fun restart() {
        findViewById<LinearLayout>(R.id.fingerguessing_linearlayout_result).removeAllViews()
        stone = 0
        scissor = 0
        cloth = 0
        win = 0
        lose = 0
        draw = 0
        floor = 1
        score = 100
        updateGoal()
    }


    // 获取战况LinearLayout

}