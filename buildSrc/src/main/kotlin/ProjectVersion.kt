object ProjectVersion {
    val version: String
        get() {
            val tag = System.getenv("GITHUB_TAG_NAME")
            return when {
                !tag.isNullOrBlank() -> tag
                else -> "undefined"
            }
        }
}
