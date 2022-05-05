package com.bytedance.jstu.demo.growthhelpermenu.growthhelpereasy

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.jstu.demo.R
import java.io.*
import kotlin.system.exitProcess

class GrowthHelperEasyActivity: AppCompatActivity() {

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

        load(true)

    }

    override fun finish() {
        var currData = getCurrData()
        if (currData != getSaveFileData()) {
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



    private fun setEtAutoRemoveHint(et: EditText) {
        et.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) et.hint = ""
        }
    }


    private fun setEtFloatMethod(et: EditText) {
        et.addTextChangedListener(
            object: TextWatcher {
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
            .setNegativeButton("取消") { _: DialogInterface, _: Int ->}
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
        if (!isInit && data.isEmpty()) Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show()
        dataToEt(data)
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
        var index= 0
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