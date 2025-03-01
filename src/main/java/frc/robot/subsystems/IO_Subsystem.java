package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap.DevMode;
import frc.robot.RobotMap.OIConstants;
import frc.robot.RobotMap.RobotButtons;

public class IO_Subsystem extends SubsystemBase {
    // Joystick (4axis tilt) at port DRIVE_PORT (def @ RobotMap)
    private static Joystick driver = new Joystick(OIConstants.DRIVE_PORT);
    // Xbox Controller at port LIFT_PORT (def @ RobotMap)
    private static XboxController operator = new XboxController(OIConstants.OPERATOR_PORT);
    // Generic HID (collections of buttons at port BOX_PORT
    private static GenericHID buttonBox = new GenericHID(OIConstants.BOX_PORT_1);
    // power dist hub on robot
    private static PowerDistribution pdh = new PowerDistribution();
    // sets up network access to access limelight
    private static final NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

    private static DigitalInput white = new DigitalInput(RobotButtons.kWhite);
    private static DigitalInput yellow = new DigitalInput(RobotButtons.kYellow);
    private static DigitalInput red = new DigitalInput(RobotButtons.kRed);
    private static DigitalInput green = new DigitalInput(RobotButtons.kGreen);
    private static DigitalInput blue = new DigitalInput(RobotButtons.kBlue);
      
    public static class Driver {
        /**
         * Checks Button
         * 
         * @param btn is the targeted button
         */
        public boolean getButton(int btn) {
            return driver.getRawButtonPressed(btn);
        }

        /**
         * Get selected axis
         * 
         * @param ax is the axis you selected
         */
        public double getRawAxis(int ax) {
            return driver.getRawAxis(ax);
        }

        /** Get the lower dial, values from -1 to 1 */
        public static double getSpeedDial() {
            return driver.getRawAxis(3);
        }

        public static double convToSpeedMult() {
            double spdMultiplier = ((getSpeedDial() + 1) * 0.25) + 0.5;
            SmartDashboard.putNumber("Spd Drive Multipilier", spdMultiplier);
            return spdMultiplier;
        }

        /** Checks X Axis <b>FOR THE DRIVE CONTROLLER</b> */
        public double getDriveX() {
            return Math.abs(driver.getX()) > 0.1 ? driver.getX() : 0;
        }

        /** Checks Y Axis <b>FOR THE DRIVE CONTROLLER</b> */
        public double getDriveY() {
            return Math.abs(driver.getY()) > 0.1 ? driver.getY() : 0;
        }

        /** Checks stick angle <b>FOR THE DRIVE CONTROLLER</b> */
        public double getDriveDirection() {
            return driver.getDirectionRadians();
        }

        /** Checks stick magnitude <b>FOR THE DRIVE CONTROLLER</b> */
        public double getDriveMagnitude() {
            return driver.getMagnitude();
        }

        /** Checks stick twist <b>FOR THE DRIVE CONTROLLER</b> */
        public double getDriveTwist() {
            if(DevMode.isTelemetryEnabled) {
                SmartDashboard.putNumber("Twist", driver.getTwist());
            }
            return Math.abs(driver.getTwist()) > 0.5 ? driver.getTwist() * 0.5 : 0;
        }

        /** Checks Trigger <b>FOR THE DRIVE CONTROLLER</b> */
        public boolean getTrigger() {
            return driver.getTrigger();
        }

        /** Checks POV (little hat guy on top) <b>FOR THE DRIVE CONTROLLER</b> */
        public double getPOV() {
            // return liftCont.getLeftTriggerAxis() - liftCont.getRightTriggerAxis();
            return driver.getPOV();
        }

        /**
         * Checks if POV (little hat guy on top) is rotated to an angle <b>FOR THE DRIVE
         * CONTROLLER</b>
         * 
         * @param angle is the desired angle to check for
         */
        public boolean isPOVToAngle(double angle) {
            return driver.getPOV() == angle;
        }
    }

    public static class RobotControls {
        public static boolean getDIO(int port) {
            if (port == RobotButtons.kRed || port == RobotButtons.kWhite || port == RobotButtons.kYellow 
                    || port == RobotButtons.kGreen || port == RobotButtons.kBlue) {
                switch(port) {
                    case RobotButtons.kWhite:
                        return !white.get();
                    case RobotButtons.kRed:
                        return !red.get();
                    case RobotButtons.kYellow:
                        return !yellow.get();
                    case RobotButtons.kGreen:
                        return !green.get();
                    case RobotButtons.kBlue:
                        return !blue.get();
                }
            }
            return false;
        }
    }

    /** The Operator HID - currently configured for a button box that is used by an operator */
    public static class OperatorHID {
        /**
         * Checks Button <b>FOR THE BUTTON BOX</b>
         * 
         * @param btn is the targeted button
         */
        public static boolean getButton(int btn) {
            return (buttonBox != null ? buttonBox.getRawButtonPressed(btn) : false);
        }

        public static int getJoystickPos() {
            int x = (int)buttonBox.getRawAxis(0);
            if (x > -1 && x < 1) {
                x = 0;
            }
            int y = (int)buttonBox.getRawAxis(1);
            if (y > -1 && y < 1) {
                y = 0;
            }
            int pos = 0;
            switch (x) {
                case -1:
                    switch (y) {
                        case -1:
                            pos = 2;
                            break;
                        case 0:
                            pos = 1;
                            break;
                        case 1:
                            pos = 8;
                            break;
                    }
                    break;
                case 0:
                    switch (y) {
                        case -1:
                            pos = 3;
                            break;
                        case 0:
                            pos = 0;
                            break;
                        case 1:
                            pos = 7;
                            break;
                    }
                    break;
                case 1:
                    switch (y) {
                        case -1:
                            pos = 4;
                            break;
                        case 0:
                            pos = 5;
                            break;
                        case 1:
                            pos = 6;
                            break;
                    }
                    break;
            }
            //SmartDashboard.putNumber("bbX",x);
            //SmartDashboard.putNumber("bbY",y);
            SmartDashboard.putNumber("pos", pos);
            return pos;
        }
    }

    /** The Operator Xbox Controller - currently configured for an Xbox Controller */
    public static class OperatorXbox {
        /** Checks Left Y Axis */
        public static double getLeftY() {
            return operator.getLeftY();
        }

        /** Checks Left X Axis */
        public static double getLeftX() {
            return operator.getLeftX();
        }

        /** Checks Right X Axis */
        public static double getRightX() {
            return operator.getRightX();
        }

        /** Checks left joystick pressed */
        public static boolean isLeftAxisPressed() {
            return operator.getLeftStickButtonPressed();
        }

        /** Checks left joystick pressed */
        public static boolean isRightAxisPressed() {
            return operator.getRightStickButtonPressed();
        }

        /** Checks X */
        public static boolean isXPressed() {
            return operator.getXButtonPressed();
        }

        /** Checks Y */
        public static boolean isYPressed() {
            return operator.getYButtonPressed();
        }

        /** Checks POV */
        public static int getLiftPOV() {
            return operator.getPOV();
        }

        /** Checks A */
        public static boolean isAPressed() {
            return operator.getAButtonPressed();
        }

        /** Checks B */
        public static boolean isBPressed() {
            return operator.getBButtonPressed();
        }

        /** Checks Left Bumper */
        public static boolean isLeftBumpPressed() {
            return operator.getLeftBumperPressed();
        }

        /** Checks Right Bumper */
        public static boolean isRightBumpPressed() {
            return operator.getRightBumperPressed();
        }
    }

}
