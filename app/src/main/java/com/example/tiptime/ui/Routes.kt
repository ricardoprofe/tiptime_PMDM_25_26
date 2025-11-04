package com.example.tiptime.ui

import androidx.annotation.StringRes
import com.example.tiptime.R

enum class Routes(@StringRes val title: Int) {
    Start (title = R.string.app_name),
    EditTip (title = R.string.edit_tip),
    TipResult (title = R.string.tip_result)
}