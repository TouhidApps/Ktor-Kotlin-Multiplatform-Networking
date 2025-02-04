package com.touhidapps.ktor.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.collections.arrayListOf


@Serializable
data class FlowersModel (

    @SerialName("myJsonObject") var myJsonObject : ArrayList<FlowerModel> = arrayListOf(),

)

@Serializable
data class FlowerModel (

    @SerialName("id"            ) var id          : Int? = null,
    @SerialName("isActive"      ) var isActive    : Boolean? = null,
    @SerialName("name"          ) var name        : String? = null,
    @SerialName("details"       ) var details     : String? = null,
    @SerialName("imgData"       ) var imgData     : ImageData? = null,
    @SerialName("attributes"    ) var attributes  : ArrayList<String>? = arrayListOf()

)

@Serializable
data class ImageData (

    @SerialName("fileName") var fileName : String? = null,
    @SerialName("baseUrl") var baseUrl : String? = null,

)
