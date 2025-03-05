package test.febri.data.model

data class AuthorResponse(
    val name: String,
    val socials: SocialsResponse?
)

data class SocialsResponse(
    val x: String?,
    val youtube: String?,
    val instagram: String?,
    val linkedin: String?,
    val mastodon: String?,
    val bluesky: String?
)



