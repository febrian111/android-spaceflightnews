package test.febri.domain.model
import java.time.Instant

data class BlogModel(
    val id: Int,
    val title: String,
    val url: String,
    val imageUrl: String?,
    val newsSite: String,
    val summary: String?,
    val publishedAt: String,
    val updatedAt: String,
    val featured: Boolean,
    val launches: List<Launch>,
    val events: List<Event>,
    val authors: List<Author>
)