package com.bytedance.jstu.demo.minigames.GuessNumber

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewDebug
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import android.widget.LinearLayout.FOCUS_LEFT
import android.widget.LinearLayout.HORIZONTAL
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.marginBottom
import androidx.core.view.marginRight
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.bytedance.jstu.demo.R
import org.intellij.lang.annotations.JdkConstants
import org.w3c.dom.Text

class GuessNumber : AppCompatActivity() {

    private val advisor = GuessNumberAdvisor()

    private val answer = arrayListOf<String>()

    private var guessDigit = 0

    private var rChance = 0;

    private val buttonNum = arrayListOf<Button>()
    lateinit var buttonConfirm: Button
    lateinit var buttonBackspace: Button
    lateinit var buttonRestart: Button
    lateinit var txtLastChance: TextView
    lateinit var txtResult: TextView
    private val txtGuessNum = arrayListOf<TextView>()
    private val historyLinearLayout = arrayListOf<LinearLayout>()

    private val btnAdvisor: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.guessnumber_btn_advisor)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guessnumber)
        setGuideTrigger()

        txtLastChance = findViewById<TextView>(R.id.guessnumber_txt_lastchance)
        txtResult = findViewById<TextView>(R.id.guessnumber_txt_result)

        btnAdvisor.setOnClickListener(View.OnClickListener {
            startActivity(Intent().apply {
                setClass(this@GuessNumber, GuessNumberAdvisorActivity::class.java)
            })
        })

        summonAnswer()
        initHistory()
        initKeyBoard()
        initGuessText()
    }

    fun summonAnswer() {
        answer.clear()
        var tempString: String = "${(1..9).random()}"
        answer.add(tempString)
        while (tempString == answer[0]) {
            tempString = "${(1..9).random()}"
        }
        answer.add(tempString)
        while (tempString == answer[0] || tempString == answer[1]) {
            tempString = "${(1..9).random()}"
        }
        answer.add(tempString)
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun initKeyBoard() {
        buttonNum.add(findViewById<Button>(R.id.guessnumber_btn_1))
        buttonNum.add(findViewById<Button>(R.id.guessnumber_btn_2))
        buttonNum.add(findViewById<Button>(R.id.guessnumber_btn_3))
        buttonNum.add(findViewById<Button>(R.id.guessnumber_btn_4))
        buttonNum.add(findViewById<Button>(R.id.guessnumber_btn_5))
        buttonNum.add(findViewById<Button>(R.id.guessnumber_btn_6))
        buttonNum.add(findViewById<Button>(R.id.guessnumber_btn_7))
        buttonNum.add(findViewById<Button>(R.id.guessnumber_btn_8))
        buttonNum.add(findViewById<Button>(R.id.guessnumber_btn_9))
        for (i in (0..8)) {
            buttonNum[i].setOnClickListener(View.OnClickListener {
                input(i + 1)
            })
        }
        buttonBackspace = findViewById<Button>(R.id.guessnumber_btn_backspace)
        buttonBackspace.setOnClickListener(View.OnClickListener {
            backspace()
        })
        buttonConfirm = findViewById<Button>(R.id.guessnumber_btn_confirm)
        buttonConfirm.setOnClickListener(View.OnClickListener {
            confirm()
        })

        buttonRestart = findViewById<Button>(R.id.guessnumber_btn_restart)
        buttonRestart.setOnClickListener(View.OnClickListener {
            restart()
        })
    }

    fun initGuessText() {
        txtGuessNum.add(findViewById<TextView>(R.id.guessnumber_txt_guess1))
        txtGuessNum.add(findViewById<TextView>(R.id.guessnumber_txt_guess2))
        txtGuessNum.add(findViewById<TextView>(R.id.guessnumber_txt_guess3))
    }

    fun initHistory() {

        historyLinearLayout.add(findViewById<LinearLayout>(R.id.guessnumber_linearlayout_history1))
        historyLinearLayout.add(findViewById<LinearLayout>(R.id.guessnumber_linearlayout_history2))
        historyLinearLayout.add(findViewById<LinearLayout>(R.id.guessnumber_linearlayout_history3))
        historyLinearLayout.add(findViewById<LinearLayout>(R.id.guessnumber_linearlayout_history4))
        historyLinearLayout.add(findViewById<LinearLayout>(R.id.guessnumber_linearlayout_history5))
        historyLinearLayout.add(findViewById<LinearLayout>(R.id.guessnumber_linearlayout_history6))
        historyLinearLayout.add(findViewById<LinearLayout>(R.id.guessnumber_linearlayout_history7))
        historyLinearLayout.add(findViewById<LinearLayout>(R.id.guessnumber_linearlayout_history8))
        historyLinearLayout.add(findViewById<LinearLayout>(R.id.guessnumber_linearlayout_history9))
        historyLinearLayout.add(findViewById<LinearLayout>(R.id.guessnumber_linearlayout_history10))

    }

    fun setGuideTrigger() {
        findViewById<Button>(R.id.guessnumber_btn_rule).setOnClickListener(View.OnClickListener {
            AlertDialog.Builder(this)
                .setTitle("猜密码规则")
                .setMessage(
                    "宝物箱的密码是 3 位从 1 到 9 中随机选取的不重复的有顺序的数字\n"
                            + "玩家有 7 次机会猜密码，每次猜密码后，系统会告知有几位数字位置正确，有几位数字正确但位置不正确\n"
                            + "例如：密码为 1 2 3，玩家猜测 1 3 4，则系统告知有 1 位数字位置正确（数字 \"1\"），有 1 位数字正确但位置不正确（数字 \"3\"）"
                )
                .setNegativeButton("关闭") { _: DialogInterface, _: Int ->

                }
                .show()
        })
    }

    fun input(num: Int) {
        if (guessDigit < 3) {
            txtGuessNum[guessDigit].setText("$num")
            guessDigit += 1
            buttonNum[num - 1].isClickable = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun confirm() {
        if (guessDigit != 3) return
        var result = arrayListOf<Int>()
        var playerGuess = arrayListOf<String>()
        for (i in (0..2)) {
            playerGuess.add(txtGuessNum[i].text.toString())
        }
        clearPlayerGuess()
        guessDigit = 0
        compare(playerGuess, answer, result);

        addHistory(playerGuess, result[0], result[1])

        rChance += 1
        txtLastChance.setText("${10 - rChance}")

        for (i in (0..8)) {
            buttonNum[i].isClickable = true
        }

        if (result[0] == 3) {
            end()
            txtResult.setText("You Win!")
            return
        }

        if (rChance == 10) {
            txtResult.setText("Chance Out!")
            end()
        }

    }

    @SuppressLint("NewApi")
    fun end() {
        for (i in (0..8)) {
            buttonNum[i].isClickable = false
        }
        buttonRestart.setBackgroundColor(getColor(R.color.purple_500))
    }


    fun toInt(string: String): Int {

        if (string == "0") return 0
        else if (string == "1") return 1
        else if (string == "2") return 2
        else if (string == "3") return 3
        else if (string == "4") return 4
        else if (string == "5") return 5
        else if (string == "6") return 6
        else if (string == "7") return 7
        else if (string == "8") return 8
        else if (string == "9") return 9
        else return -1;
    }

    fun compare(code1: ArrayList<String>, code2: ArrayList<String>, result: ArrayList<Int>) {
        var code1Int = arrayListOf(toInt(code1[0]), toInt(code1[1]), toInt(code1[2]))
        var code2Int = arrayListOf(toInt(code2[0]), toInt(code2[1]), toInt(code2[2]))
        result.clear()
        result.add(0)
        result.add(0)
        for (i in (0..2)) {
            for (j in (0..2)) {
                if (code1[i] != code2[j]) continue
                if (i == j) result[0] += 1
                else result[1] += 1
            }
        }
    }

    fun backspace() {
        if (guessDigit > 0) {
            var num = toInt(txtGuessNum[guessDigit - 1].text.toString())
            buttonNum[num - 1].isClickable = true
            txtGuessNum[guessDigit - 1].setText("")
            guessDigit -= 1
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun restart() {
        guessDigit = 0
        clearPlayerGuess()
        clearHistory()
        for (i in (0..8)) {
            buttonNum[i].isClickable = true
        }
        rChance = 0
        txtLastChance.setText("${10 - rChance}")
        txtResult.setText("")
        buttonRestart.setBackgroundColor(getColor(R.color.purple_200))
        summonAnswer()
    }

    fun clearPlayerGuess() {
        for (i in (0..2)) {
            txtGuessNum[i].setText("")
        }
    }

    fun clearHistory() {
        for (i in (0..9)) {
            historyLinearLayout[i].removeAllViews()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun addHistory(playerGuess: ArrayList<String>, a: Int, b: Int) {

        val view = LayoutInflater.from(this)
            .inflate(R.layout.guessnumber_history, historyLinearLayout[rChance])
        view.findViewById<TextView>(R.id.guessnumber_history_playerguess).text =
            "${playerGuess[0]}  ${playerGuess[1]}  ${playerGuess[2]}      "
        view.findViewById<TextView>(R.id.guessnumber_history_a).text = "$a "
        view.findViewById<TextView>(R.id.guessnumber_history_b).text = " $b "


    }

}