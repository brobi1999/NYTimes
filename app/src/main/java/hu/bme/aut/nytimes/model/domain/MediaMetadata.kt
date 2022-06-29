package hu.bme.aut.nytimes.model.domain

import com.google.gson.annotations.SerializedName


data class MediaMetadata (
  @SerializedName("url"    ) var url    : String? = null,

)