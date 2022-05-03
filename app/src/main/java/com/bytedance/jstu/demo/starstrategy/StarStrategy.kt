package com.bytedance.jstu.demo.starstrategy

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import com.bytedance.jstu.demo.R
import kotlin.concurrent.thread

class StarStrategy : AppCompatActivity() {
    private val algorithm = StarStrategyAlgorithm()

    private val spinnerStartLevel: Spinner by lazy {
        findViewById<Spinner>(R.id.starstrategy_spinner_startLevel)
    }
    private val spinnerStartStarLevel: Spinner by lazy {
        findViewById<Spinner>(R.id.starstrategy_spinner_startstarlevel)
    }
    private val spinnerTargetLevel: Spinner by lazy {
        findViewById<Spinner>(R.id.starstrategy_spinner_targetlevel)
    }
    private val spinnerTargetStarLevel: Spinner by lazy {
        findViewById<Spinner>(R.id.starstrategy_spinner_targetstarlevel)
    }

    private val btnCalculate: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.starstrategy_btn_calculate)
    }

    private val textResultGrid = arrayListOf<ArrayList<TextView>>()
    private val textResultStar = arrayListOf<TextView>()

    private val textTotalStar: TextView by lazy {
        findViewById<TextView>(R.id.starstrategy_txt_totalstar)
    }

    private val progressBarWaiting: ProgressBar by lazy {
        findViewById<ProgressBar>(R.id.starstrategy_pb_waiting)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starstrategy)
        initLevelSpinner(spinnerStartLevel)
        initStarLevelSpinner(spinnerStartStarLevel)
        initLevelSpinner(spinnerTargetLevel)
        initStarLevelSpinner(spinnerTargetStarLevel)

        initTextResult()

        btnCalculate.setOnClickListener(View.OnClickListener {
            progressBarWaiting.visibility = View.VISIBLE
            calculate()
            progressBarWaiting.visibility = View.INVISIBLE

        })

    }

    fun initLevelSpinner(_spinner: Spinner) {
        val option = arrayOf<String>("100", "110", "120", "130", "140", "150", "160")
        var adapter: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.starstrategy_spinner_item, option)
        adapter.setDropDownViewResource(R.layout.starstrategy_spinner_dropitem)
        _spinner.adapter = adapter
    }
    fun initStarLevelSpinner(_spinner: Spinner) {
        val option = arrayOf<String>("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        var adapter: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.starstrategy_spinner_item, option)
        adapter.setDropDownViewResource(R.layout.starstrategy_spinner_dropitem)
        _spinner.adapter = adapter
    }

    fun initTextResult() {
        for (i in (0 until 4)) {
            textResultGrid.add(arrayListOf<TextView>())
        }
        textResultGrid[0].add(findViewById<TextView>(R.id.starstrategy_txt_res00))
        textResultGrid[0].add(findViewById<TextView>(R.id.starstrategy_txt_res01))
        textResultGrid[0].add(findViewById<TextView>(R.id.starstrategy_txt_res02))
        textResultGrid[0].add(findViewById<TextView>(R.id.starstrategy_txt_res03))
        textResultGrid[0].add(findViewById<TextView>(R.id.starstrategy_txt_res04))
        textResultGrid[1].add(findViewById<TextView>(R.id.starstrategy_txt_res10))
        textResultGrid[1].add(findViewById<TextView>(R.id.starstrategy_txt_res11))
        textResultGrid[1].add(findViewById<TextView>(R.id.starstrategy_txt_res12))
        textResultGrid[1].add(findViewById<TextView>(R.id.starstrategy_txt_res13))
        textResultGrid[1].add(findViewById<TextView>(R.id.starstrategy_txt_res14))
        textResultGrid[2].add(findViewById<TextView>(R.id.starstrategy_txt_res20))
        textResultGrid[2].add(findViewById<TextView>(R.id.starstrategy_txt_res21))
        textResultGrid[2].add(findViewById<TextView>(R.id.starstrategy_txt_res22))
        textResultGrid[2].add(findViewById<TextView>(R.id.starstrategy_txt_res23))
        textResultGrid[2].add(findViewById<TextView>(R.id.starstrategy_txt_res24))
        textResultGrid[3].add(findViewById<TextView>(R.id.starstrategy_txt_res30))
        textResultGrid[3].add(findViewById<TextView>(R.id.starstrategy_txt_res31))
        textResultGrid[3].add(findViewById<TextView>(R.id.starstrategy_txt_res32))
        textResultGrid[3].add(findViewById<TextView>(R.id.starstrategy_txt_res33))
        textResultGrid[3].add(findViewById<TextView>(R.id.starstrategy_txt_res34))

        textResultStar.add(textResultGrid[1][1])
        textResultStar.add(textResultGrid[1][2])
        textResultStar.add(textResultGrid[1][3])
        textResultStar.add(textResultGrid[1][4])
        textResultStar.add(textResultGrid[3][1])
        textResultStar.add(textResultGrid[3][2])
        textResultStar.add(textResultGrid[3][3])
    }

    fun calculate() {
        resClear()

        var startLevel = spinnerStartLevel.selectedItemId.toInt()
        var startStarLevel = spinnerStartStarLevel.selectedItemId.toInt()
        var targetLevel = spinnerTargetLevel.selectedItemId.toInt()
        var targetStarLevel = spinnerTargetStarLevel.selectedItemId.toInt()

        var routeCost = algorithm.calculate(startLevel, startStarLevel, targetLevel, targetStarLevel)

        if (routeCost.size == 0) {
            textTotalStar.text = "错误，请检查条件是否合理"
            resClear()
        } else {
            var bestRoute = routeCost[0].route
            var bestCost = routeCost[0].cost
            textTotalStar.text = "%.2f".format(bestCost).toString()
            for (i in (0 until bestRoute.size)) {
                textResultStar[i + startLevel].text = bestRoute[i].toString()
            }
            textResultStar[targetLevel].text = targetStarLevel.toString()
        }

    }

    private fun resClear() {
        for (i in (0 until 7)) {
            textResultStar[i].text = ""
        }
    }


}