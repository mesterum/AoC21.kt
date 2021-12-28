@ExperimentalStdlibApi
fun main() {

    data class Input(
        val syM: CharArray
        , val growRule: Array<ArrayList<Int>>
        , val polymer0: LongArray
        , val symOcc: LongArray)

    fun init(name: String): Input {
        var polymerTemplate = ""
        val rules = mutableListOf<String>()
        val symbols:CharArray
        val syM: Map<Char, Int>
        val symbolS = mutableSetOf<Char>()
        var addingFirst = true
//        val maxSize = intArrayOf(0, 0)
        val ruleRegex = Regex("""([A-Z]{2}) -> ([A-Z])""")
        readInput(name) {
            if (addingFirst) {
                if (it.isBlank()) {
                    addingFirst = false
                } else {
                    polymerTemplate = it.onEach(symbolS::add)
                }
            } else {
//                println(it)
                rules += ruleRegex.matchEntire(it)!!.destructured.let { (pair, inserted) ->
                    pair+inserted
                }.onEach(symbolS::add)
            }
        }
//        rules.binarySearch("").inv()
        symbols = symbolS.toCharArray();symbols.sort()
        val symLen=symbols.size
        syM = buildMap(symLen){symbols.forEachIndexed { i, c -> put(c,i) }}
        val growRule = Array(symLen*symLen){ arrayListOf(it)}
        rules.forEach { rule ->
            val iLeft=symLen* syM[rule[0]]!!
            val iRight=syM[rule[1]]!!
            val iMid = syM[rule[2]]!!
            val i=iLeft+iRight
            growRule[iLeft+iMid].add(i)
            growRule[symLen*iMid+iRight].add(i)
            growRule[i].remove(i)
        }
        val polymer0=LongArray(symLen*symLen)
        polymerTemplate.reduce { acc, c ->
            polymer0[symLen*syM[acc]!!+syM[c]!!]++
            c
        }
        val symOcc = LongArray(symLen)
        symOcc[syM[polymerTemplate.first()]!!]++
        symOcc[syM[polymerTemplate.last()]!!]++
        return Input(symbols, growRule, polymer0, symOcc)
    }
    val (symbols, growRule, polymer0, symOcc0) =
    init("input14") //test14 //

    fun part1(steps: Int, prev0: LongArray =polymer0): LongArray {
//        println(rules)
        var prev=prev0//polymer0.copyOf()
        var crt=LongArray(prev.size)
        for (s in 1..steps){
            for (i in prev.indices){
                crt[i] = growRule[i].sumOf { prev[it] }
            }
            val temp = prev
            prev=crt; crt=temp
        }
        return prev
    }
    fun part2(input: LongArray): Long {
        val symOcc = symOcc0.copyOf()
        val symLen=symbols.size
        for (i in 0 until symLen)
          for (j in 0 until symLen){
            val v = input[symLen*i+j]
            symOcc[i] += v
            symOcc[j] += v
          }
        return symOcc.maxOf { it } - symOcc.minOf { it } shr 1
    }
    val polymer=part1(10)
    println(part2(polymer))
    println(part2(part1(40-10, polymer)))
}
//

