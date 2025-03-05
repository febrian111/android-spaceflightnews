package test.febri.data.mapper

import test.febri.data.model.ArticleResponse
import test.febri.data.model.AuthorResponse
import test.febri.data.model.EventResponse
import test.febri.data.model.LaunchResponse
import test.febri.data.model.SocialsResponse

import test.febri.domain.model.ArticleModel
import test.febri.domain.model.Author
import test.febri.domain.model.Event
import test.febri.domain.model.Launch
import test.febri.domain.model.Socials

fun ArticleResponse.toDomain() = ArticleModel(
    id = id,
    title = title,
    authors = authors?.map { it.toDomain() } ?: listOf(),
    url = url.orEmpty(),
    imageUrl = imageUrl.orEmpty(),
    newsSite = newsSite.orEmpty(),
    summary = summary.orEmpty(),
    publishedAt = publishedAt.orEmpty(),
    updatedAt = updatedAt.orEmpty(),
    featured = featured,
    launches = launches?.map { it.toDomain() } ?: listOf(),
    events = events?.map { it.toDomain() } ?: listOf()
)

fun AuthorResponse.toDomain() = Author(
    name = name,
    socials = socials.toDomain()
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
