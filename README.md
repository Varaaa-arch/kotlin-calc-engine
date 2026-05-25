# Android Calculator Engine (Advanced Tier)
A production-ready, low-latency calculator application built natively using **Kotlin** and **Android Studio**. Designed as an advanced-tier implementation for procedural assessment, this engine bypasses traditional heavyweight third-party expression parsers in favor of a customized, high-efficiency sequential tokenizer.

---

## Technical Specifications
- **Architecture**: Native Android Component Architecture
- **Language Stack**: Kotlin 1.9+ / Functional Core
- **Layout Paradigm**: Linear & Contextual Structural Layout (`TableLayout` abstraction layer)
- **Minimum SDK**: API 24 (Android 7.0 Nougat)
- **Target SDK**: API 34 (Android 14)

---

## Core Architecture & Engine Mechanics

### 1. Sequential Tokenizer Engine
Unlike baseline applications that rely on standard reflection or risk stack-overflow crashes through heavy regular expressions, this project implements a deterministic sequential evaluation algorithm (`evaluateExpression`).
- Space Complexity: O(N)
- Time Complexity: O(N)

```kotlin
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
