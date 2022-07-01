package hu.bme.aut.nytimes.ui.detail

import hu.bme.aut.nytimes.model.ui.Article

data class DetailViewState(
    var article: Article?,
    var isInitial: Boolean,
    var isLoading: Boolean,
    var isError: Boolean,
    var isNetworkLost: Boolean
)