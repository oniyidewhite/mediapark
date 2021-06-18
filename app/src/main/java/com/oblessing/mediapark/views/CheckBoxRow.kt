package com.oblessing.mediapark.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.StringRes
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.oblessing.mediapark.core.mavericks.viewBinding
import com.oblessing.mediapark.core.setDebounceOnClick
import com.oblessing.mediapark.databinding.RowCheckBoxBinding
import java.util.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CheckBoxRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: RowCheckBoxBinding by viewBinding()
    private var callback: Callback? = null

    @ModelProp
    fun setOptions(value: Data) {
        binding.materialCheckBox.setText(value.title)
        val s = binding.materialCheckBox.text.toString().toLowerCase(Locale.ROOT)
        binding.materialCheckBox.isChecked = s in value.values

        binding.materialCheckBox.setOnCheckedChangeListener { _, v ->
            callback?.onCheckChanged(v)
        }
    }

    @CallbackProp
    fun setCheckChange(callback: Callback?) {
        this.callback = callback
    }

    data class Data(@StringRes val title: Int, val values: List<String>)

    interface Callback {
        fun onCheckChanged(v: Boolean)
    }
}