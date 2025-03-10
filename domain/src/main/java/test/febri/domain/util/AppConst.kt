package test.febri.domain.util

object AppConst {

    enum class SortOrder(val value: String, val displayName: String) {
        SORT_ASC_PUBLISH_DATE("published_at", "Publish Date Ascending"),
        SORT_DES_PUBLISH_DATE("-published_at", "Publish Date Descending");
    }
}
