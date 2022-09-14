package edu.wm.cs.cs301.wenxindang.generation;

import java.sql.Driver;

public class DataHolder {
    private Controller controller = new Controller();
    private int seed;
    private RobotDriver driver;
    private static DataHolder holder = new DataHolder();

    /*pubklic DataHolder() {}*/
    public static DataHolder getInstance() {return holder;}

    public void setBuilder(String inputbuilder){
        switch (inputbuilder){
            case "DFS":
                controller.setBuilder(Order.Builder.DFS);
            case "Prim":
                controller.setBuilder(Order.Builder.Prim);
            case "Kruskal":
                controller.setBuilder(Order.Builder.Kruskal);
            default:
                controller.setBuilder(Order.Builder.DFS);
        }

    }

    public void setPerfect(boolean isPerfect){
        controller.setPerfect(isPerfect);
    }

    /*public void setSeed(){
        SingleRandom sr = SingleRandom.getRandom();
        seed = sr.nextInt();
        controller.setSeed(seed);
    }*/
    public void setSeed(int inputseed){
        controller.setSeed(inputseed);
    }
    public int getSeed(){
        return seed;
    }

    public void setskillLevel(int skillLevel){
        controller.setSkillLevel(skillLevel);
    }

    public void setRobotAndDriver(String inputdriver){
        Robot robot = new BasicRobot();
        switch (inputdriver){
            case "animation: Wizard":
                driver = new Wizard();
                controller.setRobotAndDriver(robot, driver);
                break;
            case "animation: WallFollower":
                driver = new WallFollower();
                controller.setRobotAndDriver(robot, driver);
                break;
            case "manual": default:
                break;
        }
    }
    public Robot getRobot(){
        return controller.getRobot();
    }

    public RobotDriver getDriver(){
        return controller.getDriver();
    }

    public void setState(Constants.State state){
        controller.setState(state);
    }

    public Controller getController() {
        return controller;
    }

    public void startNewDataHolder() {
        holder = new DataHolder();
    }
}
