package com.bytedance.jstu.demo

class MathMethod {
    fun stringToInt(_string: String): Int {
        when (_string) {
            "0" -> return 0
            "1" -> return 1
            "2" -> return 2
            "3" -> return 3
            "4" -> return 4
            "5" -> return 5
            "6" -> return 6
            "7" -> return 7
            "8" -> return 8
            "9" -> return 9
        }
        return -1
    }
}