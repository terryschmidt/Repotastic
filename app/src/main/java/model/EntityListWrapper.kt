package model

import com.google.gson.annotations.SerializedName

class EntityListWrapper(list: List<Entity>) {
    @SerializedName("items")
    var entityList: List<Entity> = list // github users or organizations (repos)
}