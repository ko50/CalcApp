package com.example.calcapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var formula: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun addNumber(view: android.view.View) {
        val inputtedNumberString: String = findViewById<Button>(view.id).text.toString()
        formula += inputtedNumberString

        val formulaArea = findViewById<TextView>(R.id.formula)
        formulaArea.text = formula
    }

    fun addSymbol(view: android.view.View) {
        val inputtedSymbolString: String = findViewById<Button>(view.id).text.toString()
        val before: Int? = formula.last().toString().toIntOrNull()
        
    }
}