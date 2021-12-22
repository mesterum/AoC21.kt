import java.util.function.Consumer

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String, act: Consumer<in String>): Unit = ClassLoader.getSystemResourceAsStream("$name.txt")!!
    .bufferedReader().lines().forEach(act)
