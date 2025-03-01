package frc.robot.subsystems;

import com.revrobotics.spark.*;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.RobotMap.CoralConstants.CANID;
/*
import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.SparkPIDController.AccelStrategy;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.SparkPIDController;
import com.revrobotics.RelativeEncoder;
*/
import edu.wpi.first.wpilibj.DigitalInput;

import frc.robot.RobotMap.CoralConstants.State;
import frc.robot.RobotMap.CoralConstants.CANID;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.IO;
import frc.robot.IO.OperatorHID;
import frc.robot.IO.OperatorXbox;

public class CoralHandlerSubsystem {
    /** Class Variables */
    //private int stateCurrent = State_CS.UNKNOWN;    // Collector/Shooter Current State
    private int countLoop = 0; 
    private int stateCurrent = State.UNKNOWN;

    /** Coral Mover CAN Spark Flex Motor */
    private SparkMax coralMover = new SparkMax(CANID.kCANCoralIntake, SparkLowLevel.MotorType.kBrushless);
    private SparkMaxConfig config_ = new SparkMaxConfig();
    private double spd = 0.0;
    private double inputSpd = 0.0;
    private boolean flag1;
    private boolean flag2;
    private String labelSensor;
   
 /** Note Sensor Class*/
    
    /** Coral Sensor components **/   
    private static final int numSensors = 5;

    private static DigitalInput[] sensorCoral = new DigitalInput[] 
        {new DigitalInput(5), new DigitalInput(6), new DigitalInput(7),
        new DigitalInput(8),new DigitalInput(9)};

    public static boolean isCoralDetected() {   // change to private later
        boolean flag = false;
        for (int i=1; i <= numSensors; i++) {
            if (getCoralSensor(i)) flag = true;
        }
        SmartDashboard.putBoolean("Coral Det.", flag);
        return flag;
    }

    public static boolean getCoralSensor(int sns) {  // change to private later
        if (sns < 1 || sns > numSensors) {
            return false;
        } else {
            return !sensorCoral[sns-1].get();
        }
    }
   
    /** Init Subsystem **/
    public void init() {
        stateCurrent = State.OFF;

        //coralMover.restoreFactoryDefaults();   
        coralMover.clearFaults();
        config_.idleMode(SparkMaxConfig.IdleMode.kBrake);
        coralMover.configure(config_, SparkBase.ResetMode.kResetSafeParameters, SparkBase.PersistMode.kPersistParameters);
    }

    public void execute() {
        //SmartDashboard.putBoolean("A Button", IO.OperatorXbox.isAPressed());
        inputSpd = IO.OperatorXbox.getLeftX();
        if (Math.abs(inputSpd)<.05) {inputSpd = 0.0;}
        SmartDashboard.putNumber("X Axis", inputSpd);
        if (inputSpd != 0.0) {
            spd = inputSpd;
            stateCurrent = State.MANUAL;
        }
        flag1 = isCoralDetected();
        for (Integer i = 1; i <= numSensors; i++) {
            flag2 = getCoralSensor(i);
            labelSensor = "Coral" + i;
            SmartDashboard.putBoolean(labelSensor, flag2);
        }

        SmartDashboard.putNumber("Coral ST", stateCurrent);
        SmartDashboard.putNumber("loop", countLoop);
        countLoop++;
        if (countLoop > 2000) {countLoop = 0;}
        switch(stateCurrent){
            case State.UNKNOWN:
            case State.OFF:
                inputSpd = 0;
                stateCurrent = State.OFF;
                if (IO.OperatorXbox.isXPressed()) {
                    countLoop = 0;
                    stateCurrent = State.CORAL_INTAKE;
                }
                break;
            case State.MANUAL:
                spd = inputSpd;
                if (spd == 0.0) {
                    stateCurrent = State.OFF;
                }
                break;
            case State.CORAL_INTAKE:    // Run intake until we grab Coral
                spd = .25;
                if (isCoralDetected()) {
                    stateCurrent = State.HAVE_CORAL;
                }
                break;
            case State.HAVE_CORAL:      // Have Coral; get it aligned in collector
                if (getCoralSensor(1)) {
                    if (getCoralSensor(5)) {   // Aligned
                        spd = 0;
                        stateCurrent = State.CORAL_READY;
                        break;
                    }
                    if (getCoralSensor(4)) {         // Slow down
                        spd = .1;
                        break;
                    }
                    if (getCoralSensor(3)) {         // Start slow down
                        spd = .2;
                        break;
                    }
                    spd = .25;                  
                }
                else {
                    spd = -.1;
                }
                break;
            case State.CORAL_READY:
                spd = 0;
                if (IO.OperatorXbox.isYPressed()) {
                    countLoop = 0;
                    stateCurrent = State.DEPOSIT_CORAL;
                }
                break;
            case State.DEPOSIT_CORAL:
                spd = .2;
                if (countLoop > 30) {
                    spd = 0;
                    stateCurrent = State.OFF;
                }
                break;
            default:
                spd = 0;
                break;                
        }

        coralMover.set(spd);
    }
    
}
