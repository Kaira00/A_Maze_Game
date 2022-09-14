package edu.wm.cs.cs301.wenxindang.generation;

/*import org.hamcrest.core.Is;
import org.junit.jupiter.params.provider.NullEnum;

import generation.CardinalDirection;
import generation.Floorplan;
import generation.Maze;
import gui.Robot.Direction;*/

/**
 * Implement DistanceSensor.
 *
 * collaborator: Floorplan
 * Is used in Robot.
 *
 * Input: a direction.
 * Output: a distance.
 *
 * careful about: IsInRoom, IsAtExit
 *
 * @author Wenxin Dang
 *
 */
public class BasicSensor implements DistanceSensor{

    /**
     * provides the maze configuration that this sensor is detecting.
     */
    private Maze maze;
    /**
     * tag this sensor with a direction to the robot.
     */
    private Robot.Direction sensorDirection;
    /**
     * notify which robot this sensor is attached to.
     */
    private Robot robot;

    /**
     * Instantiate a basic sensor
     */
    public BasicSensor() {
    }

    /**
     * Instantiate a basic sensor by providing some fixtures:
     * @param maze gives the maze that this sensor is working on.
     * @param dir gives the angle to the robot's forward direction.
     * @param robot gives the robot this sensor is attached to.
     */
    public BasicSensor(Maze maze, Robot.Direction dir, Robot robot) {
        this.maze = maze;
        this.sensorDirection = dir;
        this.robot = robot;
    }

    /**
     * Tells the distance to an obstacle (a wallboard) that the sensor
     * measures. The sensor is assumed to be mounted in a particular
     * direction relative to the forward direction of the robot.
     * Distance is measured in the number of cells towards that obstacle,
     * e.g. 0 if the current cell has a wallboard in this direction,
     * 1 if it is one step in this direction before directly facing a wallboard,
     * Integer.MaxValue if one looks through the exit into eternity.
     *
     * This method requires that the sensor has been given a reference
     * to the current maze and a mountedDirection by calling
     * the corresponding set methods with a parameterized constructor.
     *
     * @param currentPosition is the current location as (x,y) coordinates
     * @param currentDirection specifies the direction of the robot
     * @param powersupply is an array of length 1, whose content is modified
     * to account for the power consumption for sensing
     * @return number of steps towards obstacle if obstacle is visible
     * in a straight line of sight, Integer.MAX_VALUE otherwise.
     * @throws Exception with message
     * SensorFailure if the sensor is currently not operational
     * PowerFailure if the power supply is insufficient for the operation
     * @throws IllegalArgumentException if any parameter is null
     * or if currentPosition is outside of legal range
     * ({@code currentPosition[0] < 0 || currentPosition[0] >= width})
     * ({@code currentPosition[1] < 0 || currentPosition[1] >= height})
     * @throws IndexOutOfBoundsException if the powersupply is out of range
     * ({@code powersupply < 0})
     */
    @Override
    public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
            throws Exception {
        // throw exceptions
        if (IsOutsideOfRange(currentPosition)) {
            throw new IllegalArgumentException();
        }
        else if (currentPosition==null || currentDirection==null || powersupply==null ) {
            throw new IllegalArgumentException();
        }
        else if (powersupply[0]<0.0f) {
            throw new IllegalArgumentException();
        }
        //ends.
        else if (powersupply[0] >= 1.0f) {
            CardinalDirection robotCardinalDirection = currentDirection;
            CardinalDirection sensorCardinalDirection = switchToSensorFacingDirection(sensorDirection, robotCardinalDirection);
            int[] moveFowrdPosition = new int[2];
            moveFowrdPosition[0] = currentPosition[0];
            moveFowrdPosition[1] = currentPosition[1];
            int countDistance =0;
            while(maze.isValidPosition(moveFowrdPosition[0], moveFowrdPosition[1])) {
                if (maze.hasWall(moveFowrdPosition[0], moveFowrdPosition[1], sensorCardinalDirection)) {
                    return countDistance;
                }
                countDistance++;
                moveFowrdPosition = moveOneStepForward(moveFowrdPosition, sensorCardinalDirection);
            }
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    /**
     * Helps to get a sensor's CardinalDirection.
     * @param dir gives the sensor's direction (Left/Right/Front/Back) to the robot.
     * @param robotForwarDirection provides the robot's facing CardinalDirection.
     * @return CardinalDirection of the sensor.
     */
    private CardinalDirection switchToSensorFacingDirection(Robot.Direction dir, CardinalDirection robotForwarDirection) {
        CardinalDirection sensorCardinalDirection = robotForwarDirection;
        switch (dir) {
            case LEFT: {
                sensorCardinalDirection = rotate(sensorCardinalDirection);
                sensorCardinalDirection = rotate(sensorCardinalDirection);
                sensorCardinalDirection = rotate(sensorCardinalDirection);
                break;
            }
            case RIGHT: {
                sensorCardinalDirection = rotate(sensorCardinalDirection);
                break;
            }
            case FORWARD: {
                break;
            }
            case BACKWARD: {
                sensorCardinalDirection = rotate(sensorCardinalDirection);
                sensorCardinalDirection = rotate(sensorCardinalDirection);
                break;
            }
            default:
                throw new IllegalArgumentException("Unexpected value: " + dir);
        }

        return sensorCardinalDirection;
    }

    /**
     * Helps to determine the CardinalDirection with angles.
     * @param CD current CardinalDirection.
     * @return CardinalDirection after rotation.
     */
    private CardinalDirection rotate(CardinalDirection CD) {
        switch (CD) {
            case West:
                return CardinalDirection.South;
            case East:
                return CardinalDirection.North;
            case South:
                return CardinalDirection.East;
            case North:
                return CardinalDirection.West;
            default:
                break;
        }
        return CD;
    }

    /**
     * Helps to check whether the current position is outside of the maze.
     * @param currentPosition provides the current postion that we want to check.
     * @return  T/F the current position is outside of the maze.
     */
    private boolean IsOutsideOfRange(int[] currentPosition) {
        if (maze.isValidPosition(currentPosition[0], currentPosition[1])) {
            return false;
        }
        return true;
    }

    /**
     * This method helps to move only one step forward.
     *
     * @param oldposition indicates the position before a move.
     * @param dir gives the facingDirection.
     * @return a new position after the move.
     */
    private int[] moveOneStepForward(int[] oldposition, CardinalDirection dir) {
        int[] newPosition = new int[2];
        switch (dir) {
            case West: {
                newPosition[0] = oldposition[0] - 1;
                newPosition[1] = oldposition[1];
                break;
            }
            case East: {
                newPosition[0] = oldposition[0] + 1;
                newPosition[1] = oldposition[1];
                break;
            }
            case North: {
                newPosition[0] = oldposition[0];
                newPosition[1] = oldposition[1] - 1;
                break;
            }
            case South: {
                newPosition[0] = oldposition[0];
                newPosition[1] = oldposition[1] + 1;
                break;
            }
            default:
                throw new IllegalArgumentException("Unexpected value: " + dir);
        }
        return newPosition;
    }

    /**
     * Provides the maze information that is necessary to make
     * a DistanceSensor able to calculate distances.
     * @param maze the maze for this game
     * @throws IllegalArgumentException if parameter is null
     * or if it does not contain a floor plan
     */
    @Override
    public void setMaze(Maze maze) {
        if (maze == null || maze.getFloorplan() == null) {
            throw new IllegalArgumentException();
        }
        this.maze = maze;
    }

    /**
     * Provides the angle, the relative direction at which this
     * sensor is mounted on the robot.
     * If the direction is left, then the sensor is pointing
     * towards the left hand side of the robot at a 90 degree
     * angle from the forward direction.
     * @param mountedDirection is the sensor's relative direction
     * @throws IllegalArgumentException if parameter is null
     */
    @Override
    public void setSensorDirection(Robot.Direction mountedDirection) {
        if (mountedDirection == null) {
            throw new IllegalArgumentException();
        }
        this.sensorDirection = mountedDirection;
    }

    /**
     * Returns the amount of energy this sensor uses for
     * calculating the distance to an obstacle exactly once.
     * This amount is a fixed constant for a sensor.
     * @return the amount of energy used for using the sensor once
     */
    @Override
    public float getEnergyConsumptionForSensing() {
        return 1.0f;
    }

    @Override
    public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

}

