package com.oblessing.mediapark.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.oblessing.mediapark.core.mavericks.viewBinding
import com.oblessing.mediapark.databinding.RowTitleBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TitleRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: RowTitleBinding by viewBinding()

    @TextProp
    @JvmOverloads
    fun setText(title: CharSequence = "") {
        binding.textView.text = title
    }
}