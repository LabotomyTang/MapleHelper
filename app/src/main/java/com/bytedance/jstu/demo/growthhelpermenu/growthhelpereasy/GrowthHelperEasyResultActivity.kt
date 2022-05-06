package com.bytedance.jstu.demo.growthhelpermenu.growthhelpereasy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bytedance.jstu.demo.R
import com.bytedance.jstu.demo.R.color.gray
import com.bytedance.jstu.demo.R.color.purple_200
import java.lang.Exception

class GrowthHelperEasyResultActivity : AppCompatActivity() {
    var data: String? = ""

    private val includedList = arrayListOf<Boolean>(
        false, false, false, false, false, false, false, false, false
    )

    var mainAttr: Float = 0f
    var secondAttr: Float = 0f
    var extraMainAttr: Float = 0f
    var coef: Float = 0f
    var attrAtt: Float = 0f
    var dmg: Float = 0f
    var lastDmg: Float = 0f
    var bossDmg: Float = 0f
    var ignore: Float = 0f
    var criticalRate: Float = 0f
    var criticalDmg: Float = 0f
    var mainAttrPerAndAttPerEnable: Boolean = false
    var mainAttrPer: Float = 0f
    var attPer: Float = 0f

    var basicMainAttr: Float = 0f
    var basicAtt: Float = 0f

    private val attrList = ArrayList<Float>()

    var extraMainAttrDelta: Float = 0f // 0
    var dmgDelta: Float = 0f // 1
    var bossDmgDelta: Float = 0f // 2
    var criticalRateDelta: Float = 0f // 3
    var criticalDmgDelta: Float = 0f // 4
    var basicMainAttrDelta: Float = 0f // 5
    var mainAttrPerDelta: Float = 0f // 6
    var basicAttDelta: Float = 0f // 7
    var attPerDelta: Float = 0f // 8
    var ignoreDelta: Float = 0f
    var lastDmgDelta: Float = 0f

    private val deltaList = ArrayList<Float>()


    private val txtExtraMainAttr: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_extramainattr)
    }

    private val txtDmg: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_dmg)
    }

    private val txtBossDmg: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_bossdmg)
    }

    private val txtCriticalRate: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_criticalrate)
    }

    private val txtCriticalDmg: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_criticaldmg)
    }

    private val txtBasicMainAttr: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_mainattr)
    }

    private val txtMainAttrPer: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_mainattrper)
    }

    private val txtBasicAtt: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_att)
    }

    private val txtAttPer: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_attper)
    }

    private val txtList = ArrayList<TextView>()


    private val etExtraMainAttr: EditText by lazy {
        findViewById(R.id.growthhelpereasyresult_et_extramainattr)
    }

    private val etDmg: EditText by lazy {
        findViewById(R.id.growthhelpereasyresult_et_dmg)
    }

    private val etBossDmg: EditText by lazy {
        findViewById(R.id.growthhelpereasyresult_et_bossdmg)
    }

    private val etCriticalRate: EditText by lazy {
        findViewById(R.id.growthhelpereasyresult_et_criticalrate)
    }

    private val etCriticalDmg: EditText by lazy {
        findViewById(R.id.growthhelpereasyresult_et_criticaldmg)
    }

    private val etBasicMainAttr: EditText by lazy {
        findViewById(R.id.growthhelpereasyresult_et_mainattr)
    }

    private val etMainAttrPer: EditText by lazy {
        findViewById(R.id.growthhelpereasyresult_et_mainattrper)
    }

    private val etBasicAtt: EditText by lazy {
        findViewById(R.id.growthhelpereasyresult_et_att)
    }

    private val etAttPer: EditText by lazy {
        findViewById(R.id.growthhelpereasyresult_et_attper)
    }

    private val etList = ArrayList<EditText>()


    private val txtExtraMainAttrResult: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_extramainattrresult)
    }

    private val txtDmgResult: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_dmgresult)
    }

    private val txtBossDmgResult: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_bossdmgresult)
    }

    private val txtCriticalRateResult: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_criticalrateresult)
    }

    private val txtCriticalDmgResult: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_criticaldmgresult)
    }

    private val txtBasicMainAttrResult: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_mainattrresult)
    }

    private val txtMainAttrPerResult: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_mainattrperresult)
    }

    private val txtBasicAttResult: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_attresult)
    }

    private val txtAttPerResult: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_attperresult)
    }

    private val txtResultList = ArrayList<TextView>()

    private val txtTotalResult: TextView by lazy {
        findViewById(R.id.growthhelpereasyresult_txt_totalresult)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_growthhelpereasyresult)

        var bundle = intent.extras
        data = bundle?.getString("data")

        receiveData(data)

        if (!mainAttrPerAndAttPerEnable) {
            etBasicMainAttr.isEnabled = false
            etMainAttrPer.isEnabled = false
            etBasicAtt.isEnabled = false
            etAttPer.isEnabled = false

            txtBasicMainAttrResult.text = "无效"
            txtMainAttrPerResult.text = "无效"
            txtBasicAttResult.text = "无效"
            txtAttPerResult.text = "无效"

            txtBasicMainAttr.isClickable = false
            txtMainAttrPer.isClickable = false
            txtBasicAtt.isClickable = false
            txtAttPer.isClickable = false
        }

        findViewById<Button>(R.id.growthhelpereasyresult_btn_ignore).setOnClickListener(View.OnClickListener {
            startActivity(Intent().apply {
                setClass(this@GrowthHelperEasyResultActivity, GrowthHelperEasyIgnoreActivity::class.java)
                var bundle = Bundle()
                bundle.putString("ignore", ignore.toString())
                putExtras(bundle)
            })
        })

        initList()

        for (i in (0 until txtList.size)) {
            setTxtClick(txtList[i], i)
        }

        for (i in (0 until etList.size)) {
            setEtListener(etList[i], i)
        }

        update()

        setEtFloatMethod(etCriticalDmg)
    }

    private fun initList() {
        attrList.add(extraMainAttr)
        attrList.add(dmg)
        attrList.add(bossDmg)
        attrList.add(criticalRate)
        attrList.add(criticalDmg)
        attrList.add(basicMainAttr)
        attrList.add(mainAttrPer)
        attrList.add(basicAtt)
        attrList.add(attPer)

        deltaList.add(extraMainAttrDelta)
        deltaList.add(dmgDelta)
        deltaList.add(bossDmgDelta)
        deltaList.add(criticalRateDelta)
        deltaList.add(criticalDmgDelta)
        deltaList.add(basicMainAttrDelta)
        deltaList.add(mainAttrPerDelta)
        deltaList.add(basicAttDelta)
        deltaList.add(attPerDelta)

        txtList.add(txtExtraMainAttr)
        txtList.add(txtDmg)
        txtList.add(txtBossDmg)
        txtList.add(txtCriticalRate)
        txtList.add(txtCriticalDmg)
        txtList.add(txtBasicMainAttr)
        txtList.add(txtMainAttrPer)
        txtList.add(txtBasicAtt)
        txtList.add(txtAttPer)

        etList.add(etExtraMainAttr)
        etList.add(etDmg)
        etList.add(etBossDmg)
        etList.add(etCriticalRate)
        etList.add(etCriticalDmg)
        etList.add(etBasicMainAttr)
        etList.add(etMainAttrPer)
        etList.add(etBasicAtt)
        etList.add(etAttPer)

        txtResultList.add(txtExtraMainAttrResult)
        txtResultList.add(txtDmgResult)
        txtResultList.add(txtBossDmgResult)
        txtResultList.add(txtCriticalRateResult)
        txtResultList.add(txtCriticalDmgResult)
        txtResultList.add(txtBasicMainAttrResult)
        txtResultList.add(txtMainAttrPerResult)
        txtResultList.add(txtBasicAttResult)
        txtResultList.add(txtAttPerResult)
    }

    private fun receiveData(data: String?) {
        if (data == null || data.isEmpty() || data[0] == '\n') {
            Log.e("zhTang", "Error in receiveData: data is illegal. ")
            return
        }
        var dataList = Array<String>(14) { i -> "" }
        var singleData = ""
        var dataIndex = 0
        var charIndex = 0
        while (charIndex < data.length && dataIndex < dataList.size) {
            singleData = ""
            while (data[charIndex] != '\n') {
                singleData += data[charIndex]
                charIndex += 1
            }
            charIndex += 1
            dataList[dataIndex] = singleData
            dataIndex += 1
            if (singleData == "false") break
        }
        mainAttr = dataList[0].toFloat()
        secondAttr = dataList[1].toFloat()
        extraMainAttr = dataList[2].toFloat()
        coef = dataList[3].toFloat()
        attrAtt = dataList[4].toFloat()
        dmg = dataList[5].toFloat()
        lastDmg = dataList[6].toFloat()
        bossDmg = dataList[7].toFloat()
        ignore = dataList[8].toFloat()
        criticalRate = dataList[9].toFloat()
        criticalDmg = dataList[10].toFloat()
        mainAttrPerAndAttPerEnable = dataList[11].toBoolean()
        if (!mainAttrPerAndAttPerEnable) return
        mainAttrPer = dataList[12].toFloat()
        attPer = dataList[13].toFloat()
        basicMainAttr = (mainAttr - extraMainAttr) / (100 + mainAttrPer) * 100
        basicAtt =
            attrAtt * 100 / coef / (100 + lastDmg) * 100 / (100 + dmg) * 100 / (4 * mainAttr + secondAttr) / (100 + attPer) * 100
    }

    private fun getPercentage(type: Int): Float {
        when (type) {
            0 -> return ((mainAttr + deltaList[0]) * 4 + secondAttr) / (mainAttr * 4 + secondAttr)
            1 -> return (100 + dmg + deltaList[1] + bossDmg) / (100 + dmg + bossDmg)
            2 -> return (100 + dmg + bossDmg + deltaList[2]) / (100 + dmg + bossDmg)
            3 -> {
                var newCriticalRate = criticalRate + deltaList[3]
                if (newCriticalRate > 100) newCriticalRate = 100f
                if (newCriticalRate < 0) newCriticalRate = 0f
                var percentage = (newCriticalRate * (135 + criticalDmg) + (100 - newCriticalRate) * 100) /
                        (criticalRate * (135 + criticalDmg) + (100 - criticalRate) * 100)
                return percentage
            }
            4 -> {
                var percentage = (criticalRate * (135 + criticalDmg + deltaList[4]) + 100 - criticalRate) /
                        (criticalRate * (135 + criticalDmg) + 100 - criticalRate)
                return percentage
            }
            5 -> {
                var percentage = ((basicMainAttr + deltaList[5]) * (100 + mainAttrPer) / 100 * 4 + extraMainAttr * 4 + secondAttr) /
                        (basicMainAttr * (100 + mainAttrPer) / 100 * 4 + extraMainAttr * 4 + secondAttr)
                return percentage
            }
            6 -> {
                var percentage = (basicMainAttr * (100 + mainAttrPer + deltaList[6]) / 100 * 4 + extraMainAttr * 4 + secondAttr) /
                        (basicMainAttr * (100 + mainAttrPer) / 100 * 4 + extraMainAttr * 4 + secondAttr)
                return percentage
            }
            7 -> return deltaList[7] / basicAtt + 1
            8 -> return (100 + deltaList[8] + attPer) / (100 + attPer)
        }
        return 0f
    }

    private fun update() {
        for (i in (0 until etList.size)) {
            updateSingle(i)
        }
    }

    private fun updateSingle(index: Int) {
        if (index > 4 && !mainAttrPerAndAttPerEnable) return
        try {
            deltaList[index] = etList[index].text.toString().toFloat()
        } catch (e: Exception) {
            deltaList[index] = 0f
            etList[index].setText("0")
        }
        var percentage = (getPercentage(index) - 1) * 100
        txtResultList[index].text = "%.2f".format(percentage).toString()
    }

    private fun updateTotal() {
        var oldTotal =
            attrAtt / (100 + dmg) * (100 + dmg + bossDmg) * (criticalRate * (135 + criticalDmg) / 100 + 100 - criticalRate) / 100
        var newAttrList = ArrayList<Float>()
        for (i in (0 until attrList.size)) {
            if (includedList[i]) newAttrList.add(attrList[i] + deltaList[i])
            else newAttrList.add(attrList[i])
            if (i == 3) {
                if (newAttrList[3] > 100) newAttrList[3] = 100f
                if (newAttrList[3] < 0) newAttrList[4] = 0f
            }
        }
        if (mainAttrPerAndAttPerEnable) {
            var newTotal =
                attrAtt / (100 + dmg) * (100 + newAttrList[1] + newAttrList[2]) *
                        (newAttrList[3] * (135 + newAttrList[4]) / 100 + 100 - newAttrList[3]) / 100 /
                        (basicAtt * (100 + attPer) / 100) * (newAttrList[7] * (100 + newAttrList[8]) / 100) /
                        ((basicMainAttr * (100 + mainAttrPer) / 100 + extraMainAttr) * 4 + secondAttr) *
                        ((newAttrList[5] * (100 + newAttrList[6]) / 100 + newAttrList[0]) * 4 + secondAttr)
            var percentage = (newTotal / oldTotal - 1) * 100
            txtTotalResult.text = "%.2f".format(percentage).toString()
        } else {
            var newTotal =
                attrAtt / (mainAttr * 4 + secondAttr) * (mainAttr * 4 + secondAttr + newAttrList[0]) / (100 + dmg) * (100 + newAttrList[1] + newAttrList[2]) *
                        (newAttrList[3] * (135 + newAttrList[4]) / 100 + 100 - newAttrList[3]) / 100
            var percentage = (newTotal / oldTotal - 1) * 100
            txtTotalResult.text = "%.2f".format(percentage).toString()
        }
    }

    private fun setTxtClick(txt: TextView, index: Int) {
        txt.setOnClickListener(View.OnClickListener {
            if (!mainAttrPerAndAttPerEnable && index > 4) return@OnClickListener
            if (includedList[index]) txt.setBackgroundColor(resources.getColor(gray))
            else txt.setBackgroundColor(resources.getColor(purple_200))
            includedList[index] = !includedList[index]
            updateTotal()
        })
    }

    private fun setEtListener(et: EditText, index: Int) {
        et.setOnFocusChangeListener { view, b ->
            if (b) et.gravity = Gravity.CENTER_VERTICAL + Gravity.LEFT
            else {
                et.gravity = Gravity.CENTER_VERTICAL + Gravity.RIGHT
                if (et.text.isEmpty()) et.setText("0")
                updateSingle(index)
                if (includedList[index]) updateTotal()
            }
        }
        et.setOnEditorActionListener { textView, i, keyEvent ->
            if (i != EditorInfo.IME_ACTION_DONE) return@setOnEditorActionListener false

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            et.clearFocus()


            return@setOnEditorActionListener true
        }
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
}