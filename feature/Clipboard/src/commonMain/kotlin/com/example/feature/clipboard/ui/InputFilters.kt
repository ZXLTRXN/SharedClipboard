package com.example.feature.clipboard.ui

object InputFilters {
    private val urlPattern = Regex("""(https?://|www\.)[^\s/$.?#].[^\s]*""")

    private val phonePattern = Regex("""(?:\+|8)[\d\-\(\) ]{9,18}\d""")

    private val idPattern = Regex("""@[A-Za-z0-9_]+""")

    fun filterContent(text: String): String {
        val combinedPattern = Regex("${urlPattern.pattern}|${phonePattern.pattern}|${idPattern.pattern}")

        return combinedPattern.findAll(text)
            .map { it.value.trim() }
            .joinToString(separator = " ")
    }
}