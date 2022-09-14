package edu.wm.cs.cs301.wenxindang.generation;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Random;



/**
 * Class handles the user interaction.
 * It implements an automaton with states for the different stages of the game.
 * It has state-dependent behavior that controls the display and reacts to key board input from a user.
 * At this point user keyboard input is first dealt with a key listener (SimpleKeyListener)
 * and then handed over to a Controller object by way of the keyDown method.
 *
 * The class is part of a state pattern. It has a state object to implement
 * state-dependent behavior.
 * The automaton currently has 4 states.
 * StateTitle: the starting state where the user can pick the skill-level
 * StateGenerating: the state in which the factory computes the maze to play
 * and the screen shows a progress bar.
 * StatePlaying: the state in which the user plays the game and
 * the screen shows the first person view and the map view.
 * StateWinning: the finish screen that shows the winning message.
 * The class provides a specific method for each possible state transition,
 * for example switchFromTitleToGenerating contains code to start the maze
 * generation.
 *
 * This code is refactored code from Maze.java by Paul Falstad,
 * www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 *
 * @author Peter Kemper
 */
public class Controller implements Order{
    private String TAG = "Controller";
    /**
     * The game has a reservoir of 4 states:
     * 1: show the title screen, wait for user input for skill level
     * 2: show the generating screen with the progress bar during
     * maze generation
     * 3: show the playing screen, have the user or robot driver
     * play the game
     * 4: show the finish screen with the winning/loosing message
     * The array entries are set in the constructor.
     * There is no mutator method.
     */
/*    State[] states;*/
    /**
     * The current state of the controller and the game.
     * All state objects share the same interface and can be
     * operated in the same way, although the behavior is
     * vastly different.
     * currentState is never null and only updated by
     * switchFrom .. To .. methods.
     */
/*    private Constants.UserInput userinput;*/
   /* State currentState;*/
    /**
     * The panel is used to draw on the screen for the UI.
     * It can be set to null for dry-running the controller
     * for testing purposes but otherwise panel is never null.
     */
    MazePanel panel;
    /**
     * The filename is optional, may be null, and tells
     * if a maze is loaded from this file and not generated.
     */
    String fileName;
    /**
     * The builder algorithm to use for generating a maze.
     */
    Order.Builder builder;
    /**
     * Specifies if the maze is perfect, i.e., it has
     * no loops, which is guaranteed by the absence of
     * rooms and the way the generation algorithms work.
     */
    boolean perfect;
    /**
     * The seed to be used for the random number generator
     * during maze generation.
     */
    int seed;
    /**
     * In the deterministic setting (true),
     * the same random maze will be generated over
     * and over again for the same settings of skill level,
     * builder, and perfect.
     * If false, a different maze will be generated each
     * and every time, even if settings of skill level,
     * builder, and perfect remain the same.
     */
    boolean deterministic;
    int skillLevel;


    private int progress = 0;
    private boolean showMaze;
    private boolean showSolution;
    private boolean showMap;
    private Constants.State state;
    private RangeSet rangeset;
    private Factory factory;
    private Maze mazeConfig ;
    private Floorplan floorplan;
    private int px, py ; // current position on maze grid (x,y)
    private int dx, dy;  // current direction
    private int walkStep;
    private StatePlaying statePlaying;

    public Controller() {
/*        states = new State[4];
        states[0] = new StateTitle();
        states[1] = new StateGenerating();
        states[2] = new StatePlaying();
        states[3] = new StateWinning();
        currentState = states[0];*/
/*        panel = new MazePanel();
        fileName = null;
        builder = Order.Builder.DFS; // default
        perfect = false; // default
        seed = 13; // default
        deterministic = true; // default*/
        fileName = null;
        factory = new MazeFactory();
        mazeConfig = new MazeContainer();
        statePlaying = new StatePlaying();
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public void setBuilder(Builder builder) {
        this.builder = builder;
    }
    public void setPerfect(boolean isPerfect) {
        this.perfect = isPerfect;
    }
    public void setSeed(int seed) {
        this.seed = seed;
    }
    public void setSkillLevel(int skillLevel){
        this.skillLevel = skillLevel;
    }
    public void setState(Constants.State state){
        this.state = state;
    }
//    public void setDeterministic(boolean deterministic) {
//        this.deterministic = deterministic;
//    }


    public MazePanel getPanel() {
        return panel;
    }
    public void setMazePanel(MazePanel panel) {
        this.panel = panel;
    }
    public Constants.State getState(){
        return state;
    }


    /**
     * Starts the controller and begins the game
     * with the title screen.
     */
    public void start() {
        /*currentState = states[0]; // initial state is the title state
        currentState.setFileName(fileName); // can be null
        currentState.start(this, panel);
        fileName = null; // reset after use*/
        if (fileName!=null){
            //Todo: consider input files in xml;
        }
        rangeset = new RangeSet();
    }
    /**
     * Switches the controller to the generating screen.
     * Assumes that builder and perfect fields are already set
     * with set methods if default settings are not ok.
     * A maze is generated.
     * @param , {@code 0 <= skillLevel}, size of maze to be generated
*/    /*public void switchFromTitleToGenerating(int skillLevel) {
        *//*currentState = states[1];
        currentState.setSkillLevel(skillLevel);
        currentState.setBuilder(builder);
        currentState.setPerfect(perfect);
        if (!deterministic) {
//        	System.out.println("Assignment: implement code such that a repeated generation creates different mazes! Program stops!");
//			System.exit(0) ;
            SingleRandom sr = SingleRandom.getRandom();
            seed = sr.nextInt();
            // TODO: implement code that makes sure we generate different random mazes
            // HINT: check http://download.oracle.com/javase/6/docs/api/java/util/Random.html
        }
        currentState.setSeed(seed);
        currentState.start(this, panel);*//*
        setState(Constants.State.Generating);
        progress = 0;
    }*/

    public void startGenerating(){
        setState(Constants.State.Generating);
        progress = 0;
        factory.order(this);
    }

    public void startTitle(){
        setState(Constants.State.Title);
    }

    public void startPlayAnimation(){
        setState(Constants.State.PlayAnimation);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startPlayManually(){
        setState(Constants.State.PlayManually);
        if (mazeConfig != null){
            Log.i(TAG,"mazeConfig is not Null");
        }
        statePlaying.setMazeConfiguration(this.mazeConfig);
        statePlaying.start(this, panel);
    }

    public void startFinish(){
        setState(Constants.State.WinManually);
    }

    /**
     * Switches the controller to the generating screen and
     * loads maze from file.
     * @param filename gives file to load maze from
     */
    /*public void switchFromTitleToGenerating(String filename) {
        currentState = states[1];
        currentState.setFileName(filename);
        currentState.start(this, panel);
    }*/
    /**
     * Switches the controller to the playing screen.
     * This is where the user or a robot can navigate through
     * the maze and play the game.
     * @param config contains a maze to play
     */
    /*public void switchFromGeneratingToPlaying(Maze config) {
        currentState = states[2];
        currentState.setMazeConfiguration(config);
        currentState.start(this, panel);
    }*/
    /**
     * Switches the controller to the final screen
     * @param pathLength gives the length of the path
     */
    /*public void switchFromPlayingToWinning(int pathLength) {
        currentState = states[3];
        currentState.setPathLength(pathLength);
        currentState.start(this, panel);
    }*/
    /**
     * Switches the controller to the initial screen.
     */
    /*public void switchToTitle() {
        currentState = states[0];
        currentState.start(this, panel);
    }*/

    /**
     * Method incorporates all reactions to keyboard input in original code.
     * The simple key listener calls this method to communicate input.
     * @param key is the user input
     * @param value is only used for the numerical input for the size of the maze
     */
    /*public boolean keyDown(Constants.UserInput key, int value) {
        // delegated to state object
        *//*return currentState.keyDown(key, value);*//*
        if (state== Constants.State.PlayManually){
            switch (key){
                case UP:
            }
        }
    }*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean keyDown(Constants.UserInput key, int value) {
        // delegated to state object
        return statePlaying.keyDown(key, value);
    }

    /**
     * Moves in the given direction with 4 intermediate steps,
     * updates the screen and the internal position
     * @param dir, only possible values are 1 (forward) and -1 (backward)
     */
    /*private synchronized void walk(int dir) {
        // check if there is a wall in the way
        if (!checkMove(dir))
            return;
        // walkStep is a parameter of FirstPersonDrawer.draw()
        // it is used there for scaling steps
        // so walkStep is implicitly used in slowedDownRedraw
        // which triggers the draw operation in
        // FirstPersonDrawer and MapDrawer
        for (int step = 0; step != 4; step++) {
            walkStep += dir;
            slowedDownRedraw();
        }
        setCurrentPosition(px + dir*dx, py + dir*dy) ;
        walkStep = 0; // reset counter for next time
        //logPosition(); // debugging
        drawHintIfNecessary();
    }*/
    /**
     * Draws and waits. Used to obtain a smooth appearance for rotate and move operations
     */
    /*private void slowedDownRedraw() {
        draw() ;
        try {
            Thread.sleep(25);
        } catch (Exception e) {
            // may happen if thread is interrupted
            // no reason to do anything about it, ignore exception
        }
    }*/

    /**
     * Helper method for walk()
     * @param dir is the direction of interest
     * @return true if there is no wall in this direction
     */
    protected boolean checkMove(int dir) {
        CardinalDirection cd = null;
        switch (dir) {
            case 1: // forward
                cd = getCurrentDirection();
                break;
            case -1: // backward
                cd = getCurrentDirection().oppositeDirection();
                break;
            default:
                throw new RuntimeException("Unexpected direction value: " + dir);
        }
        return !mazeConfig.hasWall(px, py, cd);
    }
    protected void setCurrentPosition(int x, int y) {
        px = x ;
        py = y ;
    }
    private void setCurrentDirection(int x, int y) {
        dx = x ;
        dy = y ;
    }
    /**
     * Turns of graphics to dry-run controller for testing purposes.
     * This is irreversible.
     */
    public void turnOffGraphics() {
        panel = null;
    }

    //// Extension in preparation for Project 3: robot and robot driver //////
    /**
     * The robot that interacts with the controller starting from P3
     */
    Robot robot;
    /**
     * The driver that interacts with the robot starting from P3
     */
    RobotDriver driver;

    /**
     * Sets the robot and robot driver
     * @param robot the robot that is used for the automated playing mode
     * @param robotdriver the driver that is used for the automated playing mode
     */
    public void setRobotAndDriver(Robot robot, RobotDriver robotdriver) {
        this.robot = robot;
        driver = robotdriver;

    }
    /**
     * The robot that is used in the automated playing mode.
     * Null if run in the manual mode.
     * @return the robot, may be null
     */
    public Robot getRobot() {
        return robot;
    }
    /**
     * The robot driver that is used in the automated playing mode.
     * Null if run in the manual mode.
     * @return the driver, may be null
     */
    public RobotDriver getDriver() {
        return driver;
    }
    /**
     * Provides access to the maze configuration.
     * This is needed for a robot to be able to recognize walls
     * for the distance to walls calculation, to see if it
     * is in a room or at the exit.
     * Note that the current position is stored by the
     * controller. The maze itself is not changed during
     * the game.
     * This method should only be called in the playing state.
     * @return the MazeConfiguration
     */
    public Maze getMazeConfiguration() {
        return mazeConfig;
    }
    /**
     * Provides access to the current position.
     * The controller keeps track of the current position
     * while the maze holds information about walls.
     * This method should only be called in the playing state.
     * @return the current position as [x,y] coordinates,
     * {@code 0 <= x < width, 0 <= y < height}
     */
    public int[] getCurrentPosition() {
        /*return ((StatePlaying)states[2]).getCurrentPosition();*/
        int[] currentPos = new int[2];
        currentPos[0] = px;
        currentPos[1] = py;
        return currentPos;
    }
    /**
     * Provides access to the current direction.
     * The controller keeps track of the current position
     * and direction while the maze holds information about walls.
     * This method should only be called in the playing state.
     * @return the current direction
     */
    public CardinalDirection getCurrentDirection() {
        /*return ((StatePlaying)states[2]).getCurrentDirection();*/
        return CardinalDirection.getDirection(dx, dy);
    }

    /**
     * Gives the required skill level, range of values 0,1,2,...,15.
     *
     * @return the skill level or size of maze to be generated in response to an order
     */
    @Override
    public int getSkillLevel() {
        return skillLevel;
    }

    /**
     * Gives the requested builder algorithm, possible values
     * are listed in the Builder enum type.
     *
     * @return the builder algorithm that is expected to be used for building the maze
     */
    @Override
    public Builder getBuilder() {
        return builder;
    }

    /**
     * Describes if the ordered maze should be perfect, i.e. there are
     * no loops and no isolated areas, which also implies that
     * there are no rooms as rooms can imply loops
     *
     * @return true if a perfect maze is wanted, false otherwise
     */
    @Override
    public boolean isPerfect() {
        return perfect;
    }

    /**
     * Gives the seed that is used for the random number generator
     * used during the maze generation.
     *
     * @return the current setting for the seed value of the random number generator
     */
    @Override
    public int getSeed() {
        return seed;
    }

    /**
     * Delivers the produced maze.
     * This method is called by the factory to provide the
     * resulting maze as a MazeConfiguration.
     * It is a call back function that is called some time
     * later in response to a client's call of the order method.
     *
     * @param maze is the maze that is delivered in response to an order
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void deliver(Maze maze) {
        if (maze == null){
            Log.i(TAG,"here maze is null");
        }
        if (maze != null){
            Log.i(TAG,"here maze is not null");
        }
        this.mazeConfig = maze;
        showMaze = false ;
        showSolution = false ;
        showMap = false;
        floorplan = mazeConfig.getFloorplan();
        int[] start = mazeConfig.getStartingPosition() ;
        setCurrentPosition(start[0],start[1]) ;
        setCurrentDirection(1, 0) ; // east direction
        startPlayManually();
    }
    public int getPercentDone() {
        return progress;
    }
    /**
     * Provides an update on the progress being made on
     * the maze production. This method is called occasionally
     * during production, there is no guarantee on particular values.
     * Percentage will be delivered in monotonously increasing order,
     * the last call is with a value of 100 after delivery of product.
     *
     * @param percentage of job completion
     */
    @Override
    public void updateProgress(int percentage) {
        if (progress < percentage && percentage <= 100) {
            progress = percentage;
            /*redrawGenerating(panel, getPercentDone());
            // update the screen with the buffer graphics
            panel.commit(); ;*/
        }
    }

    /*private void redrawGenerating(MazePanel panel, int percentDone) {
        panel.addBackground(percentDone);
    }*/
}

