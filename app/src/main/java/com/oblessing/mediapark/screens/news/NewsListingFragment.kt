package com.oblessing.mediapark.screens.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.snackbar.Snackbar
import com.oblessing.mediapark.R
import com.oblessing.mediapark.core.ArticleUtil
import com.oblessing.mediapark.core.SnackbarUtil
import com.oblessing.mediapark.core.mavericks.viewBinding
import com.oblessing.mediapark.core.openUrlInTab
import com.oblessing.mediapark.core.showIf
import com.oblessing.mediapark.databinding.FragmentNewsListingBinding
import com.oblessing.mediapark.model.Article
import com.oblessing.mediapark.screens.news.state.NewListingState
import com.oblessing.mediapark.views.articleRow
import com.oblessing.mediapark.views.titleRow

class NewsListingFragment : Fragment(R.layout.fragment_news_listing), MavericksView {
    private val binding: FragmentNewsListingBinding by viewBinding()
    private val viewModel: NewsListingViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onEach(NewListingState::effect) {
            when(it) {
                NewListingState.Effect.ShowError -> SnackbarUtil.showSnackBarMessage(binding.root) {
                    viewModel.postEvent(NewListingState.Event.UserTappedRetry)
                }
                else -> Unit
            }
        }
    }

    override fun invalidate() = withState(viewModel) { state ->
        binding.recyclerView.withModels{
            titleRow {
                id("now-news")
                text(R.string.label_news)
            }

            binding.progress.showIf(state.showProgress)

            state.articles?.forEach {
                articleRow {
                    id(it.contentUrl)
                    data(it)
                    clickListener { _ -> ArticleUtil.openArticle(context, it) }
                }
            }
        }
    }
}