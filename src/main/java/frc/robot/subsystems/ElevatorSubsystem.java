package frc.robot.subsystems;

import com.revrobotics.spark.*;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.RelativeEncoder;

import frc.robot.RobotMap.ElevatorConstants.CANID;
import frc.robot.RobotMap.ElevatorConstants.State;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.IO;
import frc.robot.IO.OperatorHID;
import frc.robot.IO.OperatorXbox;

public class ElevatorSubsystem {
/** Class Variables */
    private SparkMax elevatorLeft;
    private RelativeEncoder encoderLeft;
    private SparkMax elevatorRight;
    private RelativeEncoder encoderRight;
    private SparkMaxConfig config_;

    private int stateCurrent;    // Elevator Current State
    private int countLoop; 
    private double inputSpd;
    private double spd;
    private double posLeft;
    private double posRight;
    private double posTarget;

    // Elevator Left Limit Switch
    private static DigitalInput limitElevatorLeft = new DigitalInput(3);

   
    public void init() {
        /** Elevator Motors */
        config_ = new SparkMaxConfig();
        config_.idleMode(SparkMaxConfig.IdleMode.kBrake);
        elevatorLeft = new SparkMax(CANID.kCANElevatorLeft, SparkLowLevel.MotorType.kBrushless);
        encoderLeft = elevatorLeft.getEncoder();
        //elevatorLeft.restoreFactoryDefaults();   
        elevatorLeft.clearFaults();
        elevatorLeft.configure(config_, SparkBase.ResetMode.kResetSafeParameters, SparkBase.PersistMode.kPersistParameters);

        stateCurrent = State.UNKNOWN;    // Elevator Current State
        countLoop = 0; 
        posTarget = 0;
        spd = 0;
        elevatorLeft.set(spd);
        stateCurrent = State.STOP;
    }

    public void setTargetPos(double goalPos) {
        double encPos = encoderLeft.getPosition();
        double dist = 0;
        double moveSpd = 1;
        posTarget = goalPos;
        SmartDashboard.putNumber("Targ Pos", posTarget);
        dist = encPos - goalPos;
        if(Math.abs(dist) <5) {
            moveSpd = .25;
        }
        if(Math.abs(dist) <2) {
            moveSpd = .1;
        }
        if(Math.abs(dist) <1) {
            moveSpd = .05;
        }
        if (encPos < goalPos) {
            spd = moveSpd;
            stateCurrent = State.GOING_UP;
            SmartDashboard.putString("Targ Dir", "Up");
        }
        if (encPos > goalPos) {
            spd = -moveSpd;
            stateCurrent = State.GOING_DOWN;
            SmartDashboard.putString("Targ Dir", "Down");
        }
        if (encPos == goalPos || Math.abs(dist) < .2) {
            stateCurrent = State.AT_TARGET;
            SmartDashboard.putString("Targ Dir", "At");
        }
    }

    public void execute() {
        inputSpd = -IO.OperatorXbox.getRightY();
        if (Math.abs(inputSpd)<.05) {inputSpd = 0.0;}
        SmartDashboard.putNumber("Y Axis", inputSpd);
        posLeft = encoderLeft.getPosition();
        SmartDashboard.putNumber("Encoder", posLeft);
        SmartDashboard.putNumber("Targ Pos", posTarget);

        SmartDashboard.putNumber("El State", stateCurrent);
        SmartDashboard.putBoolean("El Lim", !limitElevatorLeft.get());
        if(inputSpd != 0) {
            stateCurrent = State.MANUAL;
        }
        if(!limitElevatorLeft.get()) {
            encoderLeft.setPosition(0);
        }
        if(IO.OperatorXbox.isAPressed()) {
            setTargetPos(50);
            SmartDashboard.putBoolean("A Button", true);
        } 
        else {
            SmartDashboard.putBoolean("A Button", false);
        }
        if (IO.OperatorHID.getButton(11)) {
            setTargetPos(10);
        }
        
        countLoop++;
        switch (stateCurrent) {
            case State.UNKNOWN:
                spd = 0;
                stateCurrent = State.STOP;
            case State.STOP:
                spd = 0;
                if (!limitElevatorLeft.get()) {  // limit switch
                    encoderLeft.setPosition(0);
                    stateCurrent = State.HOME;
                }
                break;
            case State.HOME:
                encoderLeft.setPosition(0);
                spd = 0;
                break;
            case State.GOING_DOWN:
                if (posLeft == posTarget) {
                    stateCurrent = State.AT_TARGET;
                    break;
                }
                if (spd == 0 || posLeft <= 0) {
                    stateCurrent = State.STOP;
                }
                setTargetPos(posTarget);
                break;
            case State.GOING_UP:
                if (posLeft == posTarget) {
                    stateCurrent = State.AT_TARGET;
                    break;
                }
                if (spd == 0 || posLeft >= 100) {
                    stateCurrent = State.STOP;
                }
                setTargetPos(posTarget);
                break;
            case State.AT_TARGET:
                spd = 0;
                // add checks against target position?
                break;
            case State.MANUAL:
                spd = inputSpd;
                if (spd == 0) {
                    stateCurrent = State.STOP;
                }
                /*
                if (spd < 0 && posLeft <= 0){
                    spd = 0;
                    stateCurrent = State.STOP;
                }
                if (spd > 0 && posLeft >= 100) {
                    spd = 0;
                    stateCurrent = State.STOP;
                }
                */
                break;
            default:
                break;                
        }
        SmartDashboard.putNumber("spd", spd);
        elevatorLeft.set(spd);
    }    
}
