fun main() {
//    fun part2(input: List<String>): Int = 0
  fun init(name: String): IntArray {
    val startingPositions = intArrayOf(0, 0)
    val inputRegex = Regex("""Player ([12]) starting position: (\d+)""".trimIndent())
    readInput(name) {
        inputRegex.matchEntire(it)!!.destructured.let { (index, value) ->
            startingPositions[index.toInt() - 1] = value.toInt()
        }
    }
    return startingPositions
  }
    val startingPositions = init("input21") //intArrayOf(4,8) //
  fun part1(): Int {
    val scores = Array(2){ player ->
        var prevScore = 0
        var prevPosition = startingPositions[player]
        var step = 5-player
        return@Array IntArray(10){
            prevPosition = (prevPosition+step) % 10 + 1
            prevScore += prevPosition
            step = (step+8) % 10
            prevScore
        }
    }
    val maxScore= scores[0][9].coerceAtLeast(scores[1][9])
    val cycles10= 999 / maxScore
    val offset= intArrayOf(cycles10*scores[0][9], cycles10*scores[1][9])
    var winner=0; var winningStep=0
    if (maxScore==scores[0][9]){
        winningStep=scores[0].indexOfFirst {it+offset[0] > 999}
        if (winningStep > -1) {
            winner=1
        }
    }
    if(winner==0) {
        winningStep=scores[1].indexOfFirst {it+offset[1] > 999}
        winner=2
    }
    val losingPPoints =
    if (winner==1)
        offset[1] + if (winningStep==0) 0 else scores[1][winningStep-1]
    else
        offset[0] + scores[0][winningStep]
    return losingPPoints * 3*(20*cycles10+2*winningStep+winner)
  }
    println(part1())
}
