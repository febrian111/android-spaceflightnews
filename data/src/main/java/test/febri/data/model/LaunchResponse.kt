package test.febri.data.model

import com.google.gson.annotations.SerializedName

data class LaunchResponse(
    @SerializedName("launch_id") val launchId: String?,
    val provider: String?
)