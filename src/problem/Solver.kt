package problem

import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by estilon on 02/06/17.
 */
class Solver(instance: String, tenure: Int) {

    private var evaluator: Evaluator = MRC(instance)
    private val candidateSets: Array<LinkedList<Int>>
    private var tabuList = ArrayDeque<Int>()
    private val tenure: Int;

    init {
        candidateSets = evaluator.getSet()
        this.tenure = tenure
    }

    fun constructiveHeuristic() : Solution{

        val m = evaluator.getCosts()

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

        return solution
    }

    fun localSearch(iterations: Int) : Solution{

        val initialSolution = constructiveHeuristic()
        val incumbent = Solution(initialSolution)

        evaluator.evaluate(incumbent)

        println("Initial solution " + incumbent)
        println("Initial solution cost" + incumbent.cost)

        var i = 0

        while (i < iterations){

            val movement = getMovement(incumbent)
            if (movement != null) {
                incumbent.removeAt(movement.first)
                incumbent.add(movement.first, movement.second)
                makeMovementATabu(movement)
            }

            evaluator.evaluate(incumbent)

            i++
        }

        return incumbent

    }

    private fun  makeMovementATabu(movement: Pair<Int, Int>) {
        if(tabuList.size == tenure){
            tabuList.poll();
        }

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

                if(cost > solution.cost) break;
            }
        }
        if (bestMovement != null) {
            candidateSets[bestMovement.first].add(solution[bestMovement.first])
        }
        if (bestMovement != null) {
            candidateSets[bestMovement.first].remove(bestMovement.second)
        }

        return bestMovement
    }

    private fun  notATabu(movement: Pair<Int, Int>): Boolean {
        return !tabuList.contains(movement.second)
    }
}