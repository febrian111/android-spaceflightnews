package test.febri.domain.util

object AppConst {

    enum class SortOrder(val value: String) {
        SORT_ASC_PUBLISH_DATE("published_at"),
        SORT_DES_PUBLISH_DATE("-published_at");
    }
}
