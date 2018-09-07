package model

import com.google.gson.annotations.SerializedName

class Repo(name: String, language: String, created_at: String, html_url: String, isFork: Boolean, stargazers_count: Int, open_issues: Int) {
    @SerializedName("name")
    val name: String = name
    @SerializedName("language")
    val language: String = language
    @SerializedName("created_at")
    val created_at: String = created_at
    @SerializedName("html_url")
    val html_url: String = html_url
    @SerializedName("fork")
    val isFork: Boolean = isFork
    @SerializedName("stargazers_count")
    val stargazers_count: Int = stargazers_count
    @SerializedName("open_issues")
    val open_issues: Int = open_issues
}