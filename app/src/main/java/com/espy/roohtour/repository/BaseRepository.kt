@file:Suppress("unused")

package com.espy.roohtour.repository

abstract class BaseRepository {

    open val TAG = "BaseRepo"

    private var stringBuilder = StringBuilder()

    abstract fun onCleared()

    private fun clear() {
        stringBuilder.clear()
    }
}