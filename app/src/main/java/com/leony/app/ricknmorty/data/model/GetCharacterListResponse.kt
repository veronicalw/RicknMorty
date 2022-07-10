package com.leony.app.ricknmorty.data.model

import com.google.gson.annotations.SerializedName

data class GetCharacterListResponse (
    @SerializedName("info"        ) var info        : GetPagesInfo?                     = null,
    @SerializedName("results"     ) var results     : ArrayList<GetCharacterResults>   = arrayListOf()
)

data class GetPagesInfo (
    @SerializedName("next"       ) var next  :   String? = null,
    @SerializedName("prev"       ) var prev  :   Any?    = null
)

data class GetCharacterResults (
    @SerializedName("id"        ) var id        : Int?      = null,
    @SerializedName("name"      ) var name      : String?   = null,
    @SerializedName("status"    ) var status    : String?   = null,
    @SerializedName("species"   ) var species   : String?   = null,
    @SerializedName("image"     ) var image     : String?   = null
)