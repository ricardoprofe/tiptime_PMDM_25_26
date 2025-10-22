package com.example.tiptime.viewmodels

data class TipTimeState (
    val amountInput: String = "",
    val tipInput: String = "15",
    val roundUp: Boolean = false,
    val tip: String = "",
    val total: String = ""
)