package test.febri.data.model

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("event_id") val eventId: Int,
    val provider: String?
)