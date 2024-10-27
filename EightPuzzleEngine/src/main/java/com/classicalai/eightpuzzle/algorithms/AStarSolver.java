package com.classicalai.eightpuzzle.algorithms;

import com.classicalai.eightpuzzle.environment.EnvironmentState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

// Uses MinHeap for frontier list
// Uses LinkedList <LONG> for visited list
public class AStarSolver extends Engine {
    static long bpd = 100000000000000L; // the board, parent and direction digits
    static long pd = 100000L; // the parent and direction digits

    // private MinHeap frontier = new MinHeap();
    private PriorityQueue<Long> frontier = new PriorityQueue<>();
    private HashMap<Integer, Long> visited = new HashMap<>();

    private int goalKey = 0;
    private String initialState;
    private String h;
    private double gameTime;
    private int highestDepth;

    // constructor
    public AStarSolver(EnvironmentState initialState, String h) {
        super(initialState);
        this.initialState = boardToString((long)initialState.getBoard());
        this.h = h;
    }

    // playing the algorithm
    public EnvironmentState[] play() {
        search();
        super.setSearchDepth(getSearchDepth());
        super.setNodesExpanded(getNodesExpanded());
        super.setRunningTime(getRunningTime());
        return getPathToGoalAsEnvironmentStates();
    }

    public EnvironmentState[] getPathToGoalAsEnvironmentStates() {
        int[] path = getPathToGoalAsIntArray();
        EnvironmentState[] pathEnvironmentStates = new EnvironmentState[path.length];
        EnvironmentState parent = null;
        for (int i = 0; i < path.length; i++) {
            pathEnvironmentStates[i].setBoard(path[i]);
            if (i == 0)
                pathEnvironmentStates[i].setParentState(null);
            else
                pathEnvironmentStates[i].setParentState(pathEnvironmentStates[i - 1]);

        }
        return pathEnvironmentStates;
    }

    // gets the depth of a certain node
    private int getDepth(int parent, int count) {
        // parent is found
        if (parent == 0)
            return count;

        long parentState = visited.get(parent);
        return getDepth(getParent(parentState), ++count);

    }

    // manhaten heuristic function (h1) --> |Xgoal - Xcurrent| + |Ygoal - Ycurrent|
    private int getManHeuristic(String board) {
        int value = 0;
        for (int i = 0; i < 9; i++) {

            if (board.charAt(i) == '0')
                continue; // do not add the manhanten distance for the empty slote
            int currentNode = Integer.parseInt(Character.toString(board.charAt(i)));
            value += Math.abs((currentNode % 3) - (i % 3)); // adding x difference
            value += Math.abs((currentNode / 3) - (i / 3)); // adding y difference
        }
        return value;
    }

    // euler heuristic function (h2) --> sqrt((Xgoal - Xcurrent)^2 + (Ygoal -
    // Ycurrent)^2)
    private int getEulHeuristic(String board) {
        int value = 0;
        for (int i = 0; i < 9; i++) {
            if (board.charAt(i) == '0')
                continue; // do not add the manhanten distance for the empty slote
            int currentNode = Integer.parseInt(Character.toString(board.charAt(i)));
            int distance = (int) Math.sqrt(Math.pow(((currentNode % 3) - (i % 3)) + 0.0, 2.0)
                    + Math.pow(((currentNode / 3) - (i / 3)) + 0.0, 2.0));
            value += distance; // adding y difference
        }
        return value;
    }

    // prepares the board to be a state --> by adding its parent Id and the move
    // (dir) it came from and its cost to the goal
    private long prepareState(String board, int parent, int dir, String h) {
        int f = 0; // = g(depth) + h(h1 or h2)
        int g = 0;
        // choosing the heuristic funtion desired
        switch (h) {
            case "h1": // manhaten heuristic
                g = getDepth(parent, 0);
                // updating highest depth
                if (this.highestDepth < g)
                    this.highestDepth = g;
                f = g + getManHeuristic(board);
                break;
            case "h2": // euler heuristic
                g = getDepth(parent, 0);
                // updating highest depth
                if (this.highestDepth < g)
                    this.highestDepth = g;
                f = g + getEulHeuristic(board);
                break;
            default:
                break;
        }

        // state: [cost|board|parent|dir]
        return f * bpd + Long.parseLong(board) * pd + parent * 10 + dir;
    }

    // gets the cost of a state [COST|board|parent|dir]
    static int getCost(long state) {
        return (int) (state / bpd);
    }

    // gets the board of a state [cost|BOARD|parent|dir]
    static long getBoard(long state) {
        return ((state % bpd) / pd);
    }

    // gets the parent of a state [cost|board|PARENT|dir]
    static int getParent(long state) {
        return (int) (state % pd) / 10;
    }

    // gets the dir of a state [cost|board|parent|DIR]
    static int getDir(long state) {
        return (int) (state % 10);
    }

    // gets the state without the cost [cost|BOARD|PARENT|DIR]
    static long removeCost(long state) {
        return (state % bpd);
    }

    // tests if the goal is found
    // 0 1 2
    // 3 4 5
    // 6 7 8
    private boolean goalTest(long state) {
        long board = getBoard(state);
        return board == 12345678L;
    }

    // getting the index of the empty slot to be moved
    int searchForEmptySlote(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0')
                return i;
        }
        return 0; // redundant
    }

    // convert the board to string to ease the empty slote movement
    static String boardToString(long board) {
        String boardString = String.valueOf(board);
        if (boardString.length() < 9)
            boardString = '0' + boardString;
        return boardString;
    }

    // returns zero if no up move
    static long moveUp(String s, int index) {
        if (index < 3)
            return 0;
        else {
            char temp = s.charAt(index - 3);
            s = s.replace('0', 't');
            s = s.replace(temp, '0');
            s = s.replace('t', temp);
            return Long.parseLong(s);
        }
    }

    // returns zero if no down move
    static long moveDown(String s, int index) {
        if (index > 5)
            return 0;
        else {
            char temp = s.charAt(index + 3);
            s = s.replace('0', 't');
            s = s.replace(temp, '0');
            s = s.replace('t', temp);
            return Long.parseLong(s);
        }
    }

    // returns zero if no right move
    static long moveRight(String s, int index) {
        if ((index + 1) % 3 == 0)
            return 0;
        else {
            char temp = s.charAt(index + 1);
            s = s.replace('0', 't');
            s = s.replace(temp, '0');
            s = s.replace('t', temp);
            return Long.parseLong(s);
        }
    }

    // returns zero if no left move
    static long moveLeft(String s, int index) {
        if (index % 3 == 0)
            return 0;
        else {
            char temp = s.charAt(index - 1);
            s = s.replace('0', 't');
            s = s.replace(temp, '0');
            s = s.replace('t', temp);
            return Long.parseLong(s);
        }
    }

    // creating a new randomized Id with maximum 4 digits
    // not in visited list keys
    private int createId() {
        int id = (int) (Math.random() * 10000);
        if (visited.containsKey(id)) {
            return createId();
        }
        return id;
    }

    // searching for a value in hash map (visit list)
    private boolean hashMapSearch(long board) {
        for (Map.Entry<Integer, Long> entry : visited.entrySet()) {
            if (getBoard(entry.getValue()) == board)
                return true;
        }
        return false;
    }

    // check if state is in frountier or in visited lists
    // if in the visited returns -2
    // if in the frountier returns its index
    // else returns -1
    private int checkVisitedFront(long board) {
        // check if in the fronier
        int index;
        // if ((index = frontier.searchFor(board)) != -1)
        if ((index = searchFor(frontier, board)) != -1)
            return index;
        // check if in the visisted
        else if (hashMapSearch(board))
            return -2;
        return -1; // not found in either one
    }

    private long getElemFromQ(PriorityQueue<Long> q, int index) {
        ArrayList<Long> arr = new ArrayList<>(q);
        return arr.get(index);
    }

    private int searchFor(PriorityQueue<Long> q, long element) {
        ArrayList<Long> arr = new ArrayList<>(q);
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) == element)
                return i;
        }
        return -1;
    }

    private void update(PriorityQueue<Long> q, long newState, int index) {
        long oldState = getElemFromQ(q, index);
        q.remove(oldState);
        q.add(newState);
    }

    public void printFrontier() {
        ArrayList<Long> arr = new ArrayList<>(frontier);
        for (int i = 0; i < arr.size(); i++)
            System.out.println(getCost(arr.get(i)));
        System.out.println("........................");
    }

    // updates the frontier if new cost is lesser
    private void updateInFront(long newState, int index) {
        // if (getCost(frontier.get(index)) > getCost(newState))
        if (getCost(getElemFromQ(frontier, index)) > getCost(newState))
            // frontier.update(newState, index);
            update(frontier, newState, index);
    }

    // puts the new up state in frontier if not in visited or frontier lists
    // updates the frontier state if this new state is already in frontier but with
    // lower cost
    private void addUpChildState(String board, int index, int parentID) {
        int temp;
        long upBoard = moveUp(board, index);
        // if there is an up move
        if (upBoard != 0) {
            temp = checkVisitedFront(upBoard);
            if (temp == -1) { // if it is a new state
                // frontier.insert(prepareState(boardToString(upBoard), parentID, 3, h));
                frontier.add(prepareState(boardToString(upBoard), parentID, 3, h));
            } else if (temp != -1 && temp != -2) { // if it is in the fronteir
                updateInFront(prepareState(boardToString(upBoard), parentID, 3, h), temp);
            }
        }
    }

    // puts the new down state in frontier if not in visited or frontier lists
    // updates the frontier state if this new state is already in frontier but with
    // lower cost
    private void addDownChildState(String board, int index, int parentID) {
        int temp;
        long downBoard = moveDown(board, index);
        // if there is a down move
        if (downBoard != 0) {
            temp = checkVisitedFront(downBoard);
            if (temp == -1) { // if it is a new state
                // frontier.insert(prepareState(boardToString(downBoard), parentID, 4, h));
                frontier.add(prepareState(boardToString(downBoard), parentID, 4, h));
            } else if (temp != -1 && temp != -2) { // if it is in the fronteir
                updateInFront(prepareState(boardToString(downBoard), parentID, 4, h), temp);
            }
        }

    }

    // puts the new left state in frontier if not in visited or frontier lists
    // updates the frontier state if this new state is already in frontier but with
    // lower cost
    private void addLeftChildState(String board, int index, int parentID) {
        int temp;
        long leftBoard = moveLeft(board, index);
        // in case there is a left move
        if (leftBoard != 0) {
            temp = checkVisitedFront(leftBoard);
            if (temp == -1) { // if it is a new state
                // frontier.insert(prepareState(boardToString(leftBoard), parentID, 2, h));
                frontier.add(prepareState(boardToString(leftBoard), parentID, 2, h));
            } else if (temp != -1 && temp != -2) { // if it is in the fronteir
                updateInFront(prepareState(boardToString(leftBoard), parentID, 2, h), temp);
            }
        }

    }

    // puts the new right state in frontier if not in visited or frontier lists
    // updates the frontier state if this new state is already in frontier but with
    // lower cost
    private void addRightChildState(String board, int index, int parentID) {
        int temp;
        long rightBoard = moveRight(board, index);
        // if there is a right move
        if (rightBoard != 0) {
            temp = checkVisitedFront(rightBoard);
            if (temp == -1) { // if it is a new state
                // frontier.insert(prepareState(boardToString(rightBoard), parentID, 1, h));
                frontier.add(prepareState(boardToString(rightBoard), parentID, 1, h));
            } else if (temp != -1 && temp != -2) { // if it is in the fronteir
                updateInFront(prepareState(boardToString(rightBoard), parentID, 1, h), temp);
            }
        }

    }

    private void printBoard(String board) {
        for (int j = 0; j < 9; j += 3) {
            for (int i = j; i < j + 3; i++) {
                System.out.print(board.charAt(i) + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("---------------");
    }

    // start searching
    public void search() {
        // directions codes:
        // 0 root
        // 1 right parent
        // 2 left parent
        // 3 up parent
        // 4 down parent
        // parent codes:
        // 0 root
        // 1 --> 2590 others

        // start calculating time
        long startTime = System.currentTimeMillis();
        // preparing parent state
        long state = prepareState(initialState, 0, 0, h);
        // pushing it into fronier list
        // frontier.insert(state);
        frontier.add(state);

        // start tracing
        while (!frontier.isEmpty()) {
            state = frontier.poll();

            int stateID = createId();
            visited.put(stateID, state);

            // goal found and added to the visited hash map with key goalKey
            if (goalTest(state)) {
                goalKey = stateID;
                break;
            }

            // expanding neighbors r, l, u, d
            String board = boardToString(getBoard(state));
            // getting the palce of the empty slot
            int index = searchForEmptySlote(board);
            // start expanding the currnt state
            // child state after moving the empty slot up
            addUpChildState(board, index, stateID);

            // child state after moving the empty slot down
            addDownChildState(board, index, stateID);

            // child state after moving the empty slot right
            addRightChildState(board, index, stateID);

            // child state after moving the empty slot left
            addLeftChildState(board, index, stateID);
        }
        // end calculating time
        long endTime = System.currentTimeMillis();
        // calculating game time in seconds
        gameTime = (endTime - startTime);
    }

    // printing the moves leads to the goal
    public String[] getPathToGoal() {
        Stack<Integer> path = new Stack();
        long currentState = visited.get(goalKey);
        while (getParent(currentState) != 0) { // while the current state is not the root
            path.push(getDir(currentState));
            currentState = visited.get(getParent(currentState));
        }
        String[] pathArray = new String[path.size()];
        int size = path.size();
        for (int i = 0; i < size; i++) {
            int intPath = path.pop();
            switch (intPath) {
                case 1: // right
                    pathArray[i] = "right";
                    break;
                case 2: // left
                    pathArray[i] = "left";
                    break;
                case 3: // up
                    pathArray[i] = "up";
                    break;
                case 4: // down
                    pathArray[i] = "down";
                    break;
                default:
                    break;
            }
        }
        return pathArray;
    }

    // returns path to goal as array of ints
    public int[] getPathToGoalAsIntArray() {
        Stack<Integer> path = new Stack();
        long currentState = visited.get(goalKey);
        while (getParent(currentState) != 0) { // while the current state is not the root
            path.push((int)getBoard(currentState));
            currentState = visited.get(getParent(currentState));
        }
        int[] pathArray = new int[path.size()];
        int size = path.size();
        for (int i = 0; i < size; i++) {
            pathArray[i] = path.pop();
        }
        return pathArray;
    }

    // getting the depth of the goal
    public int getCostOfPath() {
        return getDepth(getParent(visited.get(goalKey)), 0);
    }

    // get number of nodes expanded
    public int getNumOfExpandedNodes() {
        return visited.size() - 1;
    }

    // get highest depth reached during searching
    public int getSearchDepth() {
        return this.highestDepth;
    }

    // get Game time
    public double getRunningTime() {
        return this.gameTime;
    }

}
