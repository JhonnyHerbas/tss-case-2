package com.umss.fcyt.tss.classes

import android.widget.EditText
import com.umss.fcyt.tss.util.Constant
import java.text.DecimalFormat
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

class Functions {

    fun generateNumbers(count: Int): List<Double> {
        val randomNumbers = mutableListOf<Double>()
        repeat(count) {
            val decimalFormat = DecimalFormat("#.#####")
            val roundedNumber = decimalFormat.format(Random.nextDouble()).toDouble()
            randomNumbers.add(roundedNumber)
        }
        return randomNumbers
    }

    fun generateRandomVariableBoxMuller1(
        numbersOne: List<Double>,
        numbersTwo: List<Double>
    ): List<Double> {
        val variables = mutableListOf<Double>()
        for (index in numbersOne.indices) {
            val decimalFormat = DecimalFormat("#.#####")
            val calculate = sqrt(-2 * ln(numbersOne[index])) * cos(2 * PI * numbersTwo[index])
            val calculateFormat = decimalFormat.format(calculate).toDouble()
            variables.add(calculateFormat)
        }
        return variables
    }

    fun generateRandomVariableBoxMuller2(
        numbersOne: List<Double>,
        numbersTwo: List<Double>
    ): List<Double> {
        val variables = mutableListOf<Double>()
        for (index in numbersOne.indices) {
            val decimalFormat = DecimalFormat("#.#####")
            val calculate = sqrt(-2 * ln(numbersOne[index])) * sin(2 * PI * numbersTwo[index])
            val calculateFormat = decimalFormat.format(calculate).toDouble()
            variables.add(calculateFormat)
        }
        return variables
    }

    fun calculatePolicyOne(toInt: Int, priceReplacement: EditText, priceHours: EditText): Int {
        return toInt * (priceReplacement.text.toString().toInt() + priceHours.text.toString().toInt())
    }

    fun calculatePolicyTwo(toInt: Int, priceReplacement: EditText, priceHours: EditText): Int {
        return toInt * ((priceReplacement.text.toString().toInt() * 4) + (priceHours.text.toString().toInt()*2))
    }

    fun generateNormalDistribution(variables: List<Double>): List<Int> {
        return variables.map { variable -> (Constant.AVERAGE_HOURS + Constant.DEVIATION * variable).toInt() }
    }
}