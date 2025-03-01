package frc.robot.subsystems;

import com.revrobotics.spark.*;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import frc.robot.RobotMap.CageConstants.CANID;
import frc.robot.RobotMap.CageConstants.State_CH;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.IO;
import frc.robot.IO.OperatorHID;
import frc.robot.IO.OperatorXbox;

public class CageHangSubsystem {
 /** Class Variables */
    //private int stateCurrent = State_CH.UNKNOWN;    // Cage Hang/Shooter Current State
    private int countLoop = 0; 
    private int stateCurrent = State_CH.UNKNOWN;

    /** Cage Hange CAN Spark Flex Motor */
    private SparkFlex cageHanger = new SparkFlex(CANID.kCANCageHang, SparkLowLevel.MotorType.kBrushless);
    private SparkMaxConfig config_ = new SparkMaxConfig();
    private double spd = 0;
   
    public void init() {
        stateCurrent = State_CH.OFF;

        //coralMover.restoreFactoryDefaults();   
        cageHanger.clearFaults();
        config_.idleMode(SparkMaxConfig.IdleMode.kBrake);
        cageHanger.configure(config_, SparkBase.ResetMode.kResetSafeParameters, SparkBase.PersistMode.kPersistParameters);
    }

    public void execute() {
        spd = IO.OperatorXbox.getRightY();
        if (Math.abs(spd)<.05) {spd = 0.0;}
        SmartDashboard.putNumber("Y Axis", spd);
        cageHanger.set(spd);
    }    
}
