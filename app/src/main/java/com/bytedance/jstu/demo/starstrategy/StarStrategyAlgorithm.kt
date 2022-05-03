package com.bytedance.jstu.demo.starstrategy

import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.Integer.max

class StarStrategyAlgorithm {

    private val maxStarLevel = arrayListOf<Int>()
    private var starRequire = arrayListOf<ArrayList<Int>>()
    private val tooLarge = 65535
    private var rate = arrayListOf<Float>()

    init {
        initMaxStarLevel()
        initStarRequire()
        initRate()
    }

    private fun initMaxStarLevel() {
        maxStarLevel.addAll(arrayListOf<Int>(8, 10, 12, 12, 12, 12, 12))
    }

    private fun initStarRequire() {
        // 100 级
        starRequire.add(
            arrayListOf<Int>(
                36, 36, 37, 38, 51,
                58, 60, 60
            )
        )

        // 110 级
        starRequire.add(
            arrayListOf<Int>(
                48, 48, 49, 50, 65,
                72, 74, 74, 89, 90
            )
        )

        // 120 级
        starRequire.add(
            arrayListOf<Int>(
                57, 57, 58, 59, 76,
                83, 85, 85, 102, 103,
                144, 145, 164, 165, 167
            )
        )

        // 130 级
        starRequire.add(
            arrayListOf<Int>(
                60, 60, 61, 62, 79,
                86, 88, 88, 105, 106,
                147, 148, 167, 168, 170,
                179, tooLarge, tooLarge, tooLarge, tooLarge
            )
        )

        // 140 级
        starRequire.add(
            arrayListOf<Int>(
                70, 70, 71, 72, 91,
                98, 100, 100, 119, 120,
                161, 162, 183, 184, 186,
                195, 215, 216, tooLarge, tooLarge,
                tooLarge, tooLarge, tooLarge, tooLarge, tooLarge
            )
        )

        // 150 级
        starRequire.add(
            arrayListOf<Int>(
                93, 93, 94, 95, 126,
                154, 156, 156, 187, 188,
                250, 251, 284, 285, tooLarge,
                317, 349, 350, tooLarge, tooLarge,
                tooLarge, 411, tooLarge, tooLarge, tooLarge
            )
        )

        // 160 级
        starRequire.add(
            arrayListOf<Int>(
                112, 112, 113, 114, 151,
                185, 187, 187, 224, 226,
                300, 301, tooLarge, tooLarge, tooLarge,
                tooLarge, tooLarge, tooLarge, tooLarge, tooLarge,
                tooLarge, tooLarge, tooLarge, tooLarge, tooLarge,
            )
        )
    }

    fun initRate() {
        rate.addAll(
            // 数据来源于 “冒险岛 BWIKI 上星模拟器” 不考虑损坏
            arrayListOf<Float>(
                0.85F, 0.80F, 0.75F, 0.70F, 0.70F,
                0.65F, 0.60F, 0.55F, 0.50F, 0.45F,
                0.40F, 0.30F, 0.25F, 0.25F, 0.20F,
                0.20F, 0.20F, 0.20F, 0.15F, 0.15F,
                0.15F, 0.10F, 0.03F, 0.02F, 0.01F
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun calculate(
        startLevel: Int,
        startStarLevel: Int,
        targetLevel: Int,
        targetStarLevel: Int
    ): ArrayList<RouteCost> {
        if (targetStarLevel > maxStarLevel[targetLevel]) return arrayListOf<RouteCost>()

        val route = arrayListOf<Int>()
        for (i in (0 until (targetLevel - startLevel))) {
            route.add(1)
        }

        val routeCost = arrayListOf<RouteCost>()

        var currLevel = startLevel
        var currStarLevel = startStarLevel
        var currCostStar = 0F
        var count = targetLevel - 1

        while (startLevel < targetLevel || (startLevel == targetLevel && startStarLevel <= targetStarLevel)) {
            currLevel = startLevel
            currStarLevel = startStarLevel
            currCostStar = 0F

            while (currLevel < targetLevel) {
                while (currStarLevel < route[currLevel - startLevel]) {
                    currCostStar += starRequire[currLevel][currStarLevel].toFloat() / rate[currStarLevel]
                    currStarLevel += 1
                }
                currLevel += 1
                currStarLevel -= 1
            }

            while (currStarLevel < targetStarLevel) {
                currCostStar += starRequire[targetLevel][currStarLevel].toFloat() / rate[currStarLevel]
                currStarLevel += 1
            }

            routeCost.add(RouteCost(route, currCostStar))

            count = targetLevel - 1
            while (count != targetLevel && count >= startLevel) {
                route[count - startLevel] += 1
                if (route[count - startLevel] > maxStarLevel[count] || route[count - startLevel] > targetStarLevel + targetLevel - startLevel - count) {
                    route[count - startLevel] = -1
                    count -= 1
                    if (count < startLevel) break
                    else continue
                } else {
                    count += 1
                    if (count < targetLevel) {
                        if (1 > route[count - startLevel - 1] - 1) route[count - startLevel] = 0
                        else route[count - startLevel] = route[count - startLevel - 1] - 2
                    }
                }
            }
            if (count < startLevel) break;
        }

        sort(routeCost, 0, routeCost.size)

        return routeCost
    }

    fun sort(arr: ArrayList<RouteCost>, l: Int, r: Int) {
        when (r - l) {
            0 -> return
            1 -> return
            2 -> {
                if (arr[l].cost > arr[l + 1].cost) {
                    var temp: RouteCost = RouteCost(arrayListOf(), 0F)
                    temp.copy(arr[l])
                    arr[l].copy(arr[l + 1])
                    arr[l + 1].copy(temp)
                }
                return
            }
        }

        var m = (l + r) / 2
        sort(arr, l, m)
        sort(arr, m, r)
        var tempArr = arrayListOf<RouteCost>()
        var il = l
        var ir = m
        while (il < m && ir < r) {
            if (arr[il].cost < arr[ir].cost) {
                tempArr.add(RouteCost(arrayListOf(), 0f))
                tempArr[tempArr.size - 1].copy(arr[il])
                il += 1
            } else {
                tempArr.add(RouteCost(arrayListOf(), 0f))
                tempArr[tempArr.size - 1].copy(arr[ir])
                ir += 1
            }
        }
        while (il < m) {
            tempArr.add(RouteCost(arrayListOf(), 0f))
            tempArr[tempArr.size - 1].copy(arr[il])
            il += 1
        }
        while (ir < r) {
            tempArr.add(RouteCost(arrayListOf(), 0f))
            tempArr[tempArr.size - 1].copy(arr[ir])
            ir += 1
        }
        var i = l
        while (i < r) {
            arr[i].copy(tempArr[i - l])
            i += 1
        }

    }
}

class RouteCost(inputRoute: ArrayList<Int>, inputCost: Float) {
    val route: ArrayList<Int> = arrayListOf<Int>()
    var cost: Float = inputCost

    init {
        route.addAll(inputRoute)
    }

    fun copy(_routeCost: RouteCost) {
        route.clear()
        for (i in (0 until _routeCost.route.size)) {
            route.add(0)
            route[i] = _routeCost.route[i]
        }
        cost = _routeCost.cost
    }
}
