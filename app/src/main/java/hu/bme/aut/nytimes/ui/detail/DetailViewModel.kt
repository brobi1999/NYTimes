package hu.bme.aut.nytimes.ui.detail

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
) : RainbowCakeViewModel<DetailViewState>(Initial) {

}