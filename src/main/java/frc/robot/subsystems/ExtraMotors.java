package frc.robot.Subsystems;

import java.util.ArrayList;
import java.util.Arrays;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Dashboard;
import frc.robot.Utility.ExtraMotor;
import frc.robot.Utility.FollowerMotors;
import frc.robot.Utility.ExtraMotor.MotorBrand;

public class ExtraMotors {
    public static String[] buttonNames = {"Off", "Slider", "D_LT", "D_RT", "D_LB", "D_RB", "D_Y", "D_X", "D_A", "D_B", "MLJoystick_X", 
    "MLJoystick_Y", "M_RJoystick_X", "M_RJoystick_Y", "M_LT", "M_RT", "M_LB", "M_RB", "M_Y", "M_X", "M_A", "M_B"};
    private ArrayList<ExtraMotor> motorsList = new ArrayList<>(Arrays.asList());
    public ExtraMotor[] motors;
    private ArrayList<FollowerMotors> followerList = new ArrayList<>(Arrays.asList());
    public FollowerMotors[] followers;
    public double[] PIDs;
    public Boolean active = false;
    
    public ExtraMotors() {
        
    }   

    public void init() {
        for (int i = 0; i<Dashboard.motorCANIDs.get().length; i++) {
            if (Dashboard.motorCANIDs.get()[i] != 0) {
                motorsList.add(new ExtraMotor((int)Dashboard.motorCANIDs.get()[i], i, MotorBrand.values()[(int)Dashboard.motorTypes.get()[i]]));
            }
        }
        for (int i = 0; i<Dashboard.followerCANIDs.get().length/4; i++) {
            if (Dashboard.motorCANIDs.get()[i] != 0 && Dashboard.motorTypes.get()[i] == 0) {
                ArrayList<Integer> validFollowers = new ArrayList<>(Arrays.asList());
                for (int i2 = 0; i2<4; i2++) {
                    if ((int)Dashboard.followerCANIDs.get()[(i*4)+i2] != 0) {
                        validFollowers.add((int)Dashboard.followerCANIDs.get()[(i*4)+i2]);
                    }
                }
                Integer[] validFollowersArray = new Integer[validFollowers.size()];
                validFollowersArray = validFollowers.toArray(validFollowersArray);
                int[] validFollowersArrayInt = Arrays.stream(validFollowersArray).mapToInt(Integer::intValue).toArray();
                followerList.add(new FollowerMotors((int)Dashboard.motorCANIDs.get()[i], validFollowersArrayInt, MotorBrand.values()[(int)Dashboard.motorTypes.get()[i]]));
            }
        }
        if (motorsList == null) {
            motors = new ExtraMotor[0];
        } else {
            motors = motorsList.toArray(new ExtraMotor[motorsList.size()]);
        }
    }

    public void update(XboxController driverController, XboxController manipController) {
        if (Dashboard.testActuatorName.get() != "" && !active) {
            init();
            active = true;
        } else if (active) {
            updateOutputs(driverController, manipController);
        }
    }

    public void updateOutputs(XboxController driverController, XboxController manipController) {
        PIDs = Dashboard.motorPIDs.get();
        double[] motorMaxPos = Dashboard.motorMaxPIDAngles.get();
        double[] motorMinPos = Dashboard.motorMinPIDAngles.get();
        for (int motor = 0; motor < motors.length; motor++){
            if (!Dashboard.motorPIDEnabled.get()[motor]) {
                motors[motor].setPID(PIDs[motor*3],PIDs[motor*3+1],PIDs[motor*3+2],Dashboard.motorMinPIDAngles.get()[motor],Dashboard.motorMaxPIDAngles.get()[motor],Dashboard.motorMaxSpeeds.get()[motor]);
            }
            motors[motor].dc = 0;
            if (motors[motor].toggled) {
                motors[motor].dc = motors[motor].dc = Dashboard.motorAmplitudes.get()[motor];
            }
            switch (buttonNames[(int)Dashboard.motorSelectorStates.get()[motor]]) {
                case "Off":
                    motors[motor].dc = 0;
                    break;
                case "Slider":
                    motors[motor].dc = Dashboard.motorAmplitudes.get()[motor];
                    break;
                case "D_LT":
                    if (driverController.getLeftTriggerAxis() > 0.1) {
                        motors[motor].dc = driverController.getLeftTriggerAxis()*Dashboard.motorAmplitudes.get()[motor];
                    }
                    break;
                case "D_RT":
                    if (driverController.getRightTriggerAxis() > 0.1) {
                        motors[motor].dc = driverController.getRightTriggerAxis()*Dashboard.motorAmplitudes.get()[motor];
                    }
                    break;
                case "D_LB":
                    if (driverController.getLeftBumperButtonPressed()) {
                        motors[motor].toggled = !motors[motor].toggled;
                    }
                    break;
                case "D_RB":
                    if (driverController.getRightBumperButtonPressed()) {
                        motors[motor].toggled = !motors[motor].toggled;
                    }
                    break;
                case "D_Y":
                    if (driverController.getYButton()) {
                        motors[motor].dc = Dashboard.motorAmplitudes.get()[motor];
                    }
                    break;
                case "D_X":
                    if (driverController.getXButton()) {
                        motors[motor].dc = Dashboard.motorAmplitudes.get()[motor];
                    }
                    break;
                case "D_A":
                    if (driverController.getAButton()) {
                        motors[motor].dc = Dashboard.motorAmplitudes.get()[motor];
                    }
                    break;
                case "D_B":
                    if (driverController.getBButton()) {
                        motors[motor].dc = Dashboard.motorAmplitudes.get()[motor];
                    }
                    break;
                case "MLJoystick_X":
                    if (Math.abs(manipController.getLeftX()) > 0.05) {
                        motors[motor].dc = manipController.getLeftX() * Dashboard.motorAmplitudes.get()[motor];
                    }
                    break;
                case "MLJoystick_Y":
                    if (Math.abs(manipController.getLeftY()) > 0.05) {
                        motors[motor].dc = manipController.getLeftY() * Dashboard.motorAmplitudes.get()[motor] * -1;
                    }
                    break;
                case "MRJoystick_X":
                    if (Math.abs(manipController.getRightX()) > 0.05) {
                        motors[motor].dc = manipController.getRightX() * Dashboard.motorAmplitudes.get()[motor];
                    }
                    break;
                case "MRJoystick_Y":
                    if (Math.abs(manipController.getRightY()) > 0.05) {
                        motors[motor].dc = manipController.getRightY() * Dashboard.motorAmplitudes.get()[motor] * -1;
                    }
                    break;
                case "M_LT":
                    if (manipController.getLeftTriggerAxis() > 0.1) {
                        motors[motor].dc = manipController.getLeftTriggerAxis()*Dashboard.motorAmplitudes.get()[motor];
                    }
                    break;
                case "M_RT":
                    if (manipController.getRightTriggerAxis() > 0.1) {
                        motors[motor].dc = manipController.getRightTriggerAxis()*Dashboard.motorAmplitudes.get()[motor];
                    }
                    break;
                case "M_LB":
                    if (manipController.getLeftBumperButtonPressed()) {
                        motors[motor].toggled = !motors[motor].toggled;
                    }
                    break;
                case "M_RB":
                    if (manipController.getRightBumperButtonPressed()) {
                        motors[motor].toggled = !motors[motor].toggled;
                    }
                    break;
                case "M_Y":
                    if (manipController.getYButton()) {
                        motors[motor].dc = Dashboard.motorAmplitudes.get()[motor];
                    }
                    break;
                case "M_X":
                    if (manipController.getXButton()) {
                        motors[motor].dc = Dashboard.motorAmplitudes.get()[motor];
                    }
                    break;
                case "M_A":
                    if (manipController.getAButton()) {
                        motors[motor].dc = Dashboard.motorAmplitudes.get()[motor];
                    }
                    break;
                case "M_B":
                    if (manipController.getBButton()) {
                        motors[motor].dc = Dashboard.motorAmplitudes.get()[motor];
                    }
                    break;
            }
            if (Dashboard.motorPIDEnabled.get()[motor]) {
                motors[motor].spin(motors[motor].dc);
            } else {
                motors[motor].goToPos(Units.degreesToRotations(motors[motor].dc*motorMaxPos[motor]+motorMinPos[motor]),Dashboard.motorGearRatios.get()[motor],Dashboard.motorMaxSpeeds.get()[motor]);
            }
        }
    }
}