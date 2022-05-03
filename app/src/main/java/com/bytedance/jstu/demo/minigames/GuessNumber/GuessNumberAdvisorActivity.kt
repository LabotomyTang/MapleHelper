package com.bytedance.jstu.demo.minigames.GuessNumber

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.media.Image
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.graphics.drawable.DrawableCompat.inflate
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.jstu.demo.R

class GuessNumberAdvisorActivity : AppCompatActivity() {
    var adviceList = ArrayList<Array<String>>()
    val algorithm = GuessNumberAdvisor()

    val viewHolderList = ArrayList<MyViewHolder>()

    private val btnNextStep: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.guessnumberadvisor_btn_nextstep)
    }

    private val btnRestart: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.guessnumberadvisor_btn_restart)
    }

    private val txtResult: TextView by lazy {
        findViewById<TextView>(R.id.guessnumberadvisor_txt_result)
    }

    private val linearLayoutScrollChild: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.guessnumberadvisor_linearlayout_scrollChild)
    }

    private val scrollViewMain: ScrollView by lazy {
        findViewById<ScrollView>(R.id.guessnumberadvisor_scrollview_main)
    }

    private val btnHelper: ImageButton by lazy {
        findViewById<ImageButton>(R.id.guessnumberadvisor_btn_helper)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guessnumberadvisor)

        scrollViewMain.viewTreeObserver.addOnGlobalLayoutListener {
            scrollViewMain.fullScroll(ScrollView.FOCUS_DOWN)
        }

        clear()
        getFirstAdvice()

        btnHelper.setOnClickListener(View.OnClickListener {
            AlertDialog.Builder(this)
                .setTitle("猜数帮手说明")
                .setMessage("        猜数帮手可以帮你猜数字，也相当于可以猜你想的数字\n" +
                        "        它每次会给出一个建议，你可以遵循它的建议，直接将结果反馈给它\n" +
                        "        你也可以不遵循它的建议，使用自定义的猜测，同样也需要反馈结果\n" +
                        "        猜数帮手能够检测出矛盾，如果矛盾，请检查你给出的反馈是否有误")
                .setNegativeButton("关闭") { _: DialogInterface, _: Int ->

                }
                .show()
        })

        btnRestart.setOnClickListener(View.OnClickListener {
            clear()

            btnNextStep.isClickable = true

            getFirstAdvice()
        })

        btnNextStep.setOnClickListener(View.OnClickListener {
            var result = arrayOf<Int>(0, 0)
            getResult(result)
            if (result[0] < 0 || result[1] < 0 || result[0] + result[1] > 3) {
                Toast.makeText(this, "“结果”输入有误", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            var custom = arrayOf<Int>(0, 0, 0)
            if (getCustom(custom)) {
                if (custom[0] == custom[1] || custom[0] == custom[2] || custom[1] == custom[2]) {
                    Toast.makeText(this, "“自定义”输入有误", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                var advice = arrayOf<String>("", "", "")
                var isFinished = algorithm.getResponse(
                    GuessNumberAdvisor.Answer(
                        algorithm.answerLength,
                        custom
                    ), result, advice
                )
                if (isFinished == -1) {
                    txtResult.text = "矛盾"
                    btnNextStep.isClickable = false
                    return@OnClickListener
                }
                if (isFinished == 1) {
                    txtResult.text = "猜中：${advice[0]} ${advice[1]} ${advice[2]}"
                    btnNextStep.isClickable = false
                    return@OnClickListener
                }
                showNextAdvice(advice)
            } else {
                var advice = arrayOf<String>("", "", "")
                var isFinished = algorithm.getResponse(
                    GuessNumberAdvisor.Answer(
                        algorithm.answerLength,
                        algorithm.lastAdvice
                    ), result, advice
                )
                if (isFinished == -1) {
                    txtResult.text = "矛盾"
                    btnNextStep.isClickable = false
                    return@OnClickListener
                }
                if (isFinished == 1) {
                    txtResult.text = "猜中：${advice[0]} ${advice[1]} ${advice[2]}"
                    btnNextStep.isClickable = false
                    return@OnClickListener
                }
                showNextAdvice(advice)
            }
        })
    }

    fun toInt(string: String): Int {
        when (string) {
            "0" -> return -1
            "1" -> return 0
            "2" -> return 1
            "3" -> return 2
            "4" -> return 3
            "5" -> return 4
            "6" -> return 5
            "7" -> return 6
            "8" -> return 7
            "9" -> return 8
        }
        return -2
    }

    fun getResult(result: Array<Int>) {
        var resultString = arrayOf<String>("", "")
        viewHolderList.last().getResult(resultString)
        result[0] = toInt(resultString[0]) + 1
        result[1] = toInt(resultString[1]) + 1
    }

    fun getCustom(custom: Array<Int>): Boolean {
        var customString = arrayOf<String>("", "", "")
        var isChecked = viewHolderList.last().getCustom(customString)
        custom[0] = toInt(customString[0])
        custom[1] = toInt(customString[1])
        custom[2] = toInt(customString[2])
        return isChecked
    }

    fun clear() {
        linearLayoutScrollChild.removeAllViews()
        viewHolderList.clear()
        txtResult.text = ""
    }

    fun getFirstAdvice() {
        algorithm.restart()

        var advice = Array<String>(algorithm.answerLength) { i -> "" }
        algorithm.firstAdvice(advice)
        adviceList.add(advice)

        var view =
            LayoutInflater.from(this).inflate(R.layout.guessnumberadvisor_step_item, null, false)
        if (viewHolderList.size > 0) viewHolderList.last().ban()
        viewHolderList.add(MyViewHolder(view))
        viewHolderList.last().update("第 ${algorithm.times} 次猜测", advice[0], advice[1], advice[2])
        linearLayoutScrollChild.addView(view)
    }

    fun showNextAdvice(advice: Array<String>) {
        var view =
            LayoutInflater.from(this).inflate(R.layout.guessnumberadvisor_step_item, null, false)
        if (viewHolderList.size > 0) viewHolderList.last().ban()
        viewHolderList.add(MyViewHolder(view))
        viewHolderList.last().update("第 ${algorithm.times} 次猜测", advice[0], advice[1], advice[2])
        linearLayoutScrollChild.addView(view)
    }

    class MyViewHolder(view: View) {

        val view = view

        private val txtTitle = view.findViewById<TextView>(R.id.guessnumberadvisor_txt_title)
        private val txtAdvice1 = view.findViewById<TextView>(R.id.guessnumberadvisor_txt_advice1)
        private val txtAdvice2 = view.findViewById<TextView>(R.id.guessnumberadvisor_txt_advice2)
        private val txtAdvice3 = view.findViewById<TextView>(R.id.guessnumberadvisor_txt_advice3)

        private val checkBoxCustom =
            view.findViewById<CheckBox>(R.id.guessnumberadvisor_checkbox_custom)
        private val etCustom1 = view.findViewById<EditText>(R.id.guessnumberadvisor_et_custom1)
        private val etCustom2 = view.findViewById<EditText>(R.id.guessnumberadvisor_et_custom2)
        private val etCustom3 = view.findViewById<EditText>(R.id.guessnumberadvisor_et_custom3)
        private val etResultA = view.findViewById<EditText>(R.id.guessnumberadvisor_et_resultA)
        private val etResultB = view.findViewById<EditText>(R.id.guessnumberadvisor_et_resultB)

        init {
            checkBoxCustom.setOnClickListener(View.OnClickListener {
                var isCustom = checkBoxCustom.isChecked
                etCustom1.isEnabled = isCustom
                etCustom2.isEnabled = isCustom
                etCustom3.isEnabled = isCustom
            })
            etCustom1.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {
                        etCustom2.requestFocus()
                    }
                }
            )
            etCustom2.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {
                        etCustom3.requestFocus()
                    }
                }
            )
            etCustom3.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {
                        etResultA.requestFocus()
                    }
                }
            )
            etResultA.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {
                        etResultB.requestFocus()
                    }
                }
            )
        }

        fun update(title: String, advice1: String, advice2: String, advice3: String) {
            txtTitle.text = title
            txtAdvice1.text = advice1
            txtAdvice2.text = advice2
            txtAdvice3.text = advice3
        }

        fun ban() {
            checkBoxCustom.isClickable = false
            etCustom1.isEnabled = false
            etCustom2.isEnabled = false
            etCustom3.isEnabled = false
            etResultA.isEnabled = false
            etResultB.isEnabled = false
        }

        fun getCustom(customString: Array<String>): Boolean {
            customString[0] = etCustom1.text.toString()
            customString[1] = etCustom2.text.toString()
            customString[2] = etCustom3.text.toString()
            return checkBoxCustom.isChecked
        }

        fun getResult(resultString: Array<String>) {
            resultString[0] = etResultA.text.toString()
            resultString[1] = etResultB.text.toString()
        }

    }

}