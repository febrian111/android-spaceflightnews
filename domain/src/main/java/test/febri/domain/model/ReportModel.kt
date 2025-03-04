package test.febri.domain.model

import java.time.Instant

data class ReportModel(
    val id: Int,
    val title: String,
    val url: String,
    val imageUrl: String?,
    val newsSite: String,
    val summary: String?,
    val publishedAt: Instant,
    val updatedAt: Instant,
    val featured: Boolean,
    val launches: List<Launch>,
    val events: List<Event>,
    val authors: List<Author>
)