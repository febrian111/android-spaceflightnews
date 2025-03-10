package test.febri.domain.model

data class SortFilterItemModel(
    val value: String,
    val label: String,
    var isSelected: Boolean = false
)
