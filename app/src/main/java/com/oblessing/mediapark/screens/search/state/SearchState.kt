package com.oblessing.mediapark.screens.search.state

import com.airbnb.mvrx.MavericksState
import com.oblessing.mediapark.model.Article
import com.oblessing.mediapark.model.SearchCriteria

data class SearchState(
    val criteria: SearchCriteria = SearchCriteria(),
    val history: List<String> = emptyList(),
    private val loading: Boolean = false,
    val results: List<Article>? = emptyList(),
    val effect: Effect? = null,
    val event: Event? = null,
) : MavericksState {
    val showProgress
        get() = loading

    val showSearchHistory
        get() = !loading && criteria.value.isBlank() && !history.isNullOrEmpty()

    fun reduce(event: Event): SearchState {
        return when (event) {
            is Event.UpdatedDateRange -> copy(
                event = event,
                criteria = criteria.copy(from = event.from, to = event.to),
                effect = if (criteria.value.isNotBlank()) Effect.SubmitRequest else effect,
                loading = if (criteria.value.isNotBlank()) true else loading
            )
            is Event.UpdatedSearchIn -> copy(
                event = event,
                criteria = criteria.copy(searchIn = event.value.joinToString(",")),
                effect = if (criteria.value.isNotBlank()) Effect.SubmitRequest else effect,
                loading = if (criteria.value.isNotBlank()) true else loading
            )
            is Event.UpdatedSortBy -> copy(
                event = event,
                criteria = criteria.copy(sortedBy = event.value),
                effect = if (criteria.value.isNotBlank()) Effect.SubmitRequest else effect,
                loading = if (criteria.value.isNotBlank()) true else loading
            )
            is Event.UserEnteredKeyword -> copy(
                event = event,
                criteria = criteria.copy(value = event.value),
                effect = if (event.value.isNotBlank()) Effect.SubmitRequest else effect,
                loading = if (event.value.isNotBlank()) true else loading
            )

            is Event.RetrievedSearchHistory -> copy(event = event, history = event.value)
            Event.HandledEffect -> copy(event = event, effect = null)
            Event.UserTappedRetry -> copy(
                event = event,
                effect = Effect.SubmitRequest,
                loading = true
            )
            Event.RequestFailed -> copy(event = event, effect = Effect.ShowError, loading = false)
            is Event.LoadedArticles -> copy(
                event = event,
                loading = false,
                results = event.articles
            )
            is Event.UserSelectedKeyword -> copy(
                event = event,
                criteria = criteria.copy(value = event.value),
                effect = if (event.value.isNotBlank()) Effect.UpdateSearchUi(event.value) else effect,
                loading = if (event.value.isNotBlank()) true else loading
            )
            is Event.UpdatedUsingCriteria -> copy(
                event = event,
                criteria = event.data,
                effect = if (criteria.value.isNotBlank()) Effect.SubmitRequest else effect,
                loading = if (criteria.value.isNotBlank()) true else loading
            )
        }
    }

    sealed class Event {
        data class UserSelectedKeyword(val value: String) : Event()
        data class UserEnteredKeyword(val value: String) : Event()
        data class RetrievedSearchHistory(val value: List<String>) : Event()
        data class UpdatedSortBy(val value: String) : Event()
        data class UpdatedDateRange(val from: String, val to: String) : Event()
        data class UpdatedSearchIn(val value: List<String>) : Event()
        data class LoadedArticles(val articles: List<Article>) : Event()
        data class UpdatedUsingCriteria(val data: SearchCriteria) : Event()

        object RequestFailed : Event()

        object UserTappedRetry : Event()
        object HandledEffect : Event()
    }

    sealed class Effect {
        object SubmitRequest : Effect()
        object ShowError : Effect()
        data class UpdateSearchUi(val value: String) : Effect()
    }
}