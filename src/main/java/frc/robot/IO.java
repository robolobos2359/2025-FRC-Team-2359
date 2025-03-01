package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap.DevMode;
import frc.robot.RobotMap.OIConstants;
import frc.robot.RobotMap.RobotButtons;

public class IO {
    // Joystick (4axis tilt) at port DRIVE_PORT (def @ RobotMap)
    //private static Joystick driver = new Joystick(OIConstants.DRIVE_PORT);
    // Xbox Controller at port LIFT_PORT (def @ RobotMap)
    private static XboxController operator = new XboxController(OIConstants.OPERATOR_PORT);
    // Generic HID (collections of buttons at port BOX_PORT_x
    private static GenericHID buttonBox1 = new GenericHID(OIConstants.BOX_PORT_1);
    private static GenericHID buttonBox2 = new GenericHID(OIConstants.BOX_PORT_2);
    // power dist hub on robot
    private static PowerDistribution pdh = new PowerDistribution();
    // sets up network access to access limelight
    private static final NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

    //private static DigitalInput white = new DigitalInput(RobotButtons.kWhite);
    //private static DigitalInput yellow = new DigitalInput(RobotButtons.kYellow);
    //private static DigitalInput red = new DigitalInput(RobotButtons.kRed);
    //private static DigitalInput green = new DigitalInput(RobotButtons.kGreen);
    //private static DigitalInput blue = new DigitalInput(RobotButtons.kBlue);  
    
    /** The Operator HID - currently configured for a button box that is used by an operator */
    public static class OperatorHID {
        /**
         * Checks Button <b>FOR THE BUTTON BOX</b>
         * 
         * @param btn is the targeted button
         * 
         * with Twin HID controllers, first 10 buttons are on HID controller 1 and next 10 buttons are on HID controller 2
         */
        public static boolean getButton(int btn) {
            if (btn>10){
                return (buttonBox2 != null ? buttonBox2.getRawButtonPressed(btn-10) : false);
            }
            else {
                return (buttonBox1 != null ? buttonBox1.getRawButtonPressed(btn) : false);
            }
        }

        public static int getJoystickPos(Integer box) {
            int x;
            int y;
            if (box == 2) {
                x = (int)buttonBox1.getRawAxis(0);
            } else {
                x = (int)buttonBox2.getRawAxis(0);
            }
            if (x > -1 && x < 1) {
                x = 0;
            }
            if (box == 2) {
                y = (int)buttonBox1.getRawAxis(1);
            } else {
                y = (int)buttonBox2.getRawAxis(1);
            }
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

        /** Checks Right Y Axis */
        public static double getRightY() {
            return operator.getRightY();
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
