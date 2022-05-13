package com.bytedance.jstu.demo.growthhelpermenu.growthhelpereasy

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.jstu.demo.R
import java.lang.Exception

class GrowthHelperEasyIgnoreActivity: AppCompatActivity() {

    var ignore = 0f

    var redefined = false

    var boss = 0f
    var ignoreOrigin = 0f
    var ignoreNew = 0f

    private val imgBtnTutor by lazy {
        findViewById<ImageButton>(R.id.growthhelpereasyignore_imgBtn_tutor)
    }

    private val etBoss by lazy {
        findViewById<EditText>(R.id.growthhelpereasyignore_et_boss)
    }

    private val checkBoxRedefine by lazy {
        findViewById<CheckBox>(R.id.growthhelpereasyignore_checkbox_redefine)
    }

    private val etIgnoreTotal by lazy {
        findViewById<EditText>(R.id.growthhelpereasyignore_et_ignoretotal)
    }

    private val etIgnoreOrigin by lazy {
        findViewById<EditText>(R.id.growthhelpereasyignore_et_ignoreorigin)
    }

    private val etIgnoreNew by lazy {
        findViewById<EditText>(R.id.growthhelpereasyignore_et_ignorenew)
    }

    private val txtResult by lazy {
        findViewById<TextView>(R.id.growthhelpereasyignore_txt_result)
    }

    private val btnUpdate by lazy {
        findViewById<Button>(R.id.growthhelpereasyignore_btn_update)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_growthhelpereasyignore)

        etBoss.setText("300")

        var bundle = intent.extras
        if (bundle != null) {
            var temp: String? = bundle.getString("ignore")
            if (temp != null) {
                try {
                    ignore = temp.toFloat()
                } catch (e: Exception) {
                    ignore = 0f
                }
            }
            etIgnoreTotal.setText(ignore.toString())
        }

        imgBtnTutor.setOnClickListener(View.OnClickListener {
            AlertDialog.Builder(this)
                .setTitle("说明")
                .setMessage("        该界面用于计算面对特定防御值的 BOSS 时，单条无视变化对输出的收益百分比\n" +
                        "        你需要设置单条无视变化前和变化后的数值以及 BOSS 的防御值。\n" +
                        "        例如，你的联盟无视从 30 % 提升到 40 %，那么将变化前设置为 30，变化后设置为 40。\n" +
                        "        再例如，你可以将总无视看作单条无视，输入总无视变化前和变化后的数值，即可计算该变化带来的总收益。\n" +
                        "        你也可以打开”设为基础值“，使系统用你设定的”无视原值“取代上级页面提供的无视值作为基础无视。\n" +
                        "        无视对最终伤害的影响与其他部分独立，你可以将无视的收益百分比乘到总收益上")
                .setNegativeButton("关闭") { _: DialogInterface, _: Int ->

                }
                .show()
        })

        checkBoxRedefine.setOnClickListener(View.OnClickListener {
            redefined = checkBoxRedefine.isChecked
            etIgnoreTotal.isEnabled = redefined
            if (!redefined) {
                etIgnoreTotal.setText(ignore.toString())
            }
        })

        setEtFloatMethod(etIgnoreTotal)
        setEtFloatMethod(etIgnoreOrigin)
        setEtFloatMethod(etIgnoreNew)

        setEtEnd(etIgnoreTotal, etIgnoreOrigin)
        setEtEnd(etBoss, etIgnoreOrigin)
        setEtEnd(etIgnoreOrigin, etIgnoreNew)

        btnUpdate.setOnClickListener(View.OnClickListener {
            update()
        })
    }

    private fun setEtFloatMethod(et: EditText) {
        et.setOnFocusChangeListener(View.OnFocusChangeListener { view, b ->
            if (b) return@OnFocusChangeListener
            try {
                et.text.toString().toFloat()
            } catch(e: Exception) {
                et.setText("0")
            }
        })

        et.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    var corretText = ""
                    var isFloat = false
                    var floatCount = 0
                    for (i in (0 until p0.toString().length)) {
                        if (p0.toString()[i] == '.') isFloat = true
                        if (isFloat) floatCount += 1
                        if (floatCount > 3) {
                            Toast.makeText(et.context, "小数位不能超过 2", Toast.LENGTH_SHORT).show()
                            et.setText(corretText)
                            et.setSelection(et.text.length)
                            break
                        }
                        corretText += p0.toString()[i]
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            }
        )
    }

    private fun setEtEnd(etCurr: EditText, etNext: EditText?) {
        etCurr.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i != EditorInfo.IME_ACTION_DONE) return@OnEditorActionListener false
            if (etNext != null && etNext.text.isEmpty()) {
                etCurr.clearFocus()
                etNext.requestFocus()
            } else {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                etCurr.clearFocus()
            }
            return@OnEditorActionListener true
        })
    }

    private fun update() {

        etBoss.clearFocus()
        etIgnoreOrigin.clearFocus()
        etIgnoreNew.clearFocus()

        boss = etBoss.text.toString().toFloat()
        ignoreOrigin = etIgnoreOrigin.text.toString().toFloat()
        ignoreNew = etIgnoreNew.text.toString().toFloat()

        if (redefined) {
            var ignoreTotalNew = 0f
            try {
                ignoreTotalNew = etIgnoreTotal.text.toString().toFloat()
            } catch (e: Exception) {

            }
            var originDmg: Float = 100 - boss * (100 - ignoreTotalNew) / 100
            var totalIgnoreNew: Float = 100 - (100 - ignoreTotalNew) / (100 - ignoreOrigin) * (100 - ignoreNew)
            var newDmg: Float = 100 - boss * (100 - totalIgnoreNew) / 100
            if (originDmg <= 0f) {
                if (newDmg <= 0f) {
                    txtResult.text = "无法破防"
                    return
                }
                txtResult.text = "破防"
                return
            }
            var percentage: Float = (newDmg / originDmg - 1) * 100
            txtResult.text = "%.2f".format(percentage)
        } else {
            var originDmg: Float = 100 - boss * (100 - ignore) / 100
            var totalIgnoreNew: Float = 100 - (100 - ignore) / (100 - ignoreOrigin) * (100 - ignoreNew)
            var newDmg: Float = 100 - boss * (100 - totalIgnoreNew) / 100
            if (originDmg <= 0f) {
                if (newDmg <= 0f) {
                    txtResult.text = "无法破防"
                    return
                }
                txtResult.text = "破防"
                return
            }
            var percentage: Float = (newDmg / originDmg - 1) * 100
            txtResult.text = "%.2f".format(percentage)
        }
    }

}