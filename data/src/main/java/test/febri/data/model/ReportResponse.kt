package test.febri.data.model

import com.google.gson.annotations.SerializedName

data class ReportResponse(
    val id: Int,
    val title: String?,
    val url: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("news_site") val newsSite: String?,
    val summary: String?,
    @SerializedName("published_at") val publishedAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    val featured: Boolean,
    val launches: List<LaunchResponse>?,
    val events: List<EventResponse>?,
    val authors: List<AuthorResponse>?
)