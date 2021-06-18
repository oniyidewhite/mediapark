package com.oblessing.mediapark.screens.filterDate

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.oblessing.mediapark.core.support.AssistedViewModelFactory
import com.oblessing.mediapark.core.support.hiltMavericksViewModelFactory
import com.oblessing.mediapark.screens.filterDate.state.FilterState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class FilterViewModel @AssistedInject constructor(
    @Assisted state: FilterState,
) : MavericksViewModel<FilterState>(state) {
    fun postEvent(event: FilterState.Event) {
        setState { reduce(event) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<FilterViewModel, FilterState> {
        override fun create(state: FilterState): FilterViewModel
    }

    companion object :
        MavericksViewModelFactory<FilterViewModel, FilterState> by hiltMavericksViewModelFactory()
}