package com.oblessing.mediapark.screens.filterDate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.airbnb.mvrx.withState
import com.oblessing.mediapark.R
import com.oblessing.mediapark.core.setDebounceOnClick
import com.oblessing.mediapark.databinding.ActivityFilterBinding
import com.oblessing.mediapark.model.SearchCriteria
import com.oblessing.mediapark.screens.filterDate.state.FilterState
import com.oblessing.mediapark.contracts.GetFilterOptionsContract
import com.oblessing.mediapark.contracts.GetSearchInOptionsContract
import com.oblessing.mediapark.views.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterActivity : AppCompatActivity(), MavericksView {
    private val viewModel: FilterViewModel by viewModel()
    private var _bindings: ActivityFilterBinding? = null
    private val bindings: ActivityFilterBinding
        get() = _bindings ?: error("Bindings: should not be null")

    private val searchCriteria: SearchCriteria by lazy {
        intent.getSerializableExtra(GetFilterOptionsContract.key) as? SearchCriteria
            ?: error("SearchCriteria: should be passed into this activity")
    }

    private val getSearchInFilterOptions = registerForActivityResult(GetSearchInOptionsContract()) {
        it?.let {
            postEvent(FilterState.Event.UserChangeSearchIn(it.searchIn.split(",").filter { it.isNotBlank() }))
        }
    }

    private fun postEvent(e: FilterState.Event) {
        viewModel.postEvent(e)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bindings = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(bindings.root)

        bindings.backButton.setDebounceOnClick {
            onBackPressed()
        }

        bindings.clearButton.setDebounceOnClick {
            postEvent(FilterState.Event.UserTappedClearDate)
        }

        bindings.actionApply.setDebounceOnClick {
            withState(viewModel) {
                setResult(
                    RESULT_OK,
                    Intent().apply { putExtra(GetFilterOptionsContract.key, it.currentCriteria) })
                finish()
            }
        }

        postEvent(FilterState.Event.UserDefinedCriteria(searchCriteria))

        viewModel.onEach {
            bindings.recyclerView.withModels {
                titleRow {
                    id("title")
                    text(R.string.label_filter)
                }

                subTitleRow {
                    id("sub-title")
                    text(R.string.label_date)
                }

                dateRow {
                    id("from")
                    text(R.string.label_from)
                    updateWithData(it.currentCriteria.from)
                    callback(object : DateRow.Callback {
                        override fun onDateSelected(date: String) {
                            postEvent(FilterState.Event.UserChangedDateFrom(date))
                        }
                    })
                }

                dateRow {
                    id("to")
                    text(R.string.label_to)
                    updateWithData(it.currentCriteria.to)
                    callback(object : DateRow.Callback {
                        override fun onDateSelected(date: String) {
                            postEvent(FilterState.Event.UserChangedDateTo(date))
                        }
                    })
                }

                searchInRow {
                    id("options")
                    options(it.currentCriteria.searchIn.split(","))
                    clickListener { _ -> openSearchIn() }
                }
            }
        }
    }

    private fun openSearchIn() {
        withState(viewModel) {
            getSearchInFilterOptions.launch(it.currentCriteria)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _bindings = null
    }

    override fun invalidate() = Unit
}