package com.oblessing.mediapark.model

import java.io.Serializable

data class SearchCriteria(
    val value: String = "",
    val to: String = "None", //  ISO 8601 format (e.g. 2021-06-17T11:25:19Z)
    val from: String = "None", //  ISO 8601 format (e.g. 2021-06-17T11:25:19Z)
    val sortedBy: String = sortedByDate, // relevance
    val searchIn: String = "title,description", // content
) : Serializable {
    val badgeCount: Int
        get() = searchIn.split(",").filter { it.isNotBlank() }
            .count() + (if (to != dateNone) 1 else 0) + (if (from != dateNone) 1 else 0)

    companion object {
        const val sortedByRelevance = "relevance"
        const val sortedByDate = "publishedAt"

        const val searchInTitle = "title"
        const val searchInDescription = "description"
        const val searchInContent = "content"

        const val dateNone = "None"
    }
}