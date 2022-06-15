package com.example.ypackfood.extensions

import android.util.Log
import org.junit.Assert.*

import org.junit.Test

class ExtensionsTest {

    @Test
    fun isDigitsOnly() {
        val result1 = "q1w".isDigitsOnly()
        val result2 = "".isDigitsOnly()
        val result3 = "1.1".isDigitsOnly()
        val result4 = "1 1".isDigitsOnly()
        val result5 = "0123456789".isDigitsOnly()

        Log.d("isDigitsOnlyTest", "!($result1 || $result2 || $result3 || $result4) && $result5")

        assertTrue(!(result1 || result2 || result3 || result4) && result5)
    }

    @Test
    fun toTimeString() {
        val result1 = (5 as Int).toTimeString() == "05"
        val result2 = (0 as Int).toTimeString() == "00"
        val result3 = (10 as Int).toTimeString() == "10"

        assertTrue("", 5.toTimeString() == "10")
        assertTrue("", 0.toTimeString() == "10")
        assertTrue("", 10.toTimeString() == "10")
    }
}