package com.oblessing.mediapark.core

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.oblessing.mediapark.R
import com.oblessing.mediapark.screens.news.state.NewListingState

object SnackbarUtil {
    fun showSnackBarMessage(view: View, callback: () -> Unit) {
        Snackbar.make(view, view.context.getString(R.string.error_message), Snackbar.LENGTH_INDEFINITE).apply {
            setAction(R.string.label_retry) {
               callback()
            }
        }.show()
    }
}