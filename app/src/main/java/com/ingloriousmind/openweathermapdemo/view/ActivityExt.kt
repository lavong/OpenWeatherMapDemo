package com.ingloriousmind.openweathermapdemo.view

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideSoftKeyboard(view: View? = currentFocus) {
    view?.let { inputMethodManager().hideSoftInputFromWindow(view.windowToken, 0) }
}

fun Activity.inputMethodManager() = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
