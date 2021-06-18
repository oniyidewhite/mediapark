package com.oblessing.mediapark.screens.filter.state

import com.airbnb.mvrx.MavericksState
import com.oblessing.mediapark.model.SearchCriteria

data class FilterState(
    val currentCriteria: SearchCriteria = SearchCriteria(),
    val olderCriteria: SearchCriteria = SearchCriteria(),
    val effect: Effect? = null,
    val event: Event? = null
) : MavericksState {

    fun reduce(event: Event): FilterState {
        return when (event) {
            Event.HandledEffect -> copy(event = event, effect = null)
            is Event.UserChangeSearchIn -> copy(
                event = event,
                currentCriteria = currentCriteria.copy(searchIn = event.value.joinToString(","))
            )
            is Event.UserChangedDateFrom -> copy(
                event = event,
                currentCriteria = currentCriteria.copy(from = event.from)
            )
            is Event.UserChangedDateTo -> copy(
                event = event,
                currentCriteria = currentCriteria.copy(to = event.to)
            )
            is Event.UserDefinedCriteria -> copy(
                event = event,
                currentCriteria = event.criteria,
                olderCriteria = event.criteria
            )
            Event.UserTappedApply -> copy(event = event, effect = Effect.UserTappedApplyFilter)
            Event.UserTappedClearDate -> copy(
                event = event,
                currentCriteria = currentCriteria.copy(
                    from = olderCriteria.from,
                    to = olderCriteria.to
                )
            )
            Event.UserTappedClearSearchIn -> copy(
                event = event,
                currentCriteria = currentCriteria.copy(
                    searchIn = olderCriteria.searchIn,
                )
            )
        }
    }

    sealed class Event {
        data class UserDefinedCriteria(val criteria: SearchCriteria) : Event()
        data class UserChangedDateFrom(val from: String) : Event()
        data class UserChangedDateTo(val to: String) : Event()
        data class UserChangeSearchIn(val value: List<String>) : Event()

        object UserTappedClearDate : Event()
        object UserTappedClearSearchIn : Event()
        object UserTappedApply : Event()
        object HandledEffect : Event()
    }

    sealed class Effect {
        object UserTappedApplyFilter : Effect()
    }
}