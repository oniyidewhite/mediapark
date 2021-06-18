package com.oblessing.mediapark.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.oblessing.mediapark.core.mavericks.viewBinding
import com.oblessing.mediapark.core.setDebounceOnClick
import com.oblessing.mediapark.databinding.RowSearchInBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SearchInRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: RowSearchInBinding by viewBinding()

    @ModelProp
    fun setOptions(values: List<String>?) {
        binding.options.text = values?.joinToString()
    }

    @CallbackProp
    fun setClickListener(listener: OnClickListener?) {
        setDebounceOnClick {
            listener?.onClick(this)
        }
    }
}