package model

import com.google.gson.annotations.SerializedName

class Entity(login: String, repos_url: String, type: String) {
    @SerializedName("login")
    val login: String = login
    @SerializedName("repos_url")
    val reposUrl: String = repos_url
    @SerializedName("type")
    val type: String = type
}