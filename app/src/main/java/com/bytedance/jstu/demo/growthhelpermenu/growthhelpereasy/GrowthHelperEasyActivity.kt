package com.bytedance.jstu.demo.growthhelpermenu.growthhelpereasy

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.jstu.demo.R
import java.io.*
import kotlin.system.exitProcess

class GrowthHelperEasyActivity : AppCompatActivity() {

    private val defaultSaveFileName: String = "GrowthHelperEasyDefaultSaveFile"

    private val etMainAttr: EditText by lazy {
        findViewById(R.id.growthhelpereasy_et_mainattr)
    }

    private val etSecondAttr: EditText by lazy {
        findViewById(R.id.growthhelpereasy_et_secondattr)
    }

    private val etExtraMainAttr: EditText by lazy {
        findViewById(R.id.growthhelpereasy_et_extramainattr)
    }

    private val etCoef: EditText by lazy {
        findViewById(R.id.growthhelpereasy_et_coef)
    }

    private val etAttrAtt: EditText by lazy {
        findViewById(R.id.growthhelpereasy_et_attratt)
    }

    private val etDmg: EditText by lazy {
        findViewById(R.id.growthhelpereasy_et_dmg)
    }

    private val etLastDmg: EditText by lazy {
        findViewById(R.id.growthhelpereasy_et_lastdmg)
    }

    private val etBossDmg: EditText by lazy {
        findViewById(R.id.growthhelpereasy_et_bossdmg)
    }

    private val etIgnore: EditText by lazy {
        findViewById(R.id.growthhelpereasy_et_ignore)
    }

    private val etCriticalRate: EditText by lazy {
        findViewById(R.id.growthhelpereasy_et_criticalrate)
    }

    private val etCriticalDmg: EditText by lazy {
        findViewById(R.id.growthhelpereasy_et_criticaldmg)
    }

    private val checkBoxSwitchPercent: CheckBox by lazy {
        findViewById(R.id.growthhelpereasy_checkbox_switchpercent)
    }

    private val etMainAttrPer: EditText by lazy {
        findViewById(R.id.growthhelpereasy_et_mainattrper)
    }

    private val etAttPer: EditText by lazy {
        findViewById(R.id.growthhelpereasy_et_attper)
    }

    private val btnReset: Button by lazy {
        findViewById(R.id.growthhelpereasy_btn_reset)
    }

    private val btnLoad: Button by lazy {
        findViewById(R.id.growthhelpereasy_btn_load)
    }

    private val btnSave: Button by lazy {
        findViewById(R.id.growthhelpereasy_btn_save)
    }

    private val btnCalculate: Button by lazy {
        findViewById(R.id.growthhelpereasy_btn_calculate)
    }

    private val imgBtnSecondAttr: ImageButton by lazy {
        findViewById(R.id.growthhelpereasy_imgbtn_secondattr)
    }

    private val imgBtnExtraMainAttr: ImageButton by lazy {
        findViewById(R.id.growthhelpereasy_imgbtn_extramainattr)
    }

    private val imgBtnCoef: ImageButton by lazy {
        findViewById(R.id.growthhelpereasy_imgbtn_coef)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_growthhelpereasy)

        setEtAutoRemoveHint(etMainAttr)
        setEtAutoRemoveHint(etSecondAttr)
        setEtAutoRemoveHint(etExtraMainAttr)
        setEtAutoRemoveHint(etCoef)
        setEtAutoRemoveHint(etAttrAtt)
        setEtAutoRemoveHint(etDmg)
        setEtAutoRemoveHint(etLastDmg)
        setEtAutoRemoveHint(etBossDmg)
        setEtAutoRemoveHint(etIgnore)
        setEtAutoRemoveHint(etCriticalRate)
        setEtAutoRemoveHint(etCriticalDmg)
        setEtAutoRemoveHint(etMainAttrPer)
        setEtAutoRemoveHint(etAttPer)

        setEtEnd(etMainAttr, etSecondAttr)
        setEtEnd(etSecondAttr, etExtraMainAttr)
        setEtEnd(etExtraMainAttr, etCoef)
        setEtEnd(etCoef, etAttrAtt)
        setEtEnd(etAttrAtt, etDmg)
        setEtEnd(etDmg, etLastDmg)
        setEtEnd(etLastDmg, etBossDmg)
        setEtEnd(etBossDmg, etIgnore)
        setEtEnd(etIgnore, etCriticalRate)
        setEtEnd(etCriticalRate, etCriticalDmg)
        setEtEnd(etCriticalDmg, null)

        setEtEnd(etMainAttrPer, etAttPer)

        setEtFloatMethod(etCoef)
        setEtFloatMethod(etLastDmg)
        setEtFloatMethod(etIgnore)
        setEtFloatMethod(etCriticalDmg)

        checkBoxSwitchPercent.setOnClickListener(View.OnClickListener {
            etMainAttrPer.isEnabled = checkBoxSwitchPercent.isChecked
            etAttPer.isEnabled = checkBoxSwitchPercent.isChecked
        })

        btnReset.setOnClickListener(View.OnClickListener {
            reset()
        })

        btnSave.setOnClickListener(View.OnClickListener {
            save()
        })

        btnLoad.setOnClickListener(View.OnClickListener {
            load(false)
        })

        btnCalculate.setOnClickListener(View.OnClickListener {
            clearAllFocus()
            var data = getCurrData()
            if (data.isEmpty()) {
                Toast.makeText(this, "参数不全", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else {
                var bundle = Bundle()
                bundle.putString("data", data)
                var intent = Intent()
                intent.setClass(this, GrowthHelperEasyResultActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })


        imgBtnSecondAttr.setOnClickListener(View.OnClickListener {
            AlertDialog.Builder(this)
                .setTitle("说明")
                .setMessage(
                    "你可以前往 冒险岛 BWIKI 查询各个职业的副属性"
                )
                .setNegativeButton("关闭") { _: DialogInterface, _: Int ->

                }
                .setNeutralButton("打开 BWIKI") { _: DialogInterface, _: Int ->
                    var uri: Uri = Uri.parse("https://wiki.biligame.com/maplestory/%E4%B8%BB%E5%89%AF%E5%B1%9E%E6%80%A7%E8%A1%A8")
                    var intent: Intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                .show()
        })

        imgBtnExtraMainAttr.setOnClickListener(View.OnClickListener {
            AlertDialog.Builder(this)
                .setTitle("说明")
                .setMessage(
                    "额外主属性指不受到属性百分比加成的主属性，通常可以直接填入神秘徽章提供的主属性加成。\n" +
                            "你可以前往 冒险岛 BWIKI 查看详情。"
                )
                .setNegativeButton("关闭") { _: DialogInterface, _: Int ->

                }
                .setNeutralButton("打开 BWIKI") { _: DialogInterface, _: Int ->
                    var uri: Uri = Uri.parse("https://wiki.biligame.com/maplestory/%E6%B8%B8%E6%88%8F%E4%BC%A4%E5%AE%B3%E5%85%AC%E5%BC%8F#%E6%9C%80%E7%BB%88%E5%B1%9E%E6%80%A7(Final_Stats)")
                    var intent: Intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                .show()
        })

        imgBtnCoef.setOnClickListener(View.OnClickListener {
            AlertDialog.Builder(this)
                .setTitle("说明")
                .setMessage("武器系数在计算属性攻击力时会与其他数据相乘，你可以去 冒险岛 BWIKI 查询你的职业的武器系数。")
                .setNegativeButton("关闭") { _: DialogInterface, _: Int ->

                }
                .setNeutralButton("打开 BWIKI") { _: DialogInterface, _: Int ->
                    var uri: Uri = Uri.parse("https://wiki.biligame.com/maplestory/%E6%B8%B8%E6%88%8F%E4%BC%A4%E5%AE%B3%E5%85%AC%E5%BC%8F#%E6%AD%A6%E5%99%A8%E7%B3%BB%E6%95%B0(Weapon_Multiplier)")
                    var intent: Intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                .show()
        })

        load(true)

    }

    override fun finish() {
        Log.e("zhTang", "currData: " + getCurrData())
        Log.e("zhTang", "saveFileData: " + getSaveFileData())
        var currData = getCurrData()
        if (currData == getSaveFileData()) {
            super.finish()
            return
        }
        AlertDialog.Builder(this)
            .setTitle("警告")
            .setMessage("当前数据未保存，确定要退出吗？")
            .setNegativeButton("取消") { _: DialogInterface, _: Int ->

            }
            .setNeutralButton("不保存") { _: DialogInterface, _: Int ->
                super.finish()
            }
            .setPositiveButton("保存") { _: DialogInterface, _: Int ->
                save()
            }
            .show()
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

    private fun setEtAutoRemoveHint(et: EditText) {
        et.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) et.hint = ""
            else {
                try {
                    et.text.toString().toFloat()
                } catch (e: Exception) {
                    et.setText("")
                }
            }
        }
    }


    private fun clearAllFocus() {
        etMainAttr.clearFocus()
        etSecondAttr.clearFocus()
        etExtraMainAttr.clearFocus()
        etCoef.clearFocus()
        etAttrAtt.clearFocus()
        etDmg.clearFocus()
        etLastDmg.clearFocus()
        etBossDmg.clearFocus()
        etIgnore.clearFocus()
        etCriticalRate.clearFocus()
        etCriticalDmg.clearFocus()
        etMainAttrPer.clearFocus()
        etAttPer.clearFocus()
    }

    private fun setEtFloatMethod(et: EditText) {
        et.addTextChangedListener(
            object : TextWatcher {
                private var floatCount = 0

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


    private fun getCurrData(): String {
        var data: String = ""

        if (etMainAttr.text.toString().isEmpty()) {
            etMainAttr.hint = "未设置"
            return ""
        } else data += etMainAttr.text.toString() + '\n'

        if (etSecondAttr.text.toString().isEmpty()) {
            etSecondAttr.hint = "未设置"
            return ""
        } else data += etSecondAttr.text.toString() + '\n'

        if (etExtraMainAttr.text.toString().isEmpty()) {
            etExtraMainAttr.hint = "未设置"
            return ""
        } else data += etExtraMainAttr.text.toString() + '\n'

        if (etCoef.text.toString().isEmpty()) {
            etCoef.hint = "未设置"
            return ""
        } else data += etCoef.text.toString() + '\n'

        if (etAttrAtt.text.toString().isEmpty()) {
            etAttrAtt.hint = "未设置"
            return ""
        } else data += etAttrAtt.text.toString() + '\n'

        if (etDmg.text.toString().isEmpty()) {
            etDmg.hint = "未设置"
            return ""
        } else data += etDmg.text.toString() + '\n'

        if (etLastDmg.text.toString().isEmpty()) {
            etLastDmg.hint = "未设置"
            return ""
        } else data += etLastDmg.text.toString() + '\n'

        if (etBossDmg.text.toString().isEmpty()) {
            etBossDmg.hint = "未设置"
            return ""
        } else data += etBossDmg.text.toString() + '\n'

        if (etIgnore.text.toString().isEmpty()) {
            etIgnore.hint = "未设置"
            return ""
        } else data += etIgnore.text.toString() + '\n'

        if (etCriticalRate.text.toString().isEmpty()) {
            etCriticalRate.hint = "未设置"
            return ""
        } else data += etCriticalRate.text.toString() + '\n'

        if (etCriticalDmg.text.toString().isEmpty()) {
            etCriticalDmg.hint = "未设置"
            return ""
        } else data += etCriticalDmg.text.toString() + '\n'

        if (!checkBoxSwitchPercent.isChecked) {
            data += "false" + '\n'
            return data
        } else data += "true" + '\n'

        if (etMainAttrPer.text.toString().isEmpty()) {
            etMainAttrPer.hint = "未设置"
            return ""
        } else data += etMainAttrPer.text.toString() + '\n'

        if (etAttPer.text.toString().isEmpty()) {
            etAttPer.hint = "未设置"
            return ""
        } else data += etAttPer.text.toString() + '\n'

        return data
    }

    private fun reset() {
        etMainAttr.setText("")
        etSecondAttr.setText("")
        etExtraMainAttr.setText("")
        etCoef.setText("")
        etAttrAtt.setText("")
        etDmg.setText("")
        etLastDmg.setText("")
        etBossDmg.setText("")
        etIgnore.setText("")
        etCriticalRate.setText("")
        etCriticalDmg.setText("")
        etMainAttrPer.setText("")
        etAttPer.setText("")

        etMainAttr.hint = ""
        etSecondAttr.hint = ""
        etExtraMainAttr.hint = ""
        etCoef.hint = ""
        etAttrAtt.hint = ""
        etDmg.hint = ""
        etLastDmg.hint = ""
        etBossDmg.hint = ""
        etIgnore.hint = ""
        etCriticalRate.hint = ""
        etCriticalDmg.hint = ""
        etMainAttrPer.hint = ""
        etAttPer.hint = ""
    }


    private fun save() {
        var data = getCurrData()
        if (data.isEmpty()) return
        AlertDialog.Builder(this)
            .setTitle("警告")
            .setMessage("先前可能存在的数据将会被覆盖，要继续吗?")
            .setNegativeButton("取消") { _: DialogInterface, _: Int -> }
            .setPositiveButton("确定") { _: DialogInterface, _: Int ->
                try {
                    val output = openFileOutput(defaultSaveFileName, Context.MODE_PRIVATE)
                    val writer = BufferedWriter(OutputStreamWriter(output))
                    writer.use {
                        it.write(data)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            .show()
    }

    private fun getSaveFileData(): String {
        var data: String = ""
        try {
            val input = openFileInput(defaultSaveFileName)
            val reader = BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    data += it + '\n'
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return data
    }

    private fun load(isInit: Boolean): String {
        var data: String = getSaveFileData()
        if (isInit) {
            if (!data.isEmpty()) dataToEt(data)
        } else {
            if (!data.isEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("警告")
                    .setMessage("将用存档数据覆盖当前数据，要继续吗？")
                    .setNegativeButton("取消") { _: DialogInterface, _: Int ->

                    }
                    .setPositiveButton("确定") { _: DialogInterface, _: Int ->
                        dataToEt(data)
                    }
                    .show()
            } else Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show()
        }
        return data
    }

    fun setSingleEtText(et: EditText, data: String, start: Int): Int {
        var singleData: String = ""
        var i: Int = start
        while (i < data.length) {
            if (data[i] != '\n') singleData += data[i]
            else break
            i += 1
        }
        et.setText(singleData)
        return i + 1
    }

    private fun dataToEt(data: String) {
        var index = 0
        index = setSingleEtText(etMainAttr, data, index)
        index = setSingleEtText(etSecondAttr, data, index)
        index = setSingleEtText(etExtraMainAttr, data, index)
        index = setSingleEtText(etCoef, data, index)
        index = setSingleEtText(etAttrAtt, data, index)
        index = setSingleEtText(etDmg, data, index)
        index = setSingleEtText(etLastDmg, data, index)
        index = setSingleEtText(etBossDmg, data, index)
        index = setSingleEtText(etIgnore, data, index)
        index = setSingleEtText(etCriticalRate, data, index)
        index = setSingleEtText(etCriticalDmg, data, index)
        var isChecked: String = ""
        for (i in (index until data.length)) {
            if (data[i] != '\n') isChecked += data[i]
            else {
                index = i + 1
                break
            }
        }
        if (isChecked == "true") {
            checkBoxSwitchPercent.isChecked = true
            etMainAttrPer.isEnabled = true
            etAttPer.isEnabled = true
            index = setSingleEtText(etMainAttrPer, data, index)
            index = setSingleEtText(etAttPer, data, index)
        } else if (isChecked == "false") {
            checkBoxSwitchPercent.isChecked = false
            etMainAttrPer.isEnabled = false
            etAttPer.isEnabled = false
        } else {
            Log.e("zhTang", "Error occured in function load")
        }
    }

}