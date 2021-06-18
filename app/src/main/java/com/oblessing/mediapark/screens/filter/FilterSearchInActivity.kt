package com.oblessing.mediapark.screens.filter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.airbnb.mvrx.withState
import com.oblessing.mediapark.R
import com.oblessing.mediapark.core.setDebounceOnClick
import com.oblessing.mediapark.model.SearchCriteria
import com.oblessing.mediapark.screens.filter.state.FilterState
import com.oblessing.mediapark.contracts.GetSearchInOptionsContract
import com.oblessing.mediapark.databinding.ActivityFilterSearchInBinding
import com.oblessing.mediapark.views.CheckBoxRow
import com.oblessing.mediapark.views.checkBoxRow
import com.oblessing.mediapark.views.titleRow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterSearchInActivity : AppCompatActivity(), MavericksView {
    private val viewModel: FilterViewModel by viewModel()
    private var _bindings: ActivityFilterSearchInBinding? = null
    private val bindings: ActivityFilterSearchInBinding
        get() = _bindings ?: error("Bindings: should not be null")

    private val searchCriteria: SearchCriteria by lazy {
        intent.getSerializableExtra(GetSearchInOptionsContract.key) as? SearchCriteria
            ?: error("SearchCriteria: should be passed into this activity")
    }

    private fun postEvent(e: FilterState.Event) {
        viewModel.postEvent(e)
    }

    private fun updateAndPost(value: String, shouldAdd: Boolean) {
        withState(viewModel) {
            val options = it.currentCriteria.searchIn.split(",").toMutableList()
            if (shouldAdd) {
                options.add(value)
            } else {
                options.remove(value)
            }

            postEvent(FilterState.Event.UserChangeSearchIn(options))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bindings = ActivityFilterSearchInBinding.inflate(layoutInflater)
        setContentView(bindings.root)

        bindings.backButton.setDebounceOnClick {
            onBackPressed()
        }

        bindings.clearButton.setDebounceOnClick {
            postEvent(FilterState.Event.UserTappedClearSearchIn)
        }

        bindings.actionApply.setDebounceOnClick {
            withState(viewModel) {
                setResult(
                    RESULT_OK,
                    Intent().apply { putExtra(GetSearchInOptionsContract.key, it.currentCriteria) })
                finish()
            }
        }

        viewModel.onEach {
            val options = it.currentCriteria.searchIn.split(",")
            bindings.recyclerView.withModels {
                titleRow {
                    id("title")
                    text(R.string.label_search_in)
                }
                checkBoxRow {
                    id("option-title")
                    checkChange(object : CheckBoxRow.Callback{
                        override fun onCheckChanged(v: Boolean) {
                            updateAndPost(SearchCriteria.searchInTitle, v)
                        }
                    })
                    options(CheckBoxRow.Data(R.string.label_title, options))
                }

                checkBoxRow {
                    id("option-description")
                    options(CheckBoxRow.Data(R.string.label_description, options))
                    checkChange(object : CheckBoxRow.Callback{
                        override fun onCheckChanged(v: Boolean) {
                            updateAndPost(SearchCriteria.searchInDescription, v)
                        }
                    })
                }

                checkBoxRow {
                    id("option-content")
                    options(CheckBoxRow.Data(R.string.label_content, options))
                    checkChange(object : CheckBoxRow.Callback{
                        override fun onCheckChanged(v: Boolean) {
                            updateAndPost(SearchCriteria.searchInContent, v)
                        }
                    })
                }
            }
        }

        postEvent(FilterState.Event.UserDefinedCriteria(searchCriteria))
    }

    override fun onDestroy() {
        super.onDestroy()
        _bindings = null
    }

    override fun invalidate() = Unit
}