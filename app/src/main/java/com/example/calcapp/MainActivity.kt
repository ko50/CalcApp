package com.example.calcapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.graphics.Color

class MainActivity : AppCompatActivity() {
    private var formula: String
    private var result: String

    init {
        formula = "0"
        result = "0"
    }

    private fun showError(errorMessage: String) {
        val errorArea = findViewById<TextView>(R.id.error)
        errorArea.text = errorMessage
        errorArea.setTextColor(Color.RED)
    }

    private fun remakeFormula(): Array<String> {
        var sortedFormulaElements: Array<String> = arrayOf("")
        println(formula)
        for(s in formula) {
            val before: String = sortedFormulaElements.last() // OrNull() ?: continue

            when {
                (s.isIntChar() || s == '.') &&
                        before.isNumStr() -> sortedFormulaElements[sortedFormulaElements.lastIndex] = before + s
                else                      -> sortedFormulaElements += s.toString()
            }
        }

        sortedFormulaElements.forEach { println("$it \n") }

        return sortedFormulaElements.copyOfRange(1, sortedFormulaElements.lastIndex+1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: android.view.View) {
        val inputtedStr: String = findViewById<Button>(view.id).text.toString()
        println(inputtedStr)
        when(inputtedStr) {
            "AC" -> allClear()
            "="  -> executeFormula()
            "|←" -> backSpace()
            "."  -> addPoint()
            "0"  -> addZero()
            else -> {
                if(inputtedStr.isNumStr()) {
                    addNumber(inputtedStr)
                } else {
                    addSign(inputtedStr)
                }
            }
        }
    }

    private fun allClear() {
        val formulaArea = findViewById<TextView>(R.id.formula)
        formula = "0"
        formulaArea.text = formula
    }

    @SuppressLint("SetTextI18n")
    private fun executeFormula() {
        val readyToCalcComponents: Array<String> = remakeFormula()
        readyToCalcComponents.forEach { println("$it \n") }
        println("first comp: ${readyToCalcComponents.first()}")
        println("size: ${readyToCalcComponents.size}")

        if(!readyToCalcComponents.last().isNumStr()) {
            showError("Cannot Execute Formula That Dose Not End With Number.")
            return
        }
        println(readyToCalcComponents.last())

        var frontNum: Double = readyToCalcComponents[0].toDouble()
        var sign:   String
        var rearNum:  Double

        // 最後が記号で終わる式を弾かないとindex out of rangeが起きて世界が滅ぶ
        for(i in 0..readyToCalcComponents.size-2 step 2) {
            sign   = readyToCalcComponents[i + 1]
            rearNum  = readyToCalcComponents[i + 2].toDouble()


            when(sign) {
                "÷" -> frontNum /= rearNum
                "×" -> frontNum *= rearNum
                "-" -> frontNum -= rearNum
                "+" -> frontNum += rearNum
            }
        }

        result = frontNum.toString().removeSuffix(".0")

        val formulaArea = findViewById<TextView>(R.id.formula)
        formula = result
        formulaArea.text = formula

        val errorArea = findViewById<TextView>(R.id.error)
        errorArea.text = "The Formula Was Successfully Executed."
        errorArea.setTextColor(Color.BLACK)
    }

    private fun backSpace() {
        if(formula == "0") return

        formula = when(formula.lastIndex) {
            0    -> "0"
            else -> formula.removeSuffix(formula.last().toString())
        }

        val formulaArea = findViewById<TextView>(R.id.formula)
        formulaArea.text = formula
    }

    private fun addPoint() {
        if(!formula.last().isIntChar()) {
            showError("You Cannot Add Point To That.")
            return
        }

        println(formula.reversed())

        for(s in formula.reversed()) {
            println(s)
            if(s == '.') return
            else if(!s.toString().isNumStr()) break
        }

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
        if(formula == "0") formula = inputtedNum
        else               formula += inputtedNum
        formulaArea.text = formula
    }

    private fun addSign(inputtedSign: String) {
        if(!formula.last().isIntChar()) return

        val formulaArea = findViewById<TextView>(R.id.formula)
        formula += inputtedSign
        formulaArea.text = formula
    }
}

fun Char.isIntChar(): Boolean = this.toString().toIntOrNull() != null

fun String.isNumStr(): Boolean = this.toIntOrNull() != null ||
        this.toDoubleOrNull() != null