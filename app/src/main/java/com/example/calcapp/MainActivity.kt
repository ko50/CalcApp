package com.example.calcapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var formula: String
    var result: String

    init {
        formula = "0"
        result = "0"
    }

    private fun constructingArrayOfComponentsByFormula(): Array<String> {
        val arrayOfComponents: Array<String> = arrayOf()
        for(s in formula) {
            remakeFormulaComponent(s, arrayOfComponents.last())
        }

        return arrayOfComponents
    }

    private fun remakeFormulaComponent(s: Char, beforeComponent: String): String {
        return when {
            s.isIntChar() || s == '.' ||
                    beforeComponent.isNumStr() -> beforeComponent + s
            else                               -> s.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: android.view.View) {
        val inputtedStr: String = findViewById<Button>(view.id).text.toString()
        when {
            inputtedStr == "AC"    -> allClear()
            inputtedStr == "="     -> executeFormula()
            inputtedStr == "."     -> addPoint()
            inputtedStr == "0"     -> addZero()
            inputtedStr.isNumStr() -> addNumber(inputtedStr)
            else                   -> addSymbol(inputtedStr)
        }
    }

    private fun allClear() {
        formula = "0"
        result  = "0"
        val formulaArea = findViewById<TextView>(R.id.formula)
        formulaArea.text = formula
        val resultArea = findViewById<TextView>(R.id.result)
        resultArea.text = result
    }

    private fun executeFormula() {
        val readyToCalcComponents: Array<String> = constructingArrayOfComponentsByFormula()
        var frontNum: Double? = null
        var symbol:   String? = null
        var rearNum:  Double? = null

        for(component in readyToCalcComponents) {
            when {
                component.isNumStr() -> when {
                    frontNum == null -> frontNum = component.toDouble()
                    rearNum  == null -> rearNum  = component.toDouble()
                }
                else                 -> symbol = component
            }

            if (frontNum == null || symbol == null || rearNum == null) continue

            frontNum = when (symbol) {
                "÷" -> frontNum / rearNum
                "×" -> frontNum * rearNum
                "-" -> frontNum - rearNum
                "+" -> frontNum + rearNum

                // ここのnullはsymbolをクラス化してなんやかんやすれば取り除けたり
                else -> null
            }
        }

        val resultArea = findViewById<TextView>(R.id.result)
        result = frontNum!!.toString()
        resultArea.text = result

        val formulaArea = findViewById<TextView>(R.id.formula)
        formula = "0"
        formulaArea.text = formula
    }

    private fun addPoint() {
        if(!formula.last().isIntChar()) return

        val formulaArea = findViewById<TextView>(R.id.formula)
        formula += "."
        formulaArea.text = formula
    }

    private fun addZero() {
        if(formula == "0") return

        val formulaArea = findViewById<TextView>(R.id.formula)
        formula += "0"
        formulaArea.text = formula
    }

    private fun addNumber(inputtedNum: String) {
        val formulaArea = findViewById<TextView>(R.id.formula)
        formula += inputtedNum
        formulaArea.text = formula
    }

    private fun addSymbol(inputtedSymbol: String) {
        if(!formula.last().isIntChar()) return

        val formulaArea = findViewById<TextView>(R.id.formula)
        formula += inputtedSymbol
        formulaArea.text = formula
    }
}

fun Char.isIntChar(): Boolean = this.toString().toIntOrNull() != null

fun String.isNumStr(): Boolean = this.toIntOrNull() != null ||
        (this.toDoubleOrNull() != null && this.contains('.'))
