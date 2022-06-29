package hu.bme.aut.nytimes.ui.detail

import hu.bme.aut.nytimes.network.NYTimesRepo
import javax.inject.Inject

class DetailPresenter @Inject constructor(
    private val nyTimesRepo: NYTimesRepo
) {
}