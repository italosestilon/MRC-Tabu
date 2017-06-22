package problem

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.io.StreamTokenizer
import java.util.*

/**
 * Created by estilon on 02/06/17.
 */
class MRC(filename: String) : Evaluator{

    var size: Int = 0;
    var c = 0

    var sets: Array<LinkedList<Int>>
    var m: Array<IntArray>

    init {
        sets = Array(0, {_ -> LinkedList<Int>() })
        m = Array(0, {_ -> intArrayOf()})
        readInput(filename)
    }

    override fun problemSize(): Int {
        return size
    }

    override fun candidates(): Int {
        return c
    }

    override fun getSet(): Array<LinkedList<Int>> {

        var setsCopy = Array<LinkedList<Int>>(sets.size, {i ->
            var set = LinkedList<Int>()
            for(elem in sets[i]){
                set.add(elem)
            }
            set
        })
        return setsCopy
    }

    override fun getCosts(): Array<IntArray> {
        return m.clone()
    }

    override fun evaluate(sol: Solution): Int {
        var cost = 0;
        for(i in sol){
            for(j in sol){

                cost+= m[i][j];

            }
        }
        sol.cost = cost
        return cost
    }

    override fun evaluateExchange(sol: Solution, set: Int, elem: Int): Int {
        val sol2 = Solution(sol)
        sol2.remove(set)
        sol2.add(set, elem)
        return evaluate(sol2)
    }

    private fun instances() {

        var random = Random(4)
        val s = 10
        var r = 10

        sets = Array<LinkedList<Int>>(s, {i ->
            var k = random.nextInt(r) + 1
            println(k)
            val set = LinkedList<Int>()
            for (j in 0..k - 1) {
                set.add(c++)
            }
            set
        })

        m = Array(c) { IntArray(c) }

        for (i in 0..s - 1) {
            for (e in sets[i]) {
                for (j in i + 1..s - 1) {
                    if (i == j) continue
                    for (f in sets[j]) {
                        if (m[e][f] == 0) {
                            m[e][f] = random.nextInt(8) + 1
                            //m[f][e] = 0;
                        }
                    }
                }
            }
        }

        for (i in 0..c - 1) {
            for (j in 0..c - 1) {
                print(m[i][j].toString() + " ")
            }

            println()
        }

        this.size = s

    }


    private fun readInput(filename: String) {
        val fileInst = BufferedReader(FileReader(filename))
        val stok = StreamTokenizer(fileInst)

        stok.nextToken()
        val s = stok.nval.toInt()

        stok.nextToken()

        sets = Array<LinkedList<Int>>(s, {i ->
            val k = stok.nval.toInt()
            stok.nextToken()
            //println(k)
            val set = LinkedList<Int>()
            for (j in 0..k - 1) {
                set.add(c++)
            }
            set
        })

        m = Array(c) { IntArray(c) }
        var a: Int
        for (i in 0..c - 1) {
            for (j in 0..c - 1) {
                a = stok.nval.toInt()
                stok.nextToken()
                m[i][j] = a
                //print(a.toString() + " ")
            }

            //println("")
        }

        this.size = s

    }
}