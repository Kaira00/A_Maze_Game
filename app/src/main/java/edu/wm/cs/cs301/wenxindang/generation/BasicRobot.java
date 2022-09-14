package edu.wm.cs.cs301.wenxindang.generation;
/*
package gui;

import generation.CardinalDirection;
import gui.Constants.UserInput;*/

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.*;
import java.util.Map;

/**
 * Implement Robot.
 *
 * Collaborate with Controller (specifically the StatePlaying).
 * and DistanceSensor.
 *
 * Is used in RobotDriver.
 *
 * @author Wenxin Dang
 *
 */
public class BasicRobot implements Robot{
    /**
     * set a controller.
     */
    private Controller controller;
    /**
     * track the current position of the robot.
     */
    private int[] currentPosition = new int[2];
    /**
     * track the CardinalDirection of the robot.
     */
    private CardinalDirection currentFacingDirection;
    /**
     * record the Batterylevel
     */
    private float BatteryLevel;
    /**
     * record the pathlength.
     */
    private int odometer;
    /**
     * stopped is different from BatteryLevel;
     * if stopped, cannot move, only can rotate or sense;
     */
    private boolean stopped; // different from BatteryLevel;


    /**
     * store the sensors in a map.
     *
     */
    private Map<Direction, DistanceSensor> DSensorMap = new HashMap<Direction, DistanceSensor>();

    /**
     * Provides a robot to operate with.
     *
     * Is used in RobotDriver.
     */
    public BasicRobot() {
        currentPosition[0] = 0;
        currentPosition[1] = 0;
        BatteryLevel = 2000.0f;
        odometer = 0;
        stopped = false;
        currentFacingDirection = CardinalDirection.East;

        // instantiate the SensorMap
        DSensorMap.put(Direction.FORWARD,null);
        DSensorMap.put(Direction.BACKWARD,null);
        DSensorMap.put(Direction.LEFT,null);
        DSensorMap.put(Direction.RIGHT,null);

    }

    /**
     * if (controller == null) {
     * 		throw IllegalArgumentException;
     * }
     * this.controller = controller;
     * @param inputcontroller is the communication partner for robot
     * @throws IllegalArgumentException if controller is null,
     * or if controller is not in playing state,
     * or if controller does not have a maze
     */
    @Override
    public void setController(Controller inputcontroller){
        if (inputcontroller == null) {
//			System.out.println("here1");
            throw new IllegalArgumentException();
        }
        if (inputcontroller.getMazeConfiguration() == null) {
//			System.out.println("here2");
            throw new IllegalArgumentException();
        }
/*        if (!(inputcontroller.currentState instanceof StatePlaying)) {
//			System.out.println("here3");
            throw new IllegalArgumentException();
        }*/
        this.controller = inputcontroller;
        int[] startPos = inputcontroller.getCurrentPosition();
        currentPosition[0] = startPos[0];
        currentPosition[1] = startPos[1];
    }

    /**
     * Instantiate a sensor, and add it to the robot.
     * @param sensor is the distance sensor to be added
     * @param mountedDirection is the direction that it points to relative to the robot's forward direction
     */
    @Override
    public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
        DSensorMap.put(mountedDirection, sensor);
    }

    /**
     * Provides the current position as (x,y) coordinates for
     * the maze as an array of length 2 with [x,y].
     * @return array of length 2, x = array[0], y = array[1]
     * and ({@code 0 <= x < width, 0 <= y < height}) of the maze
     * @throws Exception if position is outside of the maze
     */
    @Override
    public int[] getCurrentPosition() throws Exception {
        return this.currentPosition;
    }

    /**
     * Provides the robot's current direction.
     * @return cardinal direction is the robot's current direction in absolute terms
     */
    @Override
    public CardinalDirection getCurrentDirection() {
        return this.currentFacingDirection;
    }

    /**
     * Returns the current battery level.
     * The robot has a given battery level (energy level)
     * that it draws energy from during operations.
     * The particular energy consumption is device dependent such that a call
     * for sensor distance2Obstacle may use less energy than a move forward operation.
     * If battery {@code level <= 0} then robot stops to function and hasStopped() is true.
     * @return current battery level, {@code level > 0} if operational.
     */
    @Override
    public float getBatteryLevel() {
        return this.BatteryLevel;
    }

    /**
     * modify BatteryLevel according to the action performed.
     * If battery {@code level <= 0} then robot stops to function and hasStopped() is true.
     * @param level is the current battery level
     * @throws IllegalArgumentException if level is negative
     */
    @Override
    public void setBatteryLevel(float level) {
        if (level <= 0) {
            this.stopped = true;
        }
        if (level<0) {
            throw new IllegalArgumentException();
        }
        this.BatteryLevel = level;
    }

    /**
     * Gives the energy consumption for a full 360 degree rotation.
     * Scaling by other degrees approximates the corresponding consumption.
     * @return energy for a full rotation
     */
    @Override
    public float getEnergyForFullRotation() {
        return 12.0f;
    }

    /**
     * Gives the energy consumption for moving forward for a distance of 1 step.
     * For simplicity, we assume that this equals the energy necessary
     * to move 1 step and that for moving a distance of n steps
     * takes n times the energy for a single step.
     * @return energy for a single step forward
     */
    @Override
    public float getEnergyForStepForward() {
        return 4.0f;
    }

    /**
     * Gets the distance traveled by the robot.
     * The robot has an odometer that calculates the distance the robot has moved.
     * Whenever the robot moves forward, the distance
     * that it moves is added to the odometer counter.
     * The odometer reading gives the path length if its setting is 0 at the start of the game.
     * The counter can be reset to 0 with resetOdomoter().
     * @return the distance traveled measured in single-cell steps forward
     */
    @Override
    public int getOdometerReading() {
        return this.odometer;
    }

    /**
     * Resets the odometer counter to zero.
     * The robot has an odometer that calculates the distance the robot has moved.
     * Whenever the robot moves forward, the distance
     * that it moves is added to the odometer counter.
     * The odometer reading gives the path length if its setting is 0 at the start of the game.
     */
    @Override
    public void resetOdometer() {
        this.odometer = 0;
    }

    /**
     * Turn robot on the spot for amount of degrees.
     * If robot runs out of energy, it stops,
     * which can be checked by hasStopped() == true and by checking the battery level.
     * @param turn is the direction to turn and relative to current forward direction.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void rotate(Turn turn) {
        if (this.BatteryLevel > 0) {
            if (this.BatteryLevel >= 3) {
                switch (turn) {
                    case LEFT: {
                        currentFacingDirection = rotate(currentFacingDirection);
                        currentFacingDirection = rotate(currentFacingDirection);
                        currentFacingDirection = rotate(currentFacingDirection);
                        BatteryLevel = BatteryLevel - 3.0f;
                        controller.keyDown(Constants.UserInput.LEFT, 1);
                        break;
                    }
                    case RIGHT: {
                        currentFacingDirection = rotate(currentFacingDirection);
                        BatteryLevel = BatteryLevel - 3.0f;
                        controller.keyDown(Constants.UserInput.RIGHT, 1);
                        break;
                    }
                    case AROUND: {
                        if (this.BatteryLevel < 6) {
                            this.stopped = true;
                            break;
                        }
                        currentFacingDirection = rotate(currentFacingDirection);
                        currentFacingDirection = rotate(currentFacingDirection);
                        BatteryLevel = BatteryLevel - 6.0f;
                        controller.keyDown(Constants.UserInput.RIGHT, 1);
                        controller.keyDown(Constants.UserInput.RIGHT, 1);
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("Unexpected value: " + turn);
                }
            }
            else {
                this.stopped = true;
            }
        }
        else {
            this.stopped = true;
        }

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
     * Moves robot forward a given number of steps. A step matches a single cell.
     * If the robot runs out of energy somewhere on its way, it stops,
     * which can be checked by hasStopped() == true and by checking the battery level.
     * If the robot hits an obstacle like a wall, it remains at the position in front
     * of the obstacle and also hasStopped() == true as this is not supposed to happen.
     * This is also helpful to recognize if the robot implementation and the actual maze
     * do not share a consistent view on where walls are and where not.
     * @param distance is the number of cells to move in the robot's current forward direction
     * @throws IllegalArgumentException if distance not positive
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void move(int distance) {
        int copydistance = distance;
        if (distance>0) {
            while (copydistance>0 && stopped == false) {
                if (BatteryLevel>=4.0f) {
                    if (controller.getMazeConfiguration().hasWall(currentPosition[0], currentPosition[1], currentFacingDirection)) {
                        stopped = true;
                        System.out.println("Robot/move wrong, different sensing result");
                    }
                    this.currentPosition = moveOneStepForward(this.currentPosition, currentFacingDirection);
                    controller.keyDown(Constants.UserInput.UP, 1);
                    copydistance--;
                    odometer++;
                    float currentlevel = BatteryLevel - 4.0f;
                    setBatteryLevel(currentlevel);
                }
                else {
                    stopped = true;
                }
            }
        }else {
            throw new IllegalArgumentException();
        }
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
//
//	private CardinalDirection switchTosensorCardinalDirectionOnFloorplan(CardinalDirection sensorCardinalDirection) {
//		CardinalDirection sensorCardinalDirectionOnFloorplan = null;
//		int[] tempd = sensorCardinalDirection.getDirection();
//		sensorCardinalDirectionOnFloorplan = sensorCardinalDirection.getDirection(tempd[0], tempd[1]);
//		return sensorCardinalDirectionOnFloorplan;
//	}

    /**
     * Makes robot move in a forward direction even if there is a wall
     * in front of it. In this sense, the robot jumps over the wall
     * if necessary. The distance is always 1 step and the direction
     * is always forward.
     * If the robot runs out of energy somewhere on its way, it stops,
     * which can be checked by hasStopped() == true and by checking the battery level.
     * If the robot tries to jump over an exterior wall and
     * would land outside of the maze that way,
     * it remains at its current location and direction,
     * hasStopped() == true as this is not supposed to happen.
     */
    @Override
    public void jump() {
        if (hasStopped()==false) {
            int[] intendPosition = moveOneStepForward(currentPosition, currentFacingDirection);
            if (controller.getMazeConfiguration().isValidPosition(intendPosition[0], intendPosition[1])) {
                this.currentPosition = intendPosition;
                odometer++;
            }
        }
    }

    /**
     * Tells if the current position is right at the exit but still inside the maze.
     * The exit can be in any direction. It is not guaranteed that
     * the robot is facing the exit in a forward direction.
     * @return true if robot is at the exit, false otherwise
     */
    @Override
    public boolean isAtExit() {
        return controller.getMazeConfiguration().getFloorplan().isExitPosition(currentPosition[0], currentPosition[1]);
    }

    /**
     * Tells if current position is inside a room.
     * @return true if robot is inside a room, false otherwise
     */
    @Override
    public boolean isInsideRoom() {
        return controller.getMazeConfiguration().getFloorplan().isInRoom(currentPosition[0], currentPosition[1]);
    }

    /**
     * Tells if the robot has stopped for reasons like lack of energy,
     * hitting an obstacle, etc.
     * Once a robot is has stopped, it does not rotate or
     * move anymore.
     * @return true if the robot has stopped, false otherwise
     */
    @Override
    public boolean hasStopped() {
        return this.stopped;
    }

    /**
     * calculate the distance to an obstacle.
     * @param direction specifies the direction of interest
     * @return number of steps towards obstacle if obstacle is visible
     * in a straight line of sight, Integer.MAX_VALUE otherwise
     * @throws UnsupportedOperationException if robot has no sensor in this direction
     * or the sensor exists but is currently not operational
     */
    @Override
    public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
        if (!hasDistanceSensor(direction)) {
            throw new UnsupportedOperationException();
        }
        float[] powerSupply = new float[1];
        powerSupply[0] = BatteryLevel;
        int distance = 0;
        DistanceSensor sensorInterest = DSensorMap.get(direction);
        try {
            distance = sensorInterest.distanceToObstacle(currentPosition, currentFacingDirection, powerSupply);
            BatteryLevel = BatteryLevel - 1.0f;
            return distance;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("BasicRobot/distanceToObstacle wrong");
            throw new IllegalArgumentException();
        }
    }


    /**
     * This method helps to identify whether there is a sensor on a sopecific direction.
     * @param direction provides the direction that we want to look at.
     * @return T/F have a sensor at that given direction.
     */
    private boolean hasDistanceSensor(Direction direction) {
        if (DSensorMap.get(direction) != null) {
            return true;
        }
        return false;
    }

    /**
     * Tells if a sensor can identify the exit in the given direction relative to
     * the robot's current forward direction from the current position.
     * It is a convenience method is based on the distanceToObstacle() method and transforms
     * its result into a boolean indicator.
     * @param direction is the direction of the sensor
     * @return true if the exit of the maze is visible in a straight line of sight
     * @throws UnsupportedOperationException if robot has no sensor in this direction
     * or the sensor exists but is currently not operational
     */
    @Override
    public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
        if (DSensorMap.get(direction)==null) {
            throw new UnsupportedOperationException();
        }
        else {
            if (distanceToObstacle(direction) == Integer.MAX_VALUE) {
                return true;
            }
            return false;
        }
    }

    /**
     * Optional operation. If not implemented, the method
     * throws an UnsupportedOperationException.
     * @param direction the direction the sensor is mounted on the robot
     * @param meanTimeBetweenFailures is the mean time in seconds, must be greater than zero
     * @param meanTimeToRepair is the mean time in seconds, must be greater than zero
     * @throws UnsupportedOperationException if method not supported
     */
    @Override
    public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();

    }

    /**
     * Optional operation. If not implemented, the method
     * throws an UnsupportedOperationException.
     *
     * @param direction the direction the sensor is mounted on the robot
     * @throws UnsupportedOperationException if method not supported
     */
    @Override
    public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

}

