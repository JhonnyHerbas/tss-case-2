package com.umss.fcyt.tss

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.echo.holographlibrary.Bar
import com.echo.holographlibrary.BarGraph
import com.umss.fcyt.tss.util.Constant
import java.lang.Math.ceil
import java.text.DecimalFormat
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var tableRandoms: TableLayout

    private lateinit var tableVariables: TableLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val execute: Button = findViewById(R.id.simular)
        val input: EditText = findViewById(R.id.input)
        tableRandoms = findViewById(R.id.tableNumberLayout)
        tableVariables = findViewById(R.id.tableVariableLayout)

        val graphRandomNumbers: BarGraph = findViewById(R.id.graphRandomsNumbers)

        execute.setOnClickListener {
            tableRandoms.removeAllViews()
            tableVariables.removeAllViews()
            validate(input.text.toString())
            val count = iterationsNumber(input.text.toString().toInt())
            val numbersOne = generateNumbers(count)
            val numbersTwo = generateNumbers(count)

            addHeaders("Indice", "Random Pol.1", "Random Pol.2", this.tableRandoms)

            addRowToTable(numbersOne, numbersTwo, this.tableRandoms)
            dotPlot(numbersOne.average(), numbersTwo.average(), graphRandomNumbers)

            val formula: TextView = findViewById(R.id.formulaOne);
            formula.visibility = View.VISIBLE

            val variablesOne = generateRandomVariableBoxMuller1(numbersOne, numbersTwo)
            val variablesTwo = generateRandomVariableBoxMuller2(numbersOne, numbersTwo)

            addHeaders("Indice", "Box muller 1", "Box Muller 2", this.tableVariables)

            addRowToTable(variablesOne, variablesTwo, this.tableVariables)
        }
    }

    private fun dotPlot(average: Double, average1: Double, graphRandomNumbers: BarGraph) {
        val bars = ArrayList<Bar>()

        val barOne = Bar()
        barOne.color = Color.BLUE
        barOne.name = "Promedio Ramdom Pol.1"
        barOne.value = average.toFloat()
        bars.add(barOne)

        val barTwo = Bar()
        barTwo.color = Color.GREEN
        barTwo.name = "Promedio Ramdom Pol.2"
        barTwo.value = average1.toFloat()
        bars.add(barTwo)

        graphRandomNumbers.bars = bars
    }

    private fun addHeaders(s: String, s1: String, s2: String, table: TableLayout) {
        val headerRow = TableRow(this)

        val headerColumn1 = TextView(this)
        headerColumn1.text = s
        headerColumn1.setTextColor(Color.rgb(248, 248,248))
        headerColumn1.setTypeface(null, Typeface.BOLD)
        headerRow.addView(headerColumn1)

        val headerColumn2 = TextView(this)
        headerColumn2.text = s1
        headerColumn2.setTextColor(Color.rgb(248, 248,248))
        headerColumn2.setTypeface(null, Typeface.BOLD)
        headerRow.addView(headerColumn2)

        val headerColumn3 = TextView(this)
        headerColumn3.text = s2
        headerColumn3.setTextColor(Color.rgb(248, 248,248))
        headerColumn3.setTypeface(null, Typeface.BOLD)
        headerRow.addView(headerColumn3)

        table.addView(headerRow)
    }

    private fun generateRandomVariableBoxMuller1(numbersOne: List<Double>, numbersTwo: List<Double>): List<Double> {
        val variables = mutableListOf<Double>()
        for (index in numbersOne.indices) {
            val decimalFormat = DecimalFormat("#.#####")
            val calculate = sqrt(-2 * ln(numbersOne[index])) * cos(2 * PI * numbersTwo[index])
            val calculateFormat = decimalFormat.format(calculate).toDouble()
            variables.add(calculateFormat)
        }
        return variables
    }

    private fun generateRandomVariableBoxMuller2(numbersOne: List<Double>, numbersTwo: List<Double>): List<Double> {
        val variables = mutableListOf<Double>()
        for (index in numbersOne.indices) {
            val decimalFormat = DecimalFormat("#.#####")
            val calculate = sqrt(-2 * ln(numbersOne[index])) * sin(2 * PI * numbersTwo[index])
            val calculateFormat = decimalFormat.format(calculate).toDouble()
            variables.add(calculateFormat)
        }
        return variables
    }

    private fun validate(inputText: String) {
        if (inputText.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese la cantidad de horas", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun iterationsNumber(averageHours: Int): Int {
        return ceil(averageHours.toDouble() / Constant.AVERAGE_HOURS).toInt()
    }

    private fun generateNumbers(count: Int): List<Double> {
        val randomNumbers = mutableListOf<Double>()
        repeat(count) {
            val decimalFormat = DecimalFormat("#.#####")
            val roundedNumber = decimalFormat.format(Random.nextDouble()).toDouble()
            randomNumbers.add(roundedNumber)
        }
        return randomNumbers
    }

    private fun addRowToTable(numbersOne: List<Double>, numbersTwo: List<Double>, table: TableLayout) {
        for (index in numbersOne.indices) {
            val dataRow = TableRow(this)

            val dataColumn1 = TextView(this)
            dataColumn1.text = (index + 1).toString()
            dataColumn1.setTextColor(Color.rgb(214, 214, 214))
            dataRow.addView(dataColumn1)

            val dataColumn2 = TextView(this)
            dataColumn2.text = numbersOne[index].toString()
            dataColumn2.setTextColor(Color.rgb(214, 214, 214))
            dataRow.addView(dataColumn2)

            val dataColumn3 = TextView(this)
            dataColumn3.text = numbersTwo[index].toString()
            dataColumn3.setTextColor(Color.rgb(214, 214, 214))
            dataRow.addView(dataColumn3)

            table.addView(dataRow)
        }
    }
}
