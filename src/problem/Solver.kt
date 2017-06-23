package problem

import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by estilon on 02/06/17.
 */
class Solver(instance: String, tenure: Float, iterations: Int) {

    private var evaluator: Evaluator = MRC(instance)
    private val candidateSets: Array<LinkedList<Int>>
    private var tabuList = ArrayDeque<Int>()
    private val frequency: Array<Int>
    private val tenure: Int
    private val solutionsPool: ArrayList<Solution>
    private val iterations : Int

    init {
        candidateSets = evaluator.getSet()
        this.tenure = (evaluator.candidates()*tenure).toInt()
        frequency = Array(evaluator.candidates()+1, {0})
        solutionsPool = ArrayList<Solution>()
        this.iterations = iterations
    }

    fun constructiveHeuristic() : Solution{

        /*val m = evaluator.getCosts()

        val solution = Solution()

        for(set in candidateSets){
            var cost = 0
            var bestCost = -1
            var indexBestCost = 0
            for(item in set){
                for(j in 0..m.size-1){
                    cost += m[item][j]
                }

                if(cost > bestCost){
                    bestCost = cost
                    indexBestCost = item
                }
            }

            solution.add(indexBestCost)
            set.remove(indexBestCost)
        }

        return solution*/

        //candidateSets.forEach { println(it) }

        val m = evaluator.getCosts()

        val solution = Solution()

        var chosenCandidates = IntArray(evaluator.problemSize(), {-1})

        for((k, v) in candidateSets.withIndex()){
            var bestCandidate = -1
            var bestWeight = -1
            for(i in v){
                var total = 0
                for((l, s) in candidateSets.withIndex()){
                    var best = 0
                    if(chosenCandidates[l] == -1){

                        for(j in s){
                            if(m[i][j] > best) {
                                best = Integer.max(m[i][j], m[j][i])
                            }
                        }

                    }else{
                        val j = chosenCandidates[l]
                        best = Integer.max(m[i][j], m[j][i])
                    }

                    total += best
                }


                if(total > bestWeight){
                    bestWeight = total
                    bestCandidate = i
                }

                //println("$i = ${costForCandidadte[i]}")
            }
            candidateSets[k].remove(bestCandidate)
            solution.add(k, bestCandidate)
        }
        return solution
    }

    fun localSearch(iterations: Int) : Solution{

        val initialSolution = constructiveHeuristic()
        var incumbent = Solution(initialSolution)
        var bestSol = Solution(incumbent)

        evaluator.evaluate(incumbent)
        bestSol.cost = incumbent.cost

        solutionsPool.add(incumbent.clone() as Solution)

        //println("Initial solution " + incumbent)
        //println("Initial solution cost" + incumbent.cost)

        var i = 0
        var iterationsWithoutImprovement = 0

        while (i < iterations){

            if(iterationsWithoutImprovement > 0 && iterationsWithoutImprovement % this.iterations == 0){
                incumbent = Solution(restart(incumbent))
                evaluator.evaluate(incumbent)
                //println("Reiniciou com custo "+incumbent.cost)
            }

            val movement = getMovement(incumbent)
            if (movement != null) {
                incumbent.removeAt(movement.first)
                incumbent.add(movement.first, movement.second)
                makeMovementATabu(movement)
            }

            evaluator.evaluate(incumbent)
            iterationsWithoutImprovement++
            if(incumbent.cost > bestSol.cost){
                solutionsPool.add(incumbent.clone() as Solution)
                bestSol = Solution(incumbent)
                bestSol.cost = incumbent.cost
                iterationsWithoutImprovement = 0
                //System.out.println("Melhorou na iteraração " + i +" custo "+bestSol.cost)
            }

            //System.out.println("iteraçaõ "+ i + " tabu" + tabuList)
            i++
        }

        return bestSol

    }


    private fun  makeMovementATabu(movement: Pair<Int, Int>) {
        if(tabuList.size == tenure){
            tabuList.poll()
        }

        frequency[movement.second]++
        tabuList.add(movement.second)
    }

    private fun  getMovement(solution: Solution): Pair<Int, Int>? {
        val movements = ArrayList<Pair<Int, Int>>()
        for((s, set) in candidateSets.withIndex()){
            set.mapTo(movements) { Pair(s, it) }
        }

        Collections.shuffle(movements)

        var bestMovement : Pair<Int, Int>? = null
        var bestMovementCost = -1

        for(movement in movements){
            val cost = evaluator.evaluateExchange(solution, movement.first, movement.second)
            if((cost > bestMovementCost && notATabu(movement)) || cost > solution.cost){

                bestMovement = movement
                bestMovementCost = cost

                if(cost > solution.cost) break
            }
        }
        assert(bestMovement != null)
        if (bestMovement != null) {
            val flag = candidateSets[bestMovement.first].remove(bestMovement.second)
            assert(flag)
            candidateSets[bestMovement.first].add(solution[bestMovement.first])
        }

        return bestMovement
    }

    private fun  notATabu(movement: Pair<Int, Int>): Boolean {
        return !tabuList.contains(movement.second)
    }

    private fun restart(incumbent : Solution) : Solution{

        for((key, value) in incumbent.withIndex()){
            candidateSets[key].add(value)
        }

        val random = Random()
        val solution = Solution(solutionsPool[random.nextInt(solutionsPool.size)])

        for((key, value) in solution.withIndex()){
            candidateSets[key].remove(value)
        }

        //println(solution)

        val maximuns = nMax(frequency,tenure)

        tabuList.clear()
        maximuns.filter { it >= 0 }
                .forEach { tabuList.add(it) }

        return solution
    }

    fun nMax(v: Array<Int>, n: Int): IntArray {
        val maxVector = IntArray(n)
        for (i in 0..n - 1) {
            var best = 0
            for (j in v.indices) {
                if (v[j] > v[best]) {
                    best = j
                }
            }
            if (v[best] == 0) {
                maxVector[i] = -1
            } else {
                maxVector[i] = best
            }

            v[best] = 0
        }
        return maxVector
    }
}