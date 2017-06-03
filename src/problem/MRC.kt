package problem

import java.util.*

/**
 * Created by estilon on 02/06/17.
 */
class MRC(filename: String) : Evaluator{

    var size: Int = 0;

    var sets: Array<LinkedList<Int>>;
    var m: Array<IntArray>;

    init {
        sets = Array(0, {_ -> LinkedList<Int>() })
        m = Array(0, {_ -> intArrayOf()})
        instances()
    }

    override fun problemSize(): Int {
        return size
    }

    override fun getSet(): Array<LinkedList<Int>> {

        var setsCopy = Array<LinkedList<Int>>(sets.size, {i ->
            var set = LinkedList<Int>()
            for(elem in sets[i]){
                set.add(elem)
            }
            set
        })
        return setsCopy;
    }

    override fun getCosts(): Array<IntArray> {
        return m.clone();
    }

    override fun evaluate(sol: Solution): Int {
        var cost = 0;
        for(i in sol){
            for(j in sol){
                if(i != j){
                    cost+= m[i][j];
                }
            }
        }
        sol.cost = cost
        return cost
    }

    override fun evaluateExchange(sol: Solution, set: Int, elem: Int): Int {
        val sol2 = Solution(sol)
        sol2.remove(set)
        sol2.add(set, elem)
        val improve = evaluate(sol2) - sol.cost
        return improve
    }

    private fun instances() {

        var random = Random(4)
        val s = 20
        var r = 50
        var c = 0

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
}