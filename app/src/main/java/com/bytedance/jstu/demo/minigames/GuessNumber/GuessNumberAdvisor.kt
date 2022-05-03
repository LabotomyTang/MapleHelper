package com.bytedance.jstu.demo.minigames.GuessNumber

import android.util.Log

class GuessNumberAdvisor {

    var times: Int = 0
    lateinit var lastAdvice: Array<Int>


    val keyPool: ArrayList<Key> = arrayListOf<Key>(
        Key("1"),
        Key("2"),
        Key("3"),
        Key("4"),
        Key("5"),
        Key("6"),
        Key("7"),
        Key("8"),
        Key("9")
    )
    var keyPoolSize: Int = 9
    var answerLength: Int = 3

    var totalChoicePool = ArrayList<Answer>()
    var choicePool = ArrayList<Answer>()
    var answerPool = ArrayList<Answer>()

    init {
        lastAdvice = Array<Int>(answerLength) { i -> 0}
    }

    private fun initPool() {
        totalChoicePool.clear()
        answerPool.clear()
        var pointer = Array<Int>(answerLength) { i -> -1 }
        var pos = 0
        while (pos >= 0) {
            pointer[pos] += 1
            while (pointer[pos] < keyPoolSize && keyPool[pointer[pos]].choosed) {
                pointer[pos] += 1
            }
            if (pointer[pos] == keyPoolSize) {
                pos -= 1
                if (pos < 0) break
                keyPool[pointer[pos]].choosed = false
                continue
            }
            if (pos < answerLength - 1) {
                keyPool[pointer[pos]].choosed = true
                pos += 1
                pointer[pos] = -1
                continue
            }
            totalChoicePool.add(Answer(answerLength, pointer))
            answerPool.add(Answer(answerLength, pointer))
        }
    }

    private fun getChoiceType(answer: Answer, result: Array<Int>) {
        if (result.size != answerLength) {
            Log.e("zhTang", "Length mismatch. from getChoiceType")
            return
        }
        for (i in (0 until answerLength)) {
            if (keyPool[answer.keys[i]].checked) result[i] = answer.keys[i]
            else result[i] = -1
        }
    }

    private fun resultToIndex(result: Array<Int>): Int {
        if (result.size != 2) {
            Log.e("zhTang", "Array size should be 2. from resultToIndex")
            return -1
        }
        var index = 0
        var tempArr = Array<Int>(2) { i -> result[i] }
        while (tempArr[0] >= 0) {
            while (tempArr[1] >= 0) {
                tempArr[1] -= 1
                index += 1
            }
            tempArr[0] -= 1
            tempArr[1] = answerLength - tempArr[0]
        }
        return index - 1
    }

    fun firstAdvice(advice: Array<String>) {
        initPool()
        updateChoicePool()
        var adviceIndex = evaluateChoicePool()
        giveAdvice(adviceIndex, advice)
    }

    fun getResponse(guess: Answer, result: Array<Int>, advice: Array<String>): Int {
        if (result[0] == answerLength) {
            for (i in (0 until answerLength)) {
                advice[i] = keyPool[guess.keys[i]].value
            }
            return 1
        }
        recordCheck(guess)
        var isFinished: Int = filterAnswerPool(guess, result)
        if (isFinished == -1) return isFinished
        updateTotalChoicePool()
        updateChoicePool()
        var adviceIndex = evaluateChoicePool()
        giveAdvice(adviceIndex, advice)
        if (isFinished == 1) return isFinished
        return 0
    }

    fun restart() {
        times = 0
        for (i in (0 until keyPoolSize)) {
            keyPool[i].restart()
        }
        totalChoicePool.clear()
        answerPool.clear()
        choicePool.clear()
    }

    private fun recordCheck(guess: Answer) {
        for (i in (0 until answerLength)) {
            keyPool[guess.keys[i]].checked = true
        }
    }

    private fun filterAnswerPool(guess: Answer, result: Array<Int>): Int {
        for (i in (0 until keyPoolSize)) {
            keyPool[i].out = true
        }
        var comparation = arrayOf<Int>(0, 0)
        var index: Int = 0
        while (index < answerPool.size) {
            guess.compare(answerPool[index], comparation)
            if (comparation[0] != result[0] || comparation[1] != result[1]) {
                answerPool.removeAt(index)
                index -= 1
            } else {
                for (i in (0 until answerLength)) {
                    keyPool[answerPool[index].keys[i]].out = false
                }
            }
            index += 1
        }
        if (answerPool.size == 0) return -1
        else if (answerPool.size == 1) return 1
        else return 0
    }

    private fun updateTotalChoicePool() {
        var index = 0
        while (index < totalChoicePool.size) {
            var out = false;
            for (i2 in (0 until answerLength)) {
                var keyPoolIndex = totalChoicePool[index].keys[i2]
                if (keyPool[keyPoolIndex].out) {
                    out = true
                    break
                }
            }
            if (out) {
                totalChoicePool.removeAt(index)
                index -= 1
            }
            index += 1
        }
    }


    private fun updateChoicePool() {
        choicePool.clear()
        if (answerPool.size == 1) {
            choicePool.add(answerPool[0])
            return
        }
        var typeRecord = ArrayList<Array<Int>>()
        var existed = false
        var equal = true
        for (i in (0 until totalChoicePool.size)) {
            var tempArr = Array<Int>(answerLength) { i -> 0 }
            getChoiceType(totalChoicePool[i], tempArr)
            existed = false
            for (i2 in (0 until typeRecord.size)) {
                equal = true
                for (i3 in (0 until answerLength)) {
                    if (typeRecord[i2][i3] != tempArr[i3]) {
                        equal = false
                        break
                    }
                }
                if (equal) {
                    existed = true
                    break
                }
            }
            if (existed) continue
            choicePool.add(totalChoicePool[i])
            typeRecord.add(tempArr)
        }
    }

    private fun evaluateChoicePool(): Int {
        var score = Array<Int>(choicePool.size) { i -> 0 }
        var situationAmt = resultToIndex(arrayOf(answerLength, 0)) + 1
        var bestChoice = 0
        for (i in (0 until choicePool.size)) {
            var nextSituation = Array<Int>(situationAmt) { i -> 0 }
            for (i2 in (0 until answerPool.size)) {
                var tempResult = Array<Int>(2) { i -> 0}
                choicePool[i].compare(answerPool[i2], tempResult)
                nextSituation[resultToIndex(tempResult)] += 1
            }
            for (i2 in (0 until situationAmt)) {
                score[i] += nextSituation[i2] * nextSituation[i2]
            }
            if (score[i] < score[bestChoice]) bestChoice = i
        }
        return bestChoice
    }


    private fun giveAdvice(index: Int, advice: Array<String>) {
        var adviceChoice = choicePool[index]
        for (i in (0 until answerLength)) {
            advice[i] = keyPool[adviceChoice.keys[i]].value
            lastAdvice[i] = choicePool[index].keys[i]
        }
        times += 1
    }


    class Key(_value: String) {
        var value: String = _value
        var choosed: Boolean = false
        var checked: Boolean = false
        var out = false

        fun restart() {
            choosed = false
            checked = false
            out = false
        }
    }

    class Answer(_length: Int, _keys: Array<Int>) {
        var length: Int = _length
        var keys = Array<Int>(_length) { i -> 0 }

        init {
            for (i in (0 until _length)) {
                keys[i] = _keys[i]
            }
        }

        fun compare(_answer: Answer, result: Array<Int>) {
            if (_answer.length != length) return
            var a: Int = 0
            var b: Int = 0
            for (i in (0 until length)) {
                for (j in (0 until length)) {
                    if (keys[i] == _answer.keys[j]) {
                        b += 1
                        break
                    }
                }
                if (keys[i] == _answer.keys[i]) {
                    a += 1
                    b -= 1
                }
            }
            result[0] = a
            result[1] = b
        }
    }

}