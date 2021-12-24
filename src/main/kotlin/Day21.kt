fun main() {
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
  fun wins(start: Int): ArrayList<Int> {
    val s10 = start.mod(10)
    val score = Array(10){ pos ->
        if (pos==s10) PosChances(0,1)
        else PosChances.empty
    }
    val wins = arrayListOf<Int>()
    var step=0
    var noGoingPossibilities = 1
    while (noGoingPossibilities>0){
        for (_i in 0..2){
            val last2 = score.takeLast(2)
            for (i in 9 downTo 3 step 2){
                score[i-1] += score[i-2]
                score[i] += score[i-1]
                score[i-1] += score[i-3]
//            println(i)
            }
            score[0] += last2[1]
            score[1] += score[0]
            score[0] += last2[0]
        }
        wins.add((0..9).sumOf { i ->
            score[i].addScore((i+step*3+2)%10+1).trimWin(21)
        })
        noGoingPossibilities = noGoingPossibilities*27 - wins[step]
        step++
    }
    return wins
  }
  fun part2(): Long {
    val wins = startingPositions.map(::wins)
    println(wins)
    var notWinner1 = 1.toLong(); var notWinner2 = 1.toLong()
    val (winUniv1, winUniv2) = wins[0].zip(wins[1]){ w1, w2 ->
        notWinner1 = notWinner1*27 - w1
        val noW1=w1 * notWinner2
        notWinner2 = notWinner2*27 - w2
        noW1 to w2 * notWinner1
    }.fold(0.toLong() to 0.toLong()){ (s1,s2), (noW1, noW2) ->
        s1+noW1 to s2+noW2
    }
    return maxOf(winUniv1, winUniv2)
  }
//    println(part1())
    println(part2())
}
data class PosChances(val ch:IntArray = intArrayOf(), var stScore:Int = -1,
    var endScore:Int = stScore+ch.size)
{
    constructor(stScore:Int, vararg ch: Int):this(ch,stScore)
//    constructor(ch: IntArray, stScore:Int, endScore:Int):this(ch,stScore){
//        this.endScore=endScore
//    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PosChances

        if (!ch.contentEquals(other.ch)) return false
        if (stScore != other.stScore) return false
        if (endScore != other.endScore) return false

        return true
    }
    override fun hashCode(): Int {
        var result = ch.contentHashCode()
        result = 31 * result + stScore
        result = 31 * result + endScore
        return result
    }

    operator fun plus(other: PosChances): PosChances {
        if (stScore<0) return if (other.stScore<0) this
        else other.copy()
//            PosChances(other.ch, other.stScore, other.endScore)
        if (other.stScore<0) return copy()
//        PosChances(ch, stScore, endScore)
        val terminals = Array(4){ i: Int ->
            if (i<2) if (i==0 == stScore<other.stScore) this else other
            else if (i==2 == endScore<other.endScore) this else other
        }
        return PosChances(
          IntArray(
            terminals[3].endScore-terminals[0].stScore){ it: Int ->
            val i = it+terminals[0].stScore
            if (i<terminals[1].stScore)
              return@IntArray if (i<terminals[0].endScore)
                  terminals[0].ch[it] else 0
            if (i>=terminals[2].endScore)
              return@IntArray terminals[3].ch[i-terminals[3].stScore]
            terminals[0].ch[it]+terminals[1].ch[i-terminals[1].stScore]
          },terminals[0].stScore
        )
    }

    fun addScore(score: Int): PosChances {
        if (stScore in 0 until endScore) {
            stScore+=score
            endScore+=score
        }
        return this
    }

    fun trimWin(threshold: Int): Int {
        if (endScore<=threshold) return 0
        val sum = ch.asList().subList((threshold-stScore).coerceAtLeast(0)
            ,endScore-stScore).sum()
        endScore=threshold
        if (endScore<=stScore) stScore=-1
        return sum
    }

    companion object {
        val empty = PosChances()
    }
}
