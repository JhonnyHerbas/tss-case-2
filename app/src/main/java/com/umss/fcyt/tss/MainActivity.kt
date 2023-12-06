package com.umss.fcyt.tss

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.echo.holographlibrary.BarGraph
import com.umss.fcyt.tss.classes.AddGraphics
import com.umss.fcyt.tss.classes.Assistant
import com.umss.fcyt.tss.classes.Functions
import com.umss.fcyt.tss.classes.SetTables
import com.umss.fcyt.tss.classes.Simulation
import com.umss.fcyt.tss.util.Constant
import lecho.lib.hellocharts.view.LineChartView

class MainActivity : AppCompatActivity() {

    private lateinit var tableRandoms: TableLayout

    private lateinit var tableVariables: TableLayout

    private lateinit var tableHours: TableLayout

    private val function: Functions = Functions()
    private val setTables: SetTables = SetTables()
    private val addGraphics: AddGraphics = AddGraphics()
    private val assistant: Assistant = Assistant()

    private val history: ArrayList<Simulation> = ArrayList()

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val execute: Button = findViewById(R.id.simular)
        val input: EditText = findViewById(R.id.input)
        tableRandoms = findViewById(R.id.tableNumberLayout)
        tableVariables = findViewById(R.id.tableVariableLayout)
        tableHours = findViewById(R.id.hoursActive)

        val priceHours: EditText = findViewById(R.id.priceHours)
        val priceReplacement: EditText = findViewById(R.id.priceReplacement)

        val graphRandomNumbers: BarGraph = findViewById(R.id.graphRandomsNumbers)
        val graphVariables: LineChartView = findViewById(R.id.lineChart)
        val costs: BarGraph = findViewById(R.id.costs)

        execute.setOnClickListener {
            tableRandoms.removeAllViews()
            tableVariables.removeAllViews()
            tableHours.removeAllViews()

            validate(input.text.toString())
            validate(priceHours.text.toString())
            validate(priceReplacement.text.toString())

            val count = iterationsNumber(input.text.toString().toInt())
            val numbersOne = function.generateNumbers(count)
            val numbersTwo = function.generateNumbers(count)

            setTables.addHeaders("Indice", "Random Pol.1", "Random Pol.2", this.tableRandoms, this)

            setTables.addRowToTableDouble(numbersOne, numbersTwo, this.tableRandoms, this)
            addGraphics.dotPlot(
                numbersOne.average(),
                numbersTwo.average(),
                graphRandomNumbers,
                "Promedio Ramdom Pol.1",
                "Promedio Ramdom Pol.2"
            )

            val variablesOne = function.generateRandomVariableBoxMuller1(numbersOne, numbersTwo)
            val variablesTwo = function.generateRandomVariableBoxMuller2(numbersOne, numbersTwo)

            setTables.addHeaders(
                "Indice",
                "Box muller 1",
                "Box Muller 2",
                this.tableVariables,
                this
            )

            setTables.addRowToTableDouble(variablesOne, variablesTwo, this.tableVariables, this)

            val pointsOne = assistant.generatePoints(variablesOne)
            val pointsTwo = assistant.generatePoints(variablesTwo)

            addGraphics.graphicPoints(
                pointsOne,
                pointsTwo,
                graphVariables,
                findViewById(R.id.VarLabel1),
                findViewById(R.id.VarLabel2)
            )

            val politicalHoursOne = function.generateNormalDistribution(variablesOne)
            val politicalHoursTwo = function.generateNormalDistribution(variablesTwo)

            setTables.addHeaders("Indice", "Horas Pol.1", "Horas Pol.2", this.tableHours, this)

            setTables.addRowToTableInt(politicalHoursOne, politicalHoursTwo, this.tableHours, this)

            addGraphics.graphicScatter(politicalHoursOne, findViewById(R.id.scatterOne))
            addGraphics.graphicScatter(politicalHoursTwo, findViewById(R.id.scatterTwo))

            val countOne = assistant.countsErrors(politicalHoursOne)
            val countErrorOne: TextView = findViewById(R.id.ErrorsPolOne)
            countErrorOne.text = "$countOne errores"
            val countTwo = assistant.countsErrors(politicalHoursTwo)
            val countErrorTwo: TextView = findViewById(R.id.ErrorsPolTwo)
            countErrorTwo.text = "$countTwo errores"

            val sumPolicyPriceOne =
                function.calculatePolicyOne(countOne, priceReplacement, priceHours)
            val sumPolicyPriceTwo =
                function.calculatePolicyTwo(countTwo, priceReplacement, priceHours)

            val pricePerHour: TextView = findViewById(R.id.pricePerHour)
            pricePerHour.text = "El valor del precio por hora es: Bs " + priceHours.text.toString()
            val pricePerSpare: TextView = findViewById(R.id.pricePerSpare)
            pricePerSpare.text =
                "El valor del precio por repuesto es: Bs " + priceReplacement.text.toString()

            addGraphics.dotPlot(
                sumPolicyPriceOne.toDouble(),
                sumPolicyPriceTwo.toDouble(),
                costs,
                "Costo total Pol.1",
                "Costo total Pol.2"
            )

            if (sumPolicyPriceOne < sumPolicyPriceTwo) {
                val result: TextView = findViewById(R.id.optionFinal)
                result.text = "LA POLITICA 1 ES MAS EFICIENTE"
            } else {
                val result: TextView = findViewById(R.id.optionFinal)
                result.text = "LA POLITICA 2 ES MAS EFICIENTE"
            }

            val newSimulation = Simulation(
                simulationHours = input.text.toString().toInt(),
                disconnectionPrice =  priceHours.text.toString().toInt(),
                replacementPrice = priceReplacement.text.toString().toInt(),
                policyCost1 = sumPolicyPriceOne.toDouble(),
                policyCost2 = sumPolicyPriceTwo.toDouble(),
                bestOption = if (sumPolicyPriceOne < sumPolicyPriceTwo) {
                    "Pol. 1"
                } else {
                    "Pol. 2"
                }
            )

            history.add(newSimulation)
        }

        val buttonHistory: Button = findViewById(R.id.history)
        buttonHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("history", history)
            startActivity(intent)
        }
    }

    private fun validate(inputText: String) {
        if (inputText.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese datos correctos", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun iterationsNumber(averageHours: Int): Int {
        return kotlin.math.ceil(averageHours.toDouble() / Constant.AVERAGE_HOURS).toInt()
    }
}
