package com.oblessing.mediapark.screens.news

import com.airbnb.mvrx.*
import com.oblessing.mediapark.core.support.AssistedViewModelFactory
import com.oblessing.mediapark.core.support.hiltMavericksViewModelFactory
import com.oblessing.mediapark.repository.ArticleRepository
import com.oblessing.mediapark.screens.news.state.NewListingState
import com.oblessing.mediapark.screens.news.state.NewListingState.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect

class NewsListingViewModel
@AssistedInject constructor(
    @Assisted state: NewListingState,
    private val repository: ArticleRepository,
) : MavericksViewModel<NewListingState>(state) {
    init {
        onEach(NewListingState::effect) { e ->
            when (e) {
                Effect.RequestPage -> {
                    loadContent()
                }
                else -> Unit
            }
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<NewsListingViewModel, NewListingState> {
        override fun create(state: NewListingState): NewsListingViewModel
    }

    fun postEvent(event: Event) {
        setState { reduce(event) }
    }

    private fun loadContent() {
        repository.fetchArticles().execute(IO) { async ->
            when(async) {
                is Success -> {
                    val data = async()
                    reduce(data?.let {
                            Event.RequestWasSuccessful(it)
                    } ?: Event.RequestFailed)
                }
                is Fail -> reduce(Event.RequestFailed)
                else -> this
            }
        }

        postEvent(Event.HandledEffect)
    }

    companion object :
        MavericksViewModelFactory<NewsListingViewModel, NewListingState> by hiltMavericksViewModelFactory()
}