package com.oblessing.mediapark.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.oblessing.mediapark.core.loadUrl
import com.oblessing.mediapark.core.mavericks.viewBinding
import com.oblessing.mediapark.core.setDebounceOnClick
import com.oblessing.mediapark.databinding.RowArticleBinding
import com.oblessing.mediapark.model.Article

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ArticleRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: RowArticleBinding by viewBinding()

    @ModelProp
    fun setData(title: Article) {
        binding.titleText.text = title.title
        binding.subText.text = title.subTitle
        binding.imageView.loadUrl(title.imageUrl)
    }

    @CallbackProp
    fun setClickListener(listener: OnClickListener?) {
        setDebounceOnClick {
            listener?.onClick(this)
        }
    }
}