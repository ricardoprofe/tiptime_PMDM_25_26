package com.example.tiptime.ui

import androidx.annotation.StringRes
import com.example.tiptime.R

enum class Routes(@StringRes val title: Int) {
    Start (title = R.string.app_name),
    TipResult (title = R.string.tip_result)
}