/**
 * Created by estilon on 02/06/17.
 */

package problem;

class Solution : ArrayList<Int>{

    var cost : Int = 0;

    constructor() : super()

    constructor(sol: Solution): super(sol){
        this.cost = sol.cost;
    }
}