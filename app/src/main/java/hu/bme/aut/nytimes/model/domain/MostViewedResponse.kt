package hu.bme.aut.nytimes.model.domain

import com.google.gson.annotations.SerializedName


data class MostViewedResponse (
  @SerializedName("results"     ) var results    : ArrayList<Results> = arrayListOf()

)