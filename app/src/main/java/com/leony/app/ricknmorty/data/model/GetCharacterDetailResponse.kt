package com.leony.app.ricknmorty.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class to map response retrieved from the server. To align the flow with the business logic,
 * it is usually necessary to standardize the data classes in the business logic project.
 * This is done so that when changes occur on the server-side, we already have our own standardized data mapping
 */
data class GetCharacterDetailResponse (
        @SerializedName("id"        ) var id        : Int?      = null,
        @SerializedName("name"      ) var name      : String?   = null,
        @SerializedName("status"    ) var status    : String?   = null,
        @SerializedName("species"   ) var species   : String?   = null,
        @SerializedName("type"      ) var type      : String?   = null,
        @SerializedName("gender"    ) var gender    : String?   = null,
        @SerializedName("origin"    ) var origin    : GetCharacterDetailOriginLocation?   = null,
        @SerializedName("location"  ) var location  : GetCharacterDetailOriginLocation?   = null,
        @SerializedName("image"     ) var image     : String?   = null,
        @SerializedName("episode"   ) var episodes  : ArrayList<String>?   = arrayListOf()
)

data class GetCharacterDetailOriginLocation (
        @SerializedName("name"      ) var name      : String?   = null
)