package com.example.tiptime.ui

import androidx.annotation.StringRes
import com.example.tiptime.R

enum class Routes(@StringRes val title: Int, val route: String) {
    Start(title = R.string.app_name, route = "Start"),
    EditTip(title = R.string.edit_tip, route = "EditTip"),
    TipResult(title = R.string.tip_result, route = "TipResult");

    companion object {
        const val TIP_ID_ARG = "tipId"
    }

    /**
     * The route pattern that accepts a tip id argument, e.g. "EditTip/{tipId}"
     */
    fun withArg(): String = "$route/{$TIP_ID_ARG}"

    /**
     * Build a concrete route for the given id, e.g. "EditTip/3"
     */
    fun createRouteFor(id: Int): String = "$route/$id"
}