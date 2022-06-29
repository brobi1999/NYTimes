package hu.bme.aut.nytimes.model.domain

import com.google.gson.annotations.SerializedName


data class Results (
  @SerializedName("uri"            ) var uri           : String?           = null,
  @SerializedName("url"            ) var url           : String?           = null,
  @SerializedName("published_date" ) var publishedDate : String?           = null,
  @SerializedName("byline"         ) var byline        : String?           = null,
  @SerializedName("title"          ) var title         : String?           = null,
  @SerializedName("media"          ) var media         : ArrayList<Media>  = arrayListOf(),

)