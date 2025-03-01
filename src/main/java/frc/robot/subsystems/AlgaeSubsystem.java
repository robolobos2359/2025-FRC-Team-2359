package frc.robot.subsystems;

import com.revrobotics.spark.*;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.RobotMap.AlgaeConstants.CANID;
import frc.robot.RobotMap.AlgaeConstants.State;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.IO;
import frc.robot.IO.OperatorHID;
import frc.robot.IO.OperatorXbox;

public class AlgaeSubsystem {
    /** Class Variables */
    private int stateCurrent = State.UNKNOWN;    // Algae Handler Current State
    private int countLoop = 0;
    private double inputSpd;


    /** Algae Intake CAN Spark Flex Motor */
    private SparkFlex algaeIntake = new SparkFlex(CANID.kCANAlgaeIntake, SparkLowLevel.MotorType.kBrushless);
    private SparkMaxConfig config_ = new SparkMaxConfig();
    private double spd = 0;
    /** Algae rotate CAN Spark Flex Motor */
    //private SparkFlex algaeRotator = new SparkFlex(CANID.kCANAlgaeRotate, SparkLowLevel.MotorType.kBrushless);

    // Algae Intake Switch
    private static DigitalInput sensorAlgaeIntake = new DigitalInput(4);


    public void init() {
        //stateCurrent = State_AH.OFF;

        //coralMover.restoreFactoryDefaults();   
        algaeIntake.clearFaults();
        config_.idleMode(SparkMaxConfig.IdleMode.kBrake);
        algaeIntake.configure(config_, SparkBase.ResetMode.kResetSafeParameters, SparkBase.PersistMode.kPersistParameters);
    }

    public void execute() {
        inputSpd = IO.OperatorXbox.getRightY();
        if (Math.abs(inputSpd)<.05) {inputSpd = 0.0;}
        SmartDashboard.putNumber("Y Axis", inputSpd);
        if (inputSpd != 0.0) {
            spd = inputSpd;
            stateCurrent = State.MANUAL;
        }
        if(IO.OperatorXbox.isAPressed() || IO.OperatorXbox.isLeftBumpPressed()) {
            stateCurrent = State.ALGAE_INTAKE;
        }

        SmartDashboard.putNumber("Algae ST", stateCurrent);
        countLoop++;
        if (countLoop > 2000) {countLoop = 0;}
        switch (stateCurrent) {
            case State.UNKNOWN:
                spd = 0;
                stateCurrent = State.OFF;
            case State.OFF:
                spd = 0;
                stateCurrent = State.OFF;
                if(IO.OperatorXbox.isLeftBumpPressed()) {
                    stateCurrent = State.ALGAE_INTAKE;
                }
                break;
            case State.MANUAL:
                spd = inputSpd;
                if (spd == 0.0) {
                    stateCurrent = State.OFF;
                }
                break;
            case State.ALGAE_INTAKE:
                spd = .5;
                SmartDashboard.putBoolean("Algae Intake SW", sensorAlgaeIntake.get());
                if(sensorAlgaeIntake.get()) {
                    stateCurrent = State.HAVE_ALGAE;
                }
                break;
            case State.HAVE_ALGAE:
                spd = 0;
                if(IO.OperatorXbox.isRightBumpPressed()) {
                    countLoop = 0;
                    stateCurrent = State.DEPOSIT_ALGAE;
                }
                break;
            case State.DEPOSIT_ALGAE:
                spd = -.5;
                // Testing different values - 25 is 0.5 seconds (at 50 Hz)
                if(countLoop > 25) {
                    spd = 0;
                    stateCurrent = State.OFF;
                }
                break;       
            default:
                break;
        }

        SmartDashboard.putNumber("Algae Intake Spd", spd);
        algaeIntake.set(spd);
    }    

}
