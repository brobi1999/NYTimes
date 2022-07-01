package hu.bme.aut.nytimes.ui.list

import hu.bme.aut.nytimes.model.ui.Article
import hu.bme.aut.nytimes.util.Period

data class ListViewState(
    var articles: List<Article>,
    var isLoading: Boolean,
    var isInitial: Boolean,
    var period: Period
    )

