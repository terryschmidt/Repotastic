package model

import com.google.gson.annotations.SerializedName

class Repo(name: String, language: String) {
    @SerializedName("name")
    val name: String = name
    @SerializedName("language")
    val language: String = language
}