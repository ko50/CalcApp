package com.example.calcapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {
    var formula: String = ""
    var result:  String = ""

    private fun constructingArrayOfComponentsByFormula(): Array<String> {
        val arrayOfComponents: Array<String> = arrayOf()
        for(s in formula) {
            remakeFormulaComponent(s, arrayOfComponents.last())
        }

        arrayOfComponents.forEach {
            when {
                it.isDoubleStr() -> it.toDouble()
                it.isIntStr()    -> it.toInt()
            }
        }

        return arrayOfComponents
    }

    private fun remakeFormulaComponent(s: Char, beforeComponent: String): String {
        return when {
            s.isIntChar() || s == '.' ||
                    beforeComponent.isIntStr() -> beforeComponent + s
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
            inputtedStr.isIntStr() -> addNumber(inputtedStr)
            else                   -> addSymbol(inputtedStr)
        }
    }

    private fun allClear() {
        formula = ""
        result  = ""
        val formulaArea = findViewById<TextView>(R.id.formula)
        formulaArea.text = formula
        val resultArea = findViewById<TextView>(R.id.result)
        resultArea.text = result
    }

    private fun executeFormula() {
        val readyToCalcComponents: Array<String> = constructingArrayOfComponentsByFormula()
        for(component in readyToCalcComponents) {

        }
    }

    private fun addPoint() {
        val formulaArea = findViewById<TextView>(R.id.formula)
        formula += "."
        formulaArea.text = formula
    }

    private fun addZero() {
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
        val formulaArea = findViewById<TextView>(R.id.formula)
        val isThePreviousCharIsInt: Boolean = (formula.last().isIntChar())
        if(isThePreviousCharIsInt) {
            formula += inputtedSymbol
        }
        formulaArea.text = formula
    }
}

fun Char.isIntChar(): Boolean = this.toString().toIntOrNull() != null

fun String.isIntStr(): Boolean = this.toIntOrNull() != null
fun String.isDoubleStr(): Boolean = this.toDoubleOrNull() != null && this.contains('.')