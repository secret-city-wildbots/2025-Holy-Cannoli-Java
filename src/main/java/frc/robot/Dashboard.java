package frc.robot;

import edu.wpi.first.networktables.BooleanArrayPublisher;
import edu.wpi.first.networktables.BooleanArraySubscriber;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.BooleanSubscriber;
import edu.wpi.first.networktables.DoubleArrayPublisher;
import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.IntegerArraySubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringArrayPublisher;
import edu.wpi.first.networktables.StringPublisher;
import edu.wpi.first.networktables.StringSubscriber;

public class Dashboard {
    @SuppressWarnings("unused")
    private NetworkTableInstance inst;
    @SuppressWarnings("unused")
    private NetworkTable table;

    // Drivebase Subsystem
    public static DoubleArrayPublisher swerve0Details;
    public static DoubleArrayPublisher swerve1Details;
    public static DoubleArrayPublisher swerve2Details;
    public static DoubleArrayPublisher swerve3Details;
    public static BooleanArraySubscriber swerveDisables;
    public static DoubleArrayPublisher currentDriverProfileSetpoints;
    public static StringArrayPublisher legalDrivers;
    
    public static DoublePublisher robotX;
    public static DoublePublisher robotY;
    public static DoublePublisher robotLengthBumpers;
    public static DoublePublisher robotWidthBumpers;
    public static DoublePublisher robotHeading;    

    public static DoubleArraySubscriber newDriverProfileSetpoints;
    
    public static DoubleSubscriber selectedDriver;
    public static BooleanSubscriber unlockAzimuth;
    public static BooleanSubscriber homeWheels;
    public static BooleanSubscriber calibrateWheels;
    public static BooleanSubscriber applyProfileSetpoints;

    // Limelight


    // Other Testing
    public static DoubleArrayPublisher pidTuningGoalActual;
    public static StringArrayPublisher legalActuatorNames;
    public static BooleanArrayPublisher confirmedMasterStates;

    public static DoublePublisher pressureTransducer;
    public static DoublePublisher loopTime;
    public static StringPublisher robotProfile;
    public static StringPublisher codeVersion;
    public static BooleanPublisher isAutonomous;
    
    public static DoubleSubscriber testActuatorValue;
    public static DoubleSubscriber testActuatorPeriod;
    public static DoubleSubscriber freeTuningVariable;
    public static DoubleSubscriber freeTuningkP;
    public static DoubleSubscriber freeTuningkI;
    public static DoubleSubscriber freeTuningkD;
    public static StringSubscriber testActuatorName;

    // Button binding testing
    public static DoubleArraySubscriber motorAmplitudes;
    public static DoubleArraySubscriber motorMaxPIDAngles;
    public static DoubleArraySubscriber motorMinPIDAngles;
    public static BooleanArraySubscriber motorPIDEnabled;
    public static DoubleArraySubscriber motorGearRatios;
    public static DoubleArraySubscriber motorMaxSpeeds;
    public static DoubleArraySubscriber motorPIDs;
    public static DoubleArraySubscriber followerCANIDs;
    public static DoubleArraySubscriber motorCANIDs;
    public static IntegerArraySubscriber motorTypes;
    public static IntegerArraySubscriber motorSelectorStates;

    /**
     * Creates an object for storing dashboard publishers and subscribers
     */
    public Dashboard() {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("SmartDashboard");

        // Drivebase subsystem
        swerve0Details = table.getDoubleArrayTopic("Swerve_0_Details").publish();
        swerve1Details = table.getDoubleArrayTopic("Swerve_1_Details").publish();
        swerve2Details = table.getDoubleArrayTopic("Swerve_2_Details").publish();
        swerve3Details = table.getDoubleArrayTopic("Swerve_3_Details").publish();
        swerveDisables = table.getBooleanArrayTopic("Swerve_Disables").subscribe(new boolean[]{false, false, false, false});
        currentDriverProfileSetpoints = table.getDoubleArrayTopic("Current_Driver_Profile_Setpoints").publish();
        newDriverProfileSetpoints = table.getDoubleArrayTopic("New_Driver_Profile_Setpoints")
                .subscribe(new double[] { 0.08, 1.8, 1, 0.15, 2.5, 1 });
        legalDrivers = table.getStringArrayTopic("Legal_Drivers").publish();

        motorAmplitudes = table.getDoubleArrayTopic("Motor_Amplitudes").subscribe(new double[8]);
        motorSelectorStates = table.getIntegerArrayTopic("Motor_Selector_States").subscribe(new long[8]);
        motorMaxPIDAngles = table.getDoubleArrayTopic("Max_Angle_Motor_PID").subscribe(new double[8]);
        motorMinPIDAngles = table.getDoubleArrayTopic("Min_Angle_Motor_PID").subscribe(new double[8]);
        motorPIDEnabled = table.getBooleanArrayTopic("Motor_PID_Enabled").subscribe(new boolean[8]);
        motorGearRatios = table.getDoubleArrayTopic("Motor_Ratios").subscribe(new double[8]);
        motorMaxSpeeds = table.getDoubleArrayTopic("Motor_Max_Speeds").subscribe(new double[8]);
        motorPIDs = table.getDoubleArrayTopic("Motor_PIDs").subscribe(new double[3*8]);
        followerCANIDs = table.getDoubleArrayTopic("Follower_CAN_IDs").subscribe(new double[4*8]);
        motorCANIDs = table.getDoubleArrayTopic("Motor_CAN_IDs").subscribe(new double[8]);
        motorTypes = table.getIntegerArrayTopic("Motor_Types").subscribe(new long[8]);
        
        robotX = table.getDoubleTopic("Robot_X").publish();
        robotY = table.getDoubleTopic("Robot_Y").publish();
        robotHeading = table.getDoubleTopic("Robot_H").publish();
        robotLengthBumpers = table.getDoubleTopic("Robot_Length_Bumpers").publish();
        robotWidthBumpers = table.getDoubleTopic("Robot_Width_Bumpers").publish();

        selectedDriver = table.getDoubleTopic("Selected_Driver").subscribe(0.0);
        unlockAzimuth = table.getBooleanTopic("Unlock_Azimuth").subscribe(false);
        homeWheels = table.getBooleanTopic("Home_Wheels").subscribe(false);
        calibrateWheels = table.getBooleanTopic("Calibrate_Wheels").subscribe(false);
        applyProfileSetpoints = table.getBooleanTopic("Apply_Driver_Profile_Setpoints").subscribe(false);

        
        // Other testing
        pidTuningGoalActual = table.getDoubleArrayTopic("PID_Tuning_GoalActual").publish();
        legalActuatorNames = table.getStringArrayTopic("Legal_Actuator_Names").publish();
        confirmedMasterStates = table.getBooleanArrayTopic("Confirmed_States").publish();

        pressureTransducer = table.getDoubleTopic("Pressure_Transducer").publish();
        loopTime = table.getDoubleTopic("Control_Loop_Time").publish();
        robotProfile = table.getStringTopic("Robot_Profile").publish();
        codeVersion = table.getStringTopic("Robot_Code_Version").publish();
        isAutonomous = table.getBooleanTopic("Robot_is_Autonomous").publish();

        testActuatorValue = table.getDoubleTopic("Test_Actuator_Value").subscribe(0.0);
        testActuatorPeriod = table.getDoubleTopic("Test_Actuator_Period").subscribe(0.0);
        freeTuningVariable = table.getDoubleTopic("Free_Tuning_Variable").subscribe(0);
        freeTuningkP = table.getDoubleTopic("Free_Tuning_PID_P").subscribe(0);
        freeTuningkI = table.getDoubleTopic("Free_Tuning_PID_I").subscribe(0);
        freeTuningkD = table.getDoubleTopic("Free_Tuning_PID_D").subscribe(0);
        testActuatorName = table.getStringTopic("Test_Actuator_Name").subscribe("");
    }
}
