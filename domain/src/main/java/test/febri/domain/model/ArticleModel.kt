package test.febri.domain.model

data class ArticleModel(
    val id: Int,
    val title: String,
    val authors: List<Author>,
    val url: String,
    val imageUrl: String,
    val newsSite: String,
    val summary: String,
    val publishedAt: String,
    val updatedAt: String,
    val featured: Boolean,
    val launches: List<Launch>,
    val events: List<Event>
)
