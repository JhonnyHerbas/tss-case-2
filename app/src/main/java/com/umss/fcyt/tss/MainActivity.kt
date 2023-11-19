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
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.PointsGraphSeries
import com.umss.fcyt.tss.util.Constant
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.view.LineChartView
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

    private lateinit var tableHours: TableLayout

    private lateinit var priceHours: EditText

    private lateinit var priceReplacement: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val execute: Button = findViewById(R.id.simular)
        val input: EditText = findViewById(R.id.input)
        tableRandoms = findViewById(R.id.tableNumberLayout)
        tableVariables = findViewById(R.id.tableVariableLayout)
        tableHours = findViewById(R.id.hoursActive)

        priceHours = findViewById(R.id.priceHours)
        priceReplacement = findViewById(R.id.priceReplacement)

        val graphRandomNumbers: BarGraph = findViewById(R.id.graphRandomsNumbers)
        val graphVariables: LineChartView = findViewById(R.id.lineChart)
        val costs: BarGraph = findViewById(R.id.costs)

        execute.setOnClickListener {
            tableRandoms.removeAllViews()
            tableVariables.removeAllViews()
            tableHours.removeAllViews()

            validate(input.text.toString())
            val count = iterationsNumber(input.text.toString().toInt())
            val numbersOne = generateNumbers(count)
            val numbersTwo = generateNumbers(count)

            addHeaders("Indice", "Random Pol.1", "Random Pol.2", this.tableRandoms)

            addRowToTableDouble(numbersOne, numbersTwo, this.tableRandoms)
            dotPlot(numbersOne.average(), numbersTwo.average(), graphRandomNumbers)

            val formula: TextView = findViewById(R.id.formulaOne);
            formula.visibility = View.VISIBLE

            val variablesOne = generateRandomVariableBoxMuller1(numbersOne, numbersTwo)
            val variablesTwo = generateRandomVariableBoxMuller2(numbersOne, numbersTwo)

            addHeaders("Indice", "Box muller 1", "Box Muller 2", this.tableVariables)

            addRowToTableDouble(variablesOne, variablesTwo, this.tableVariables)

            val pointsOne = generatePoints(variablesOne)
            val pointsTwo = generatePoints(variablesTwo)

            graphicPoints(pointsOne, pointsTwo, graphVariables)

            val politicalHoursOne = generateNormalDistribution(variablesOne)
            val politicalHoursTwo = generateNormalDistribution(variablesTwo)

            addHeaders("Indice", "Horas Pol.1", "Horas Pol.2", this.tableHours)

            addRowToTableInt(politicalHoursOne, politicalHoursTwo, this.tableHours)

            graphicScatter(politicalHoursOne, findViewById<GraphView>(R.id.scatterOne))
            graphicScatter(politicalHoursTwo, findViewById<GraphView>(R.id.scatterTwo))

            val countOne = countsErrors(politicalHoursOne)
            val countErrorOne: TextView = findViewById(R.id.ErrorsPolOne)
            countErrorOne.text = "$countOne errores"
            val countTwo = countsErrors(politicalHoursTwo)
            val countErrorTwo: TextView = findViewById(R.id.ErrorsPolTwo)
            countErrorTwo.text = "$countTwo errores"

            validate(priceHours.text.toString())
            validate(priceReplacement.text.toString())
            val sumPolicyPriceOne = calculatePolicyOne(countOne)
            val sumPolicyPriceTwo = calculatePolicyTwo(countTwo)

            val pricePerHour: TextView = findViewById(R.id.pricePerHour)
            pricePerHour.setText("El valor del precio por hora es: Bs " + priceHours.text.toString())
            val pricePerSpare: TextView = findViewById(R.id.pricePerSpare)
            pricePerSpare.setText("El valor del precio por repuesto es: Bs " + priceReplacement.text.toString())

            dotPlot(sumPolicyPriceOne, sumPolicyPriceTwo, costs)

            if (sumPolicyPriceOne < sumPolicyPriceTwo) {
                val result: TextView = findViewById(R.id.optionFinal)
                result.setText("LA POLITICA 1 ES MAS EFICIENTE")
            } else {
                val result: TextView = findViewById(R.id.optionFinal)
                result.setText("LA POLITICA 2 ES MAS EFICIENTE")
            }
        }
    }

    /**
     *
     *
    CALCULA EL COSTE DE LA POLITICA UNO
     */
    private fun calculatePolicyOne(toInt: Int): Int {
        return toInt * (priceReplacement.text.toString().toInt() + priceHours.text.toString().toInt())
    }

    /**
     *
     *
    CALCULA EL COSTE DE LA POLITICA DOS
     */
    private fun calculatePolicyTwo(toInt: Int): Int {
        return toInt * ((priceReplacement.text.toString().toInt() * 4) + (priceHours.text.toString().toInt()*2))
    }

    /**
     *
     *
    GENERADOR DE HORAS
     */
    private fun generateNormalDistribution(variables: List<Double>): List<Int> {
        return variables.map { variable -> (Constant.AVERAGE_HOURS + Constant.DEVIATION * variable).toInt() }
    }

    /**
     *
     *
    NUMEROS DE BOX MULLER
     */
    private fun generateRandomVariableBoxMuller1(
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

    private fun countsErrors(listHours: List<Int>): Int {
        var result = 0;
        for (value in listHours) {
            if (value < 500 || value > 700) {
                result += 1
            }
        }
        return result
    }

    private fun graphicScatter(politicalHoursOne: List<Int>, graphView: GraphView) {
        graphView.removeAllSeries()
        val seriesBelow500 = PointsGraphSeries<DataPoint>()
        val seriesBetween500And700 = PointsGraphSeries<DataPoint>()

        politicalHoursOne.forEachIndexed { index, value ->
            val dataPoint = DataPoint(index.toDouble(), value.toDouble())

            if (value < 500 || value > 700) {
                seriesBelow500.appendData(dataPoint, true, politicalHoursOne.size)
            } else {
                seriesBetween500And700.appendData(dataPoint, true, politicalHoursOne.size)
            }
        }

        seriesBelow500.color = Color.RED

        seriesBelow500.size = 10f
        seriesBetween500And700.size = 10f

        graphView.addSeries(seriesBelow500)
        graphView.addSeries(seriesBetween500And700)
        graphView.viewport.isXAxisBoundsManual = true
        graphView.viewport.setMinX(0.0)
        graphView.viewport.setMaxX(politicalHoursOne.size.toDouble())
        graphView.gridLabelRenderer.isHorizontalLabelsVisible = false
    }

    private fun generateRandomVariableBoxMuller2(
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

    private fun graphicPoints(
        pointsOne: List<PointValue>,
        pointsTwo: List<PointValue>,
        graphVariables: LineChartView
    ) {
        val lineOne = Line(pointsOne).setColor(Color.BLUE)
        val lineTwo = Line(pointsTwo).setColor(Color.GREEN)

        val lines = mutableListOf<Line>()
        lines.add(lineOne)
        lines.add(lineTwo)

        val data = LineChartData()
        data.lines = lines

        val axisY = Axis()
        data.axisYLeft = axisY

        val axisX = Axis()
        axisX.isAutoGenerated = false
        data.axisXBottom = axisX

        graphVariables.lineChartData = data

        val label1: TextView = findViewById(R.id.VarLabel1)
        val label2: TextView = findViewById(R.id.VarLabel2)
        label1.visibility = View.VISIBLE
        label2.visibility = View.VISIBLE
    }

    private fun generatePoints(list: List<Double>): List<PointValue> {
        val values = mutableListOf<PointValue>()
        for ((index, value) in list.withIndex()) {
            values.add(PointValue(index.toFloat(), value.toFloat()))
        }
        return values
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

    private fun dotPlot(costPolOne: Int, costPolTwo: Int, graphRandomNumbers: BarGraph) {
        val bars = ArrayList<Bar>()

        val barOne = Bar()
        barOne.color = Color.BLUE
        barOne.name = "Costo total Pol.1"
        barOne.value = costPolOne.toFloat()
        bars.add(barOne)

        val barTwo = Bar()
        barTwo.color = Color.GREEN
        barTwo.name = "Costo total Pol.2"
        barTwo.value = costPolTwo.toFloat()
        bars.add(barTwo)

        graphRandomNumbers.bars = bars
    }

    private fun addHeaders(s: String, s1: String, s2: String, table: TableLayout) {
        val headerRow = TableRow(this)

        val headerColumn1 = TextView(this)
        headerColumn1.text = s
        headerColumn1.setTextColor(Color.rgb(248, 248, 248))
        headerColumn1.setTypeface(null, Typeface.BOLD)
        headerRow.addView(headerColumn1)

        val headerColumn2 = TextView(this)
        headerColumn2.text = s1
        headerColumn2.setTextColor(Color.rgb(248, 248, 248))
        headerColumn2.setTypeface(null, Typeface.BOLD)
        headerRow.addView(headerColumn2)

        val headerColumn3 = TextView(this)
        headerColumn3.text = s2
        headerColumn3.setTextColor(Color.rgb(248, 248, 248))
        headerColumn3.setTypeface(null, Typeface.BOLD)
        headerRow.addView(headerColumn3)

        table.addView(headerRow)
    }

    private fun validate(inputText: String) {
        if (inputText.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese datos correctos", Toast.LENGTH_SHORT)
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

    private fun addRowToTableDouble(
        numbersOne: List<Double>,
        numbersTwo: List<Double>,
        table: TableLayout
    ) {
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

    private fun addRowToTableInt(numbersOne: List<Int>, numbersTwo: List<Int>, table: TableLayout) {
        for (index in numbersOne.indices) {
            val dataRow = TableRow(this)

            val dataColumn1 = TextView(this)
            dataColumn1.text = (index + 1).toString()
            dataColumn1.setTextColor(Color.rgb(214, 214, 214))
            dataRow.addView(dataColumn1)

            val dataColumn2 = TextView(this)
            dataColumn2.text = numbersOne[index].toString() + " hrs"
            if (numbersOne[index] > 700 || numbersOne[index] < 500) {
                dataColumn2.setTextColor(Color.RED)
            } else {
                dataColumn2.setTextColor(Color.rgb(214, 214, 214))
            }
            dataRow.addView(dataColumn2)

            val dataColumn3 = TextView(this)
            dataColumn3.text = numbersTwo[index].toString() + " hrs"
            if (numbersTwo[index] > 700 || numbersTwo[index] < 500) {
                dataColumn3.setTextColor(Color.RED)
            } else {
                dataColumn3.setTextColor(Color.rgb(214, 214, 214))
            }
            dataRow.addView(dataColumn3)

            table.addView(dataRow)
        }
    }
}
