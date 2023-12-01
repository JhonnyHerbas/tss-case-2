package com.umss.fcyt.tss.classes

import lecho.lib.hellocharts.model.PointValue

class Assistant {

    fun generatePoints(list: List<Double>): List<PointValue> {
        val values = mutableListOf<PointValue>()
        for ((index, value) in list.withIndex()) {
            values.add(PointValue(index.toFloat(), value.toFloat()))
        }
        return values
    }

    fun countsErrors(listHours: List<Int>): Int {
        var result = 0
        for (value in listHours) {
            if (value < 500 || value > 700) {
                result += 1
            }
        }
        return result
    }
}