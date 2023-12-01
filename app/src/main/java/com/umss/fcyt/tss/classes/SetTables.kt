package com.umss.fcyt.tss.classes

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.umss.fcyt.tss.MainActivity

class SetTables {

    fun addHeaders(s: String, s1: String, s2: String, table: TableLayout, window: MainActivity) {
        val headerRow = TableRow(window)

        val headerColumn1 = TextView(window)
        headerColumn1.text = s
        headerColumn1.setTextColor(Color.rgb(248, 248, 248))
        headerColumn1.setTypeface(null, Typeface.BOLD)
        headerRow.addView(headerColumn1)

        val headerColumn2 = TextView(window)
        headerColumn2.text = s1
        headerColumn2.setTextColor(Color.rgb(248, 248, 248))
        headerColumn2.setTypeface(null, Typeface.BOLD)
        headerRow.addView(headerColumn2)

        val headerColumn3 = TextView(window)
        headerColumn3.text = s2
        headerColumn3.setTextColor(Color.rgb(248, 248, 248))
        headerColumn3.setTypeface(null, Typeface.BOLD)
        headerRow.addView(headerColumn3)

        table.addView(headerRow)
    }
    
    fun addRowToTableDouble(
        numbersOne: List<Double>,
        numbersTwo: List<Double>,
        table: TableLayout,
        window: MainActivity
    ) {
        for (index in numbersOne.indices) {
            val dataRow = TableRow(window)

            val dataColumn1 = TextView(window)
            (index + 1).toString().also { dataColumn1.text = it }
            dataColumn1.setTextColor(Color.rgb(214, 214, 214))
            dataRow.addView(dataColumn1)

            val dataColumn2 = TextView(window)
            dataColumn2.text = numbersOne[index].toString()
            dataColumn2.setTextColor(Color.rgb(214, 214, 214))
            dataRow.addView(dataColumn2)

            val dataColumn3 = TextView(window)
            dataColumn3.text = numbersTwo[index].toString()
            dataColumn3.setTextColor(Color.rgb(214, 214, 214))
            dataRow.addView(dataColumn3)

            table.addView(dataRow)
        }
    }

    @SuppressLint("SetTextI18n")
    fun addRowToTableInt(numbersOne: List<Int>, numbersTwo: List<Int>, table: TableLayout, window: MainActivity) {
        for (index in numbersOne.indices) {
            val dataRow = TableRow(window)

            val dataColumn1 = TextView(window)
            dataColumn1.text = (index + 1).toString()
            dataColumn1.setTextColor(Color.rgb(214, 214, 214))
            dataRow.addView(dataColumn1)

            val dataColumn2 = TextView(window)
            dataColumn2.text = numbersOne[index].toString() + " hrs"
            if (numbersOne[index] > 700 || numbersOne[index] < 500) {
                dataColumn2.setTextColor(Color.RED)
            } else {
                dataColumn2.setTextColor(Color.rgb(214, 214, 214))
            }
            dataRow.addView(dataColumn2)

            val dataColumn3 = TextView(window)
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