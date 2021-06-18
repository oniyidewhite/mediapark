package com.oblessing.mediapark.screens.search

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.oblessing.mediapark.R
import com.oblessing.mediapark.contracts.GetFilterOptionsContract
import com.oblessing.mediapark.core.*
import com.oblessing.mediapark.core.mavericks.viewBinding
import com.oblessing.mediapark.databinding.FragmentSearchBinding
import com.oblessing.mediapark.model.Article
import com.oblessing.mediapark.model.SearchCriteria
import com.oblessing.mediapark.screens.search.state.SearchState
import com.oblessing.mediapark.views.articleRow
import com.oblessing.mediapark.views.subTitleRow
import com.oblessing.mediapark.views.titleRow

class SearchFragment : Fragment(R.layout.fragment_search), MavericksView {
    private val binding: FragmentSearchBinding by viewBinding()
    private val viewModel: SearchViewModel by activityViewModel()

    private val getFilterOptions = registerForActivityResult(GetFilterOptionsContract()) {
        it?.let {
            postEvent(SearchState.Event.UpdatedUsingCriteria(it))
        }
    }

    private val sortByBottomSheet by lazy {
        // TODO: Extract to BottomSheetUtil
        BottomSheetDialog(requireContext()).apply {
            withState(viewModel) {
                setContentView(R.layout.sheet_sort_by_type)
                findViewById<View>(R.id.sortByDate)?.setDebounceOnClick {
                    postEvent(SearchState.Event.UpdatedSortBy(SearchCriteria.sortedByDate))
                    dismiss()
                }

                findViewById<View>(R.id.sortByRelevance)?.setDebounceOnClick {
                    postEvent(SearchState.Event.UpdatedSortBy(SearchCriteria.sortedByRelevance))
                    dismiss()
                }

                // update with current selection

                when (it.criteria.sortedBy) {
                    SearchCriteria.sortedByDate -> (findViewById<View>(R.id.sortByDate) as? RadioButton)?.isChecked =
                        true
                    SearchCriteria.sortedByRelevance -> (findViewById<View>(R.id.sortByRelevance) as? RadioButton)?.isChecked =
                        true
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sortBy.setOnClickListener {
            sortByBottomSheet.show()
        }

        binding.filter.setDebounceOnClick {
            withState(viewModel) {
                getFilterOptions.launch(it.criteria)
            }
        }

        binding.input.watcherWithDebounce {
            postEvent(SearchState.Event.UserEnteredKeyword(it))
        }

        viewModel.onEach(SearchState::effect) {
            when(it) {
                is SearchState.Effect.ShowError -> {
                    SnackbarUtil.showSnackBarMessage(binding.root) {
                        postEvent(SearchState.Event.UserTappedRetry)
                    }
                    postEvent(SearchState.Event.HandledEffect)
                }
                is SearchState.Effect.UpdateSearchUi -> {
                    binding.input.setText(it.value)
                    postEvent(SearchState.Event.HandledEffect)
                }
                else -> Unit
            }
        }
    }

    private fun postEvent(event: SearchState.Event) {
        viewModel.postEvent(event)
    }

    override fun invalidate() = withState(viewModel) { state ->
        binding.recyclerView.withModels {
            binding.progress.showIf(state.showProgress)
            updateBadge(state.criteria.badgeCount)

            if (state.showSearchHistory) {
                displaySearchHistory(state.history)
            } else {
                displaySearchResult(state.results)
            }
        }
    }

    private fun updateBadge(badgeCount: Int) {
        binding.filterOptionsCounter.showIf(badgeCount != 0)
        binding.filterOptionsCounter.text = "$badgeCount"
    }

    private fun EpoxyController.displaySearchHistory(history: List<String>) {
        titleRow {
            id("history")
            text(R.string.label_history)
        }
        history.forEach {
            subTitleRow {
                id(it)
                text(it)
                clickListener { _ -> postEvent(SearchState.Event.UserSelectedKeyword(it)) }
            }
        }
    }

    private fun EpoxyController.displaySearchResult(results: List<Article>?) {
        results?.apply {
            if (isNotEmpty()) titleRow {
                id("history")
                text("${results.count()} news")
            }

            results.forEach {
                articleRow {
                    id(it.contentUrl)
                    data(it)
                    clickListener { _ -> ArticleUtil.openArticle(context, it) }
                }
            }
        }
    }
}