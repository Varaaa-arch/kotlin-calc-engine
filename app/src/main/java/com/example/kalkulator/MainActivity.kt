package com.example.kalkulator
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView

    private var lastNumeric: Boolean = false
    private var stateError: Boolean = false
    private var lastDot: Boolean = false
    private var memoryValue: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        setNumberListeners()
        setOperatorListeners()
        setMemoryListeners()
    }

    private fun setNumberListeners() {
        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        for (id in buttons) {
            findViewById<Button>(id).setOnClickListener { button ->
                if (stateError) {
                    tvResult.text = (button as Button).text
                    stateError = false
                } else {
                    if (tvResult.text.toString() == "0") {
                        tvResult.text = (button as Button).text
                    } else {
                        tvResult.append((button as Button).text)
                    }
                }
                lastNumeric = true
            }
        }

        findViewById<Button>(R.id.btnDot).setOnClickListener {
            if (lastNumeric && !stateError && !lastDot) {
                tvResult.append(".")
                lastNumeric = false
                lastDot = true
            }
        }
    }

    private fun setOperatorListeners() {
        // Logika Map disederhanakan pakai list pasang-pasangan biar ga ketuker tipenya di compiler
        val opButtons = listOf(
            Pair(R.id.btnPlus, " + "),
            Pair(R.id.btnMinus, " - "),
            Pair(R.id.btnMul, " * "),
            Pair(R.id.btnDiv, " / ")
        )

        for (pair in opButtons) {
            findViewById<Button>(pair.first).setOnClickListener {
                if (lastNumeric && !stateError) {
                    tvExpression.append(tvResult.text.toString() + pair.second)
                    tvResult.text = "0"
                    lastNumeric = false
                    lastDot = false
                }
            }
        }

        findViewById<Button>(R.id.btnPercent).setOnClickListener {
            if (lastNumeric && !stateError) {
                val value = tvResult.text.toString().toDouble() / 100
                tvResult.text = value.toString()
            }
        }

        findViewById<Button>(R.id.btnAC).setOnClickListener {
            tvExpression.text = ""
            tvResult.text = "0"
            lastNumeric = false
            stateError = false
            lastDot = false
        }

        findViewById<Button>(R.id.btnDel).setOnClickListener {
            val txt = tvResult.text.toString()
            if (txt.isNotEmpty() && txt != "0") {
                if (txt.length == 1) {
                    tvResult.text = "0"
                } else {
                    tvResult.text = txt.substring(0, txt.length - 1)
                }
            }
        }

        findViewById<Button>(R.id.btnEqual).setOnClickListener {
            onEqual()
        }
    }

    private fun setMemoryListeners() {
        findViewById<Button>(R.id.btnMC).setOnClickListener {
            memoryValue = 0.0
        }

        findViewById<Button>(R.id.btnMR).setOnClickListener {
            tvResult.text = if (memoryValue % 1 == 0.0) memoryValue.toInt().toString() else memoryValue.toString()
            lastNumeric = true
        }

        findViewById<Button>(R.id.btnMPlus).setOnClickListener {
            try {
                memoryValue += tvResult.text.toString().toDouble()
            } catch (e: Exception) {}
        }

        findViewById<Button>(R.id.btnMMinus).setOnClickListener {
            try {
                memoryValue -= tvResult.text.toString().toDouble()
            } catch (e: Exception) {}
        }
    }

    private fun onEqual() {
        if (lastNumeric && !stateError) {
            val fullExpression = tvExpression.text.toString() + tvResult.text.toString()
            tvExpression.text = ""

            try {
                val result = evaluateExpression(fullExpression)
                if (result.isInfinite() || result.isNaN()) {
                    tvResult.text = "Error: Div by 0"
                    stateError = true
                    lastNumeric = false
                } else {
                    if (result % 1 == 0.0) {
                        tvResult.text = result.toInt().toString()
                    } else {
                        tvResult.text = result.toString()
                    }
                    lastDot = tvResult.text.contains(".")
                }
            } catch (e: Exception) {
                tvResult.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }

    private fun evaluateExpression(expr: String): Double {
        val tokens = expr.trim().split("\\s+".toRegex())
        if (tokens.isEmpty()) return 0.0

        var result = tokens[0].toDouble()
        var i = 1
        while (i < tokens.size) {
            val op = tokens[i]
            val nextVal = tokens[i + 1].toDouble()

            when (op) {
                "+" -> result += nextVal
                "-" -> result -= nextVal
                "*" -> result *= nextVal
                "/" -> {
                    if (nextVal == 0.0) return Double.NEGATIVE_INFINITY
                    result /= nextVal
                }
            }
            i += 2
        }
        return result
    }
}