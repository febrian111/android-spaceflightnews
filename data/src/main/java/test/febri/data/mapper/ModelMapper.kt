package test.febri.data.mapper

import test.febri.data.model.ArticleResponse
import test.febri.data.model.AuthorResponse
import test.febri.data.model.BlogResponse
import test.febri.data.model.EventResponse
import test.febri.data.model.LaunchResponse
import test.febri.data.model.SocialsResponse
import test.febri.domain.model.ArticleModel
import test.febri.domain.model.Author
import test.febri.domain.model.BlogModel
import test.febri.domain.model.Event
import test.febri.domain.model.Launch
import test.febri.domain.model.Socials
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import test.febri.data.model.ReportResponse
import test.febri.domain.model.ReportModel
import java.util.Locale


fun ArticleResponse.toDomain() = ArticleModel(
    id = id,
    title = title,
    authors = authors?.map { it.toDomain() } ?: listOf(),
    url = url.orEmpty(),
    imageUrl = imageUrl.orEmpty(),
    newsSite = newsSite.orEmpty(),
    summary = summary.orEmpty(),
    publishedAt = publishedAt?.dateToStringLocalTime().orEmpty(),
    updatedAt = updatedAt?.dateToStringLocalTime().orEmpty(),
    featured = featured,
    launches = launches?.map { it.toDomain() } ?: listOf(),
    events = events?.map { it.toDomain() } ?: listOf()
)

fun BlogResponse.toDomain() = BlogModel(
    id = id,
    title = title,
    url = url,
    imageUrl = imageUrl.orEmpty(),
    newsSite = newsSite,
    summary = summary.orEmpty(),
    publishedAt = publishedAt?.dateToStringLocalTime().orEmpty(),
    updatedAt = updatedAt?.dateToStringLocalTime().orEmpty(),
    featured = featured,
    launches = launches.map { it.toDomain() },
    events = events.map { it.toDomain() },
    authors = authors.map { it.toDomain() }
)

fun ReportResponse.toDomain() = ReportModel(
    id = id,
    title = title.orEmpty(),
    url = url.orEmpty(),
    imageUrl = imageUrl.orEmpty(),
    newsSite = newsSite.orEmpty(),
    summary = summary.orEmpty(),
    publishedAt = publishedAt?.dateToStringLocalTime().orEmpty(),
    updatedAt = updatedAt?.dateToStringLocalTime().orEmpty(),
    featured = featured,
    launches = launches?.map { it.toDomain() } ?: listOf(),
    events = events?.map { it.toDomain() } ?: listOf(),
    authors = authors?.map { it.toDomain() } ?: listOf()
)

fun AuthorResponse.toDomain() = Author(
    name = name,
    socials = socials?.toDomain()
)

fun SocialsResponse.toDomain() = Socials(
    x = x.orEmpty(),
    youtube = youtube.orEmpty(),
    instagram = instagram.orEmpty(),
    linkedin = linkedin.orEmpty(),
    mastodon = mastodon.orEmpty(),
    bluesky = bluesky.orEmpty()
)

fun LaunchResponse.toDomain() = Launch(
    launchId = launchId.orEmpty(),
    provider = provider.orEmpty()
)

fun EventResponse.toDomain() = Event(
    eventId = eventId,
    provider = provider.orEmpty()
)

fun String.dateToStringLocalTime(): String {

    // Parse the string to an Instant
    val instant: Instant = Instant.parse(this)

    // Convert the Instant to local date and time
    val localDateTime: ZonedDateTime = instant.atZone(ZoneId.systemDefault())

    // Format the output (optional)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", Locale("id", "ID"))
    return localDateTime.format(formatter)
}
