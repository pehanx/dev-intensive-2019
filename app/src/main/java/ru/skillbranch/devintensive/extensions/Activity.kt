package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager


fun Activity.hideKeyboard(){
//    currentFocus ?: View(this)
    val inputManager:InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}
fun Activity.isKeyboardOpen():Boolean {

    val r = Rect()
    val rootView = this.window.decorView
    rootView.getWindowVisibleDisplayFrame(r)

    val screenHeight: Int = rootView.rootView.height
    val heightDiff = screenHeight - (r.bottom - r.top)

    return heightDiff > screenHeight / 3
}

fun Activity.isKeyboardClosed(): Boolean {
    val r = Rect()
    val rootView = this.window.decorView
    rootView.getWindowVisibleDisplayFrame(r)

    val screenHeight: Int = rootView.rootView.height
    val heightDiff = screenHeight - (r.bottom - r.top)
    return heightDiff <= screenHeight / 3
}

