package com.oblessing.mediapark.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.oblessing.mediapark.core.mavericks.viewBinding
import com.oblessing.mediapark.core.setDebounceOnClick
import com.oblessing.mediapark.databinding.RowSubTitleBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SubTitleRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: RowSubTitleBinding by viewBinding()

    @TextProp
    @JvmOverloads
    fun setText(title: CharSequence = "") {
        binding.textView.text = title
    }

    @CallbackProp
    fun setClickListener(listener: OnClickListener?) {
        setDebounceOnClick {
            listener?.onClick(this)
        }
    }
}