package edu.wm.cs.cs301.wenxindang.generation;

/*package gui;

import generation.CardinalDirection;
import generation.Floorplan;
import generation.Maze;
import gui.Constants.UserInput;*/
/*import Robot.Direction;
import Robot.Turn;*/

/**
 * Implement RobotDriver with WallFollower algorithm.
 *
 * @author Wenxin Dang
 *
 */
public class WallFollower implements RobotDriver{

    /**
     * provides the robot that this RobotDriver is driving.
     */
    private Robot robot;
    /**
     * set left sensor to a robot.
     */
    private DistanceSensor leftSensor;
    /**
     * set a front sensor to a robot.
     */
    private DistanceSensor frontSensor;
    /**
     * record the initialenergy of a robot.
     */
    private float initialEnergy;

    /**
     * set a robot driver with WallFollower algorithm.
     */
    public WallFollower() {
    }
//	/**
//	 * Construct a robot driver with WallFollower algorithm with fixtures.
//	 */
//	public WallFollower(Robot r, Maze m) {
//		pathlength =0;
//		this.maze = m;
//		this.robot = r;
//		initialEnergy = robot.getBatteryLevel();
//		leftSensor = new BasicSensor(this.maze, Direction.LEFT, this.robot);
//		frontSensor = new BasicSensor(this.maze, Direction.FORWARD, this.robot);
//		this.robot.addDistanceSensor(leftSensor, Direction.LEFT);
//		this.robot.addDistanceSensor(frontSensor, Direction.FORWARD);
//	}

    /**
     * Assigns a robot platform to the driver.
     * The driver uses a robot to perform, this method provides it with this necessary information.
     * @param r robot to operate
     */
    @Override
    public void setRobot(Robot r) {
        robot = r;
        initialEnergy = robot.getBatteryLevel();
    }

    /**
     * Provides the robot driver with the maze information.
     * Only some drivers such as the wizard rely on this information to find the exit.
     * @param m represents the maze, must be non-null and a fully functional maze object.
     */
    @Override
    public void setMaze(Maze m) {
        leftSensor = new BasicSensor(m, Robot.Direction.LEFT, robot);
        frontSensor = new BasicSensor(m, Robot.Direction.FORWARD, robot);
        robot.addDistanceSensor(leftSensor, Robot.Direction.LEFT);
        robot.addDistanceSensor(frontSensor, Robot.Direction.FORWARD);
    }

    /**
     * Drives the robot towards the exit following
     * its solution strategy and given the exit exists and
     * given the robot's energy supply lasts long enough.
     * When the robot reached the exit position and its forward
     * direction points to the exit the search terminates and
     * the method returns true.
     * {@code if(atTheExit && DirecionIscorrect) return true}
     * If the robot failed due to lack of energy or crashed, the method
     * throws an exception.
     * {@code if (getBatteryLevel<0) throw new exc}
     * If the method determines that it is not capable of finding the
     * exit it returns false, for instance, if it determines it runs
     * in a cycle and can't resolve this.
     * @return true if driver successfully reaches the exit, false otherwise
     * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
     */
    @Override
    public boolean drive2Exit() throws Exception {
        while (drive1Step2Exit()) {
        }
        // If at exit:
        if (robot.getBatteryLevel()==0.0f) {
            throw new Exception();
        }
        else if (robot.canSeeThroughTheExitIntoEternity(Robot.Direction.FORWARD)) {
            return true;
        }
        else {
            if (robot.getBatteryLevel()==0.0f) {
                throw new Exception();
            }
            else if (robot.canSeeThroughTheExitIntoEternity(Robot.Direction.LEFT)) {
                if (robot.getBatteryLevel()>=3.0f) {
                    robot.rotate(Robot.Turn.LEFT);
                    return true;
                }
                else {
                    throw new Exception();
                }
            }
            else {
                if (robot.getBatteryLevel()>=3.0f) {
                    robot.rotate(Robot.Turn.RIGHT);
                    return true;
                }
                else {
                    throw new Exception();
                }
            }
        }
    }

    @Override
    public void UpdateInfo() throws Exception {
    }

    /**
     * Drives the robot one step towards the exit following
     * its solution strategy and given the exists and
     * given the robot's energy supply lasts long enough.
     * It returns true if the driver successfully moved
     * the robot from its current location to an adjacent
     * location.
     * At the exit position, it rotates the robot
     * such that if faces the exit in its forward direction
     * and returns false.
     * If the robot failed due to lack of energy or crashed, the method
     * throws an exception.
     * @return true if it moved the robot to an adjacent cell, false otherwise
     * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
     */
    @Override
    public boolean drive1Step2Exit() throws Exception {
        if (robot.isAtExit()) {
            return false;
        }
        // If no Left wall
        else if (NoLeftWall(robot.getCurrentPosition())) {
            if (robot.getBatteryLevel()<3.0f) {
                throw new Exception();
            }
            else {
                robot.rotate(Robot.Turn.LEFT);
                Thread.sleep(5);
                if (robot.getBatteryLevel()==0.0f) {
                    throw new Exception();
                }
                else {
                    robot.move(1);
                    Thread.sleep(5);
                    return true;
                }
            }
        }
        //ends.
        //If no front wall:
        else if (NoFrontWall(robot.getCurrentPosition())) {
            if (robot.getBatteryLevel()<4.0f) {
                throw new Exception();
            }
            else {
                robot.move(1);
                Thread.sleep(5);
                return true;
            }
        }
        //ends.
        // If has wall on both left & front side:
        else {
            if (robot.getBatteryLevel()<3.0f) {
                throw new Exception();
            }
            else {
                robot.rotate(Robot.Turn.RIGHT);
                Thread.sleep(5);
                return true;
            }
        }
    }

    /**
     * Helps to determine whether the robot reaches a position that has left wall.
     * @param currentPos provides the current position.
     * @return true if there is no wall.
     * @throws Exception to terminate the game.
     */
    private boolean NoLeftWall(int[] currentPos) throws Exception{
        if (hasenergy()) {
            if (robot.distanceToObstacle(Robot.Direction.LEFT)!=0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helps to determine whether the robot reaches a position that has a front wall.
     * @param currentPos provides the current position.
     * @return true if there is no wall.
     * @throws Exception to terminate the game.
     */
    private boolean NoFrontWall(int[] currentPos) throws Exception{
        if (hasenergy()) {
            if (robot.distanceToObstacle(Robot.Direction.FORWARD)!=0) {
                return true;
            }
        }
        return false;
    }
    /**
     * Helps to check if the robot still has energy
     * @return True if the robot still has energy
     * @throws Exception to indicate the robot is out of energy.
     */
    private boolean hasenergy() throws Exception {
        if (robot.getBatteryLevel()==0.0f) {
            throw new Exception();
        }
        return true;
    }

    /**
     * Returns the total energy consumption of the journey, i.e.,
     * the difference between the robot's initial energy level at
     * the starting position and its energy level at the exit position.
     * This is used as a measure of efficiency for a robot driver.
     * @return the total energy consumption of the journey
     * {@code initialEnergy - currentEnergyLevel;}
     */
    @Override
    public float getEnergyConsumption() {
        float EnergyConsum = initialEnergy - robot.getBatteryLevel();
        return EnergyConsum;
    }

    /**
     * Returns the total length of the journey in number of cells traversed.
     * Being at the initial position counts as 0.
     * This is used as a measure of efficiency for a robot driver.
     * @return the total length of the journey in number of cells traversed
     */
    @Override
    public int getPathLength() {
        return robot.getOdometerReading();
    }

}

