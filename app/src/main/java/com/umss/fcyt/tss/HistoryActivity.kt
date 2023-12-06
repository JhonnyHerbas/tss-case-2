package com.umss.fcyt.tss

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.umss.fcyt.tss.classes.Simulation

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val history = intent.getSerializableExtra("history") as? ArrayList<Simulation>
        val title: TextView = findViewById(R.id.title)
        val table: TableLayout = findViewById(R.id.tableExecute)
        addHeaders(table, this)
        if (history != null) {
            if (history.size > 0) {
                title.text = "Historial de simulaciones"
                for (simulation in history) {
                    addSimulationRow(simulation, table, this)
                }
            } else {
                title.text = "No hay historial disponible"
            }
        } else {
            title.text = "Error: No se pudo obtener el historial."
        }
    }

    private fun addHeaders(table: TableLayout, window: HistoryActivity) {
        val headerRow = TableRow(window)

        val headerColumn1 = TextView(window)
        headerColumn1.text = "Horas Sim"
        headerColumn1.setTextColor(Color.rgb(248, 248, 248))
        headerColumn1.setTypeface(null, Typeface.BOLD)
        headerRow.addView(headerColumn1)

        val headerColumn2 = TextView(window)
        headerColumn2.text = "Precio hora "
        headerColumn2.setTextColor(Color.rgb(248, 248, 248))
        headerColumn2.setTypeface(null, Typeface.BOLD)
        headerRow.addView(headerColumn2)

        val headerColumn3 = TextView(window)
        headerColumn3.text = "Precio repuesto"
        headerColumn3.setTextColor(Color.rgb(248, 248, 248))
        headerColumn3.setTypeface(null, Typeface.BOLD)
        headerRow.addView(headerColumn3)

        val headerColumn4 = TextView(window)
        headerColumn4.text = "Costo Pol.1"
        headerColumn4.setTextColor(Color.rgb(248, 248, 248))
        headerColumn4.setTypeface(null, Typeface.BOLD)
        headerRow.addView(headerColumn4)

        val headerColumn5 = TextView(window)
        headerColumn5.text = "Costo Pol.2"
        headerColumn5.setTextColor(Color.rgb(248, 248, 248))
        headerColumn5.setTypeface(null, Typeface.BOLD)
        headerRow.addView(headerColumn5)

        val headerColumn6 = TextView(window)
        headerColumn6.text = "Mejor Opcion"
        headerColumn6.setTextColor(Color.rgb(248, 248, 248))
        headerColumn6.setTypeface(null, Typeface.BOLD)
        headerRow.addView(headerColumn6)

        table.addView(headerRow)
    }

    private fun addSimulationRow(simulation: Simulation, table: TableLayout, window: HistoryActivity) {
        val dataRow = TableRow(window)

        val dataColumn1 = TextView(window)
        dataColumn1.text = simulation.simulationHours.toString()
        dataColumn1.setTextColor(Color.rgb(214, 214, 214))
        dataRow.addView(dataColumn1)

        val dataColumn2 = TextView(window)
        dataColumn2.text = simulation.disconnectionPrice.toString()
        dataColumn2.setTextColor(Color.rgb(214, 214, 214))
        dataRow.addView(dataColumn2)

        val dataColumn3 = TextView(window)
        dataColumn3.text = simulation.replacementPrice.toString()
        dataColumn3.setTextColor(Color.rgb(214, 214, 214))
        dataRow.addView(dataColumn3)

        val dataColumn4 = TextView(window)
        dataColumn4.text = simulation.policyCost1.toString()
        dataColumn4.setTextColor(Color.rgb(214, 214, 214))
        dataRow.addView(dataColumn4)

        val dataColumn5 = TextView(window)
        dataColumn5.text = simulation.policyCost2.toString()
        dataColumn5.setTextColor(Color.rgb(214, 214, 214))
        dataRow.addView(dataColumn5)

        val dataColumn6 = TextView(window)
        dataColumn6.text = simulation.bestOption.toString()
        dataColumn6.setTextColor(Color.rgb(214, 214, 214))
        dataRow.addView(dataColumn6)

        table.addView(dataRow)
    }
}