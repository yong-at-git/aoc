package utils

// on condition the same file name appears once in classpath, including prod and test code
class IoHelper {

    fun getInts(filename: String): List<Int> {
        return getLines(filename).map { it.toInt() }
    }

    fun getLines(filename: String): List<String> {
        return getRawContent(filename)?.lines().orEmpty().filter { it.isNotBlank() }
    }

    fun getRawContent(filename: String): String? {
        return this.javaClass.classLoader.getResource(filename)?.readText()?.trim()
    }
}


