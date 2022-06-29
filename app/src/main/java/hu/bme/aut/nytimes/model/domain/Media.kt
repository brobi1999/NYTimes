package hu.bme.aut.nytimes.model.domain

import com.google.gson.annotations.SerializedName


data class Media (
  @SerializedName("media-metadata"           ) var mediaMetadata         : ArrayList<MediaMetadata> = arrayListOf()
)