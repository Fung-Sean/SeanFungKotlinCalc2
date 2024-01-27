package com.example.kotlincalc2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var canAddOperation = false
    private var canAddDecimal = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun backspace(view: View) { //Function for the go back one space.
        val formulaText = findViewById<TextView>(R.id.formulaText) //For reference, these go into the activity_main xml file and get the text from the textview.
        val resultsText = findViewById<TextView>(R.id.resultsText)
        val length = formulaText.length()
        if (length > 0){
            formulaText.text = formulaText.text.subSequence(0, length - 1)
        }
    }
    fun number(view: View) { //My function for clicking a number button on the calculator
        val formulaText = findViewById<TextView>(R.id.formulaText)
        val resultsText = findViewById<TextView>(R.id.resultsText)
        if (view is Button){
            if(view.text == "."){ //Interesting that you cannot do an and statement here
                if(canAddDecimal){
                    formulaText.append(view.text)
                    canAddDecimal = false
                }
            }
            else{
                formulaText.append(view.text)
                canAddOperation = true
            }
        }
    }
    fun operator(view: View) {
        val formulaText = findViewById<TextView>(R.id.formulaText)
        val resultsText = findViewById<TextView>(R.id.resultsText)
        if(view is Button && canAddOperation){
            formulaText.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }
    fun equalsAction(view: View) {
        val formulaText = findViewById<TextView>(R.id.formulaText)
        val resultsText = findViewById<TextView>(R.id.resultsText)
        resultsText.text = calculateResults()
    }

    fun allClear(view: View) {
        val formulaText = findViewById<TextView>(R.id.formulaText)
        val resultsText = findViewById<TextView>(R.id.resultsText)

        formulaText.text = ""
        resultsText.text = ""

    }
    private fun calculateResults(): String {
        val formulaText = findViewById<TextView>(R.id.formulaText)
        val resultsText = findViewById<TextView>(R.id.resultsText)
        val digitOperators = digitsOperators()
        if(digitOperators.isEmpty()){
            return ""
        }
        val timesDivision = timesDivisionCalculate(digitOperators)
        if (timesDivision.isEmpty()){
            return ""
        }
        val result = addSubbtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubbtractCalculate(timesDivision: MutableList<Any>): Float {
        var result = timesDivision[0] as Float

        for(i in timesDivision.indices){
            if(timesDivision[i] is Char && i != timesDivision.lastIndex){
                val operator = timesDivision[i]
                val nextDigit = timesDivision[i+1] as Float
                if(operator == '+'){
                    result += nextDigit
                }
                if(operator =='-'){
                    result -= nextDigit
                }
            }
        }
        return result
    }

    private fun timesDivisionCalculate(digitOperators: MutableList<Any>): MutableList<Any> {
        var list = digitOperators
        while (list.contains('x') || list.contains('/')){
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(list: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = list.size

        for( i in list.indices){
            if(list[i] is Char && i != list.lastIndex && i < restartIndex){
                val operator = list[i]
                val prevDigit = list[i - 1] as Float
                val nextDigit = list[i + 1] as Float
                when(operator){
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->{
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if(i>restartIndex){
                newList.add(list[i])
            }
        }
        return newList
    }

    private fun digitsOperators(): MutableList<Any>{
        val formulaText = findViewById<TextView>(R.id.formulaText)
        val resultsText = findViewById<TextView>(R.id.resultsText)
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in formulaText.text){
            if(character.isDigit() || character == '.'){
                currentDigit += character
            }
            else{
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if(currentDigit != ""){
            list.add(currentDigit.toFloat())
        }
        return list
    }
}