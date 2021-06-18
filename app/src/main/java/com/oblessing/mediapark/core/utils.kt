package com.oblessing.mediapark.core

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.oblessing.mediapark.BuildConfig
import com.oblessing.mediapark.R
import java.util.*
import kotlin.concurrent.timerTask

private val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

fun ImageView.loadUrl(string: String) {
    Glide.with(context)
        .load(string).transition(DrawableTransitionOptions.withCrossFade(factory))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun debug(action: () -> Unit) {
    if (BuildConfig.DEBUG) action()
}

fun Context.openUrlInTab(url: String) {
    val builder = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(
            CustomTabColorSchemeParams.Builder()
                .setToolbarColor(ContextCompat.getColor(this, R.color.teal_200))
                .setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.teal_700))
                .build()
        )
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
}

fun TextView.watcherWithDebounce(debounce: Long = 400L, callback: (String) -> Unit) {
    val handler = Looper.getMainLooper()?.let { Handler(it) }
    val watcher = object : TextWatcher {
        private var suggestionDelayTimer = Timer()

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

        override fun afterTextChanged(s: Editable?) {
            // confirm we have something
            if (s != null) {
                suggestionDelayTimer.cancel()
                suggestionDelayTimer = Timer().apply {
                    schedule(timerTask {
                        handler?.post {
                            callback(s.toString())
                        }
                    }, debounce)
                }
            }
        }
    }

    addTextChangedListener(watcher)
}

fun View.setDebounceOnClick(debounce: Long = 400L, callback: () -> Unit) {
    val handler = Looper.myLooper()?.let { Handler(it) }

    var delayTimer = Timer()
    setOnClickListener {
        delayTimer.cancel()
        delayTimer = Timer().apply {
            schedule(timerTask {
                handler?.post(callback)
            }, debounce)
        }
    }
}

fun View.showIf(shouldShow: Boolean) {
    visibility = if (shouldShow) View.VISIBLE else View.GONE
}

