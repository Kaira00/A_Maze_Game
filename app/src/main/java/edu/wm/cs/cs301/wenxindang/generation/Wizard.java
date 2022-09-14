package edu.wm.cs.cs301.wenxindang.generation;

/*package gui;

import generation.CardinalDirection;
import generation.Floorplan;
import generation.Maze;
import gui.Robot.Direction;
import gui.Robot.Turn;*/

/**
 * Implement RobotDriver with Wizard algorithm, using the floorplan.
 *
 * @author Wenxin Dang
 *
 */
public class Wizard implements RobotDriver{

    /**
     * provides the robot that this RobotDriver is driving.
     */
    private Robot robot;
    /**
     * provides the maze configuration this RobbotDriver is operating on.
     */
    private Maze maze;
    /**
     * record the initialenergy of a robot.
     */
    private float initialEnergy;
    /**
     * Record the position before a move;
     */
    private int[] oldPos = new int[2];
    /**
     * Record the position after a move;
     */
    private int[] expectedPos = new int[2];
    /**
     * Record the facing direction of the robot that the driver is operating.
     */
    private CardinalDirection facingDirection;
    //sensors
    /**
     * set left sensor to a robot.
     */
    private DistanceSensor leftSensor;
    /**
     * set a front sensor to a robot.
     */
    private DistanceSensor frontSensor;
    /**
     * set a front sensor to a robot.
     */
    private DistanceSensor backSensor;
    /**
     * set a front sensor to a robot.
     */
    private DistanceSensor rightSensor;

    /**
     * set a robot driver with Wizard algorithm.
     */
    public Wizard() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Assigns a robot platform to the driver.
     * The driver uses a robot to perform, this method provides it with this necessary information.
     * @param r robot to operate
     */
    @Override
    public void setRobot(Robot r) {
        this.robot = r;
        initialEnergy = robot.getBatteryLevel();
    }

    /**
     * Provides the robot driver with the maze information.
     * Only some drivers such as the wizard rely on this information to find the exit.
     * @param m represents the maze, must be non-null and a fully functional maze object.
     */
    @Override
    public void setMaze(Maze m) {
        this.maze = m;
        // attach sensors to robot
        leftSensor = new BasicSensor(m, Robot.Direction.LEFT, robot);
        frontSensor = new BasicSensor(m, Robot.Direction.FORWARD, robot);
        backSensor = new BasicSensor(m, Robot.Direction.BACKWARD, robot);
        rightSensor = new BasicSensor(m, Robot.Direction.RIGHT, robot);
        robot.addDistanceSensor(leftSensor, Robot.Direction.LEFT);
        robot.addDistanceSensor(frontSensor, Robot.Direction.FORWARD);
        robot.addDistanceSensor(backSensor, Robot.Direction.BACKWARD);
        robot.addDistanceSensor(rightSensor, Robot.Direction.RIGHT);
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
        while (!robot.isAtExit()) {
            oldPos = robot.getCurrentPosition();
            facingDirection = robot.getCurrentDirection();
            expectedPos = maze.getNeighborCloserToExit(oldPos[0], oldPos[1]);
//			System.out.println("The oldpos is: " + oldPos[0]+oldPos[1]);
//			System.out.println("The expect is: " + expectedPos[0]+expectedPos[1]);
            if (!drive1Step2Exit()) {
                break;
            }
        }
        if (robot.getBatteryLevel()==0.0f) {
            throw new Exception();
        }
        else if (!robot.canSeeThroughTheExitIntoEternity(Robot.Direction.FORWARD)) {
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
        return true;
    }

    @Override
    public void UpdateInfo() throws Exception {
        oldPos = robot.getCurrentPosition();
        facingDirection = robot.getCurrentDirection();
        expectedPos = maze.getNeighborCloserToExit(oldPos[0], oldPos[1]);
    }


    /**
     * Helps to determine the BatteryLevel.
     * @return true id the left BatteryLevel is sufficient to perform a rotate.
     * @throws Exception to terminate the game.
     */
    private boolean hasEnergyToRotate() throws Exception{
        if (robot.getBatteryLevel()<3.0f) {
            throw new Exception();
        }
        return true;
    }

    /**
     * Helps to determine the BatteryLevel.
     * @return true id the left BatteryLevel is sufficient to perform a move.
     */
    private boolean hasEnergyToMove(){
        if (robot.getBatteryLevel()<4.0f) {
            return false;
        }
        return true;
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
        // first rotate:
        CardinalDirection expeCardinalDirection = switchPosToCardinalDirection(oldPos, expectedPos);
        if (expeCardinalDirection!=facingDirection) {
            if (!hasEnergyToRotate()) {
                throw new Exception();
            }
            rotateToCorrectCardinalD(facingDirection,expeCardinalDirection);
        }
        //ends rotate.
        //then move:
        if (!hasEnergyToMove()) {
            throw new Exception();
        }
//		System.out.println("FacingD is: "+ facingDirection);
//		System.out.println("ExpectedD is: "+ expeCardinalDirection);
        facingDirection = expeCardinalDirection;
        robot.move(1);
        // ends move.
        return true;
    }

    /**
     * Only if oldCD != newCD, this method will be called.
     * @param oldCD old CardinalDirection
     * @param newCaD the CardinalDirection we want the robot to turn to.
     * @throws Exception if want to turn around but lack of energy.
     */
    private void rotateToCorrectCardinalD(CardinalDirection oldCD, CardinalDirection newCaD) throws Exception {
        CardinalDirection checkCD = rotate(oldCD);
        if (checkCD==newCaD) {
            robot.rotate(Robot.Turn.RIGHT);
        }
        else {
            checkCD = rotate(checkCD);
            if (checkCD==newCaD) {
                robot.rotate(Robot.Turn.RIGHT);
                if (hasEnergyToRotate()) {
                    robot.rotate(Robot.Turn.RIGHT);
                }
            }
            else {
                robot.rotate(Robot.Turn.LEFT);
            }
        }
    }

    /**
     * Helps to determine the CardinalDirection with angles.
     * @param CD current CardinalDirection.
     * @return CardinalDirection after rotation.
     */
    private CardinalDirection rotate(CardinalDirection CD) {
        CardinalDirection returnCardinalDirection  = null;
        switch (CD) {
            case West:
                returnCardinalDirection = CardinalDirection.South;
                break;
            case East:
                returnCardinalDirection =CardinalDirection.North;
                break;
            case South:
                returnCardinalDirection = CardinalDirection.East;
                break;
            case North:
                returnCardinalDirection = CardinalDirection.West;
                break;
            default:
                break;
        }
        return returnCardinalDirection;
    }

    /**
     * gives the CardinalDirection if we want to move from one location to another.
     * @param oldp indicates the old position.
     * @param newp indicates the new position.
     * @return CardinalDirection if the driver want to move from old to new.
     */
    private CardinalDirection switchPosToCardinalDirection(int[] oldp, int[] newp) {
        if (newp[0] == oldp[0]+1) {
            return CardinalDirection.East;
        }
        else if (newp[0] == oldp[0]-1) {
            return CardinalDirection.West;
        }
        else if (newp[1] == oldp[1]+1) {
            return CardinalDirection.South;
        }
        else {
            return CardinalDirection.North;
        }
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

