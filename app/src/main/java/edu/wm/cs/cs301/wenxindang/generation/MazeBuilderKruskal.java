package edu.wm.cs.cs301.wenxindang.generation;

import java.util.ArrayList;

/**
 * This class has the responsibility to create a maze of given dimensions (width, height)
 * together with a solution based on a distance matrix.
 * The MazeBuilder implements Runnable such that it can be run a separate thread.
 * The MazeFactory has a MazeBuilder and handles the thread management.
 * This class MazeBuilderKruskal inherits the MazeBuilder, and some specialized methods are extended.
 *
 * The maze is built with a randomized version of Kruskal's algorithm.
 * This means a spanning tree is expanded into a set of cells by removing wallboards from the maze.
 * Algorithm leaves wallboards in tact that carry the border flag.
 * Borders are used to keep the outside surrounding of the maze enclosed and
 * to make sure that rooms retain outside walls and do not end up as open stalls.
 *
 */
public class MazeBuilderKruskal extends MazeBuilder implements Runnable {

    /**
     * The field needs to include:
     * 	\t 1) an array list contains all candidate Wallboards to tear down.
     *  \t 2) a two dimensional array that records which set (or tree) a position belongs to,
     *  \t\t by assigning different indices to positions in different sets (or trees).
     *
     *  The methods needed:
     *  \t 1) override the generatePathways() to generate a spanning tree with Kruskal's algorithm.
     *  \t 2) Helper methods, extractWallboardFromCandidateSetRandomly() to get a wallboard from candidate Wallboards list and remove it from the list;
     *  \t 3) Helper methods, updateTreeNumOfpositions() to update the set (or tree) information of positions at current.
     */

    private int[][] treeIndex; // a two dimensional array that records which set (or tree) a position belongs to.
    private int indexNum; // Helper integer to count the number of merges.

    /**
     * Constructor for a randomized maze generation
     */
    public MazeBuilderKruskal() {
        super();
        System.out.println("MazeBuilderKruskal uses Kruskal's algorithm to generate maze.");
    }

    /**
     * This method generates pathways into the maze by using Kruskal's algorithm to generate a spanning tree for an undirected graph.
     * The cells are the nodes of the graph and the spanning tree. An edge represents that one can move from one cell to an adjacent cell.
     * So an edge implies that its nodes are adjacent cells in the maze and that there is no wallboard separating these cells in the maze.
     *
     * Initialization:
     * 	the array list contains all Wallboards possible to tear down in a maze.
     *  all positions are set to different indices such that none of them are in the same tree.
     *
     *  While (the list of Wallboards is not empty and positions do not have the same index){
     *  	Wallboard = extractWallboardFromCandidateSetRandomly();
     *  	find the positions adjacent to this Wallboard;
     *  	updateTreeNumOfpositions();
     *  }
     */
    @Override
    protected void generatePathways() {
        // initialization
        final ArrayList<Wallboard> candidates = new ArrayList<Wallboard>(); //a list contains all candidate Wallboards to tear down.
        int indexCounter = 0;
        treeIndex = new int[width][height];
        for (int row=0; row<width; row++) {
            for (int col=0; col<height; col++) {
                treeIndex[row][col] = indexCounter; //all positions are set to different indices such that none of them are in the same tree.
                indexCounter++;
                for (CardinalDirection dir : CardinalDirection.values()) {
                    Wallboard wallb = new Wallboard(row,col,dir);
                    if (floorplan.canTearDown(wallb)){
                        candidates.add(wallb); // create a list contains all Wallboards possible to tear down in a maze.
                    }
                }
            }
        }
        // end initialization.
        // indicates the number of merges, if all positions are merged into a single tree, it equals to 1.
        indexNum = indexCounter;
        while (!candidates.isEmpty()&&indexNum!=1) {
            // in order to have a randomized algorithm,
            // we randomly select and extract a wallboard from our candidate set
            // this also reduces the set to make sure we terminate the loop
            Wallboard curWallboard = extractWallboardFromCandidateSetRandomly(candidates);
            // get the two corresponding positions adjacent to this wallboard.
            int X = curWallboard.getX();
            int Y = curWallboard.getY();
            int neighborX = curWallboard.getNeighborX();
            int neighborY = curWallboard.getNeighborY();
            if (neighborX>=0 && neighborX<width && neighborY>= 0 && neighborY<height){
                int index1 = treeIndex[X][Y];
                int index2 = treeIndex[neighborX][neighborY];
                // if these two positions are not in the same tree, then merge two trees
                // by changing indices of positions in trees to the same one.
                // Record the number of merges have done.
                if (index1!=index2) {
                    floorplan.deleteWallboard(curWallboard);
                    updateTreeNumOfpositions(index1, index2);
                    indexNum--;
                }
            }
        }
    }

    /**
     * Pick a random wallboard in the list of candidates,
     * remove the candidate from the list and return it.
     * @param candidates is the list of candidates to randomly remove a wall board from
     * @return Wallboard from the list, randomly chosen
     */
    private Wallboard extractWallboardFromCandidateSetRandomly(final ArrayList<Wallboard> candidates) {
        return candidates.remove(random.nextIntWithinInterval(0, candidates.size()-1));
    }

    /**
     * Update the set index of each position, which indicates the tree a position belongs to.
     * if two positions have same indices, which implies that they are in one tree,
     * then, return the current record of indices;
     * if two positions have different indices, which impies that they are in two different trees,
     * then, merge two trees by setting all positions in two sets to the same index.
     * @param index1 indicates the set (or tree) of one of the position adjacent to a wallboard.
     * @param index2 indicates the set (or tree) of the other position adjacent to a wallboard.
     */
    private void updateTreeNumOfpositions(int index1, int index2) {
        for (int row=0; row<width; row++) {
            for (int col=0; col<height; col++) {
                if (treeIndex[row][col]==index2) {
                    treeIndex[row][col]=index1;
                }
            }
        }
    }

    /**
     * Used for testing to track the Index of each position.
     * @return the set index of each position, which indicates the tree a position belongs to.
     */
    public int[][] gettreeIndex(){
        return treeIndex;
    }

}

