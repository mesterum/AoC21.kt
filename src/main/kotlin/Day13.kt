

fun main() {

    var points= mutableSetOf<Pair<Int, Int>>()
    val folds = mutableListOf<Fold>()
    fun part1(fold: Fold): Int {
        points = points.asSequence().mapNotNull { (x,y) ->
            when(fold.direction){
                'x' -> if (x==fold.pivot) null
                else if (x>fold.pivot) 2*fold.pivot-x to y
                else x to y
                else -> if (y==fold.pivot) null
                else if (y>fold.pivot) x to 2*fold.pivot-y
                else x to y
            }
        }.toCollection(mutableSetOf())
        return points.size
    }

//    fun part2(input: List<String>): Int = 0
    fun init(name: String) {
        var addingPoints = true
        val maxSize = intArrayOf(0, 0)
        val foldRegex = Regex("""fold along ([xy])=(\d+)""")
        readInput(name) {
            if (addingPoints) {
                if (it.isBlank()) {
                    addingPoints = false
                } else {
                    val pair = it.split(",").map(String::toInt)
                    pair.forEachIndexed { i, v -> maxSize[i] = maxOf(maxSize[i], v) }
                    val (x, y) = pair
                    points.add(x to y)
                }
            } else {
                println(it)
                folds += foldRegex.matchEntire(it)!!.destructured.let { (direction, pivot) ->
                    Fold(direction = direction.single(), pivot = pivot.toInt())
                }
            }
        }
//        println(points.size)
        println(maxSize.toList())
    }
    init("input13")//""test13
    println(part1(folds[0]))
    folds.subList(1,folds.size).asSequence().map(::part1).last()
    for (y in 0..6){
        for (x in 0..80)
            print( if (x to y in points) '#' else ' ')
        println()
    }
}
//CEJKLUGJ


data class Fold(val direction: Char, val pivot: Int) {

}

