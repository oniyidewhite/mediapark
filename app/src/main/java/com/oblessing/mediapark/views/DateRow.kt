package com.oblessing.mediapark.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.Toast
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.oblessing.mediapark.core.DateUtil
import com.oblessing.mediapark.core.mavericks.viewBinding
import com.oblessing.mediapark.core.setDebounceOnClick
import com.oblessing.mediapark.core.watcherWithDebounce
import com.oblessing.mediapark.databinding.RowDateBinding
import com.oblessing.mediapark.databinding.RowSubTitleBinding
import com.oblessing.mediapark.model.SearchCriteria
import java.util.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DateRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: RowDateBinding by viewBinding()
    private var callback: Callback? = null

    @TextProp
    @JvmOverloads
    fun setText(title: CharSequence = "") {
        binding.title.text = title
        binding.input.watcherWithDebounce(800L) {
            // convert input to date
            val date = DateUtil.stringToIsoDate(it)
            if (it.isNotBlank() && date == SearchCriteria.dateNone) {
                binding.input.error =
                    "${title}: Is invalid, the value will be disregarded. Format is yyyy-mm-dd"
                return@watcherWithDebounce
            }
            binding.input.error = null
            callback?.onDateSelected(date)
        }
    }

    @ModelProp
    fun updateWithData(value: String?) {
        if (binding.input.text?.trim() == value?.trim()) return
        binding.input.setText(DateUtil.fromIsoToDate(value ?: ""))
    }

    @CallbackProp
    fun setCallback(callback: Callback?) {
        this.callback = callback
    }

    interface Callback {
        fun onDateSelected(date: String)
    }
}