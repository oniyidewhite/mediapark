package com.oblessing.mediapark.screens.search

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.oblessing.mediapark.core.support.AssistedViewModelFactory
import com.oblessing.mediapark.core.support.hiltMavericksViewModelFactory
import com.oblessing.mediapark.database.SearchDao
import com.oblessing.mediapark.model.SearchCriteria
import com.oblessing.mediapark.repository.ArticleRepository
import com.oblessing.mediapark.screens.news.state.NewListingState
import com.oblessing.mediapark.screens.search.state.SearchState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SearchViewModel
@AssistedInject constructor(
    @Assisted state: SearchState,
    private val repository: ArticleRepository,
) : MavericksViewModel<SearchState>(state) {
    init {
        onEach(SearchState::effect) { e ->
            when (e) {
                SearchState.Effect.SubmitRequest -> {
                    loadContent()
                }
                else -> Unit
            }
        }

        repository.savedKeywords().execute(IO) { async ->
            reduce(SearchState.Event.RetrievedSearchHistory(async() ?: emptyList()))
        }
    }

    private fun loadContent() {
        withState { state ->
            repository.findArticles(state.criteria).execute(Dispatchers.IO) { async ->
                when (async) {
                    is Success -> {
                        val data = async()
                        reduce(data?.let {
                            // save to db
                            saveToDb(criteria)
                            SearchState.Event.LoadedArticles(it)
                        } ?: SearchState.Event.RequestFailed)
                    }
                    is Fail -> reduce(SearchState.Event.RequestFailed)
                    else -> this
                }
            }

            postEvent(SearchState.Event.HandledEffect)
        }
    }

    private fun saveToDb(criteria: SearchCriteria) {
        viewModelScope.launch(IO) {
            repository.saveKeyword(criteria)
        }
    }

    fun postEvent(event: SearchState.Event) {
        setState { reduce(event) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<SearchViewModel, SearchState> {
        override fun create(state: SearchState): SearchViewModel
    }

    companion object :
        MavericksViewModelFactory<SearchViewModel, SearchState> by hiltMavericksViewModelFactory()
}