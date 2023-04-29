public class Heuristic {
    public int[] groupsBorders;
    public int[][] heuristicWeights;
    public int[][] fieldsValues;

    public Heuristic(int[] groupsBorders, int[][] heuristicWeights, int[][] fieldsValues) {
        this.heuristicWeights = heuristicWeights;
        this.groupsBorders = groupsBorders;
        this.fieldsValues = fieldsValues;
    }

    public int[] getWeights(int turn) {
        int group = 0;
        while(group < groupsBorders.length && groupsBorders[group] < turn) {
            group++;
        }
        return heuristicWeights[group];
    }
}
