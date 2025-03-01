package frc.robot;

public interface RobotMap {
    public static final class DevMode {
        public static final boolean isTelemetryEnabled = true;
    }

    /** Configuration constants for Limelight */
    public static final class LimelightConstants {
        public static final int kPipelineLedSettings = 0;
        public static final int kLedOff = 1;
        public static final int kLedBlink = 2;
        public static final int kLedOn = 3;
    }

     /** Configuration constants for Operator Input */
    public static final class OIConstants {
        public static final int DRIVE_PORT = 0; //USB IO Port  -- joystick init in robotcontainer, too.  Switch if needed
        public static final int OPERATOR_PORT = 1;
        public static final int BOX_PORT_1 = 2;
        public static final int BOX_PORT_2 = 3;

        public static final boolean SEPARATE_CONTROLS = true;

        public static final double TURN_SPEED_MULT = 1;
        public static final double DRIVE_SPEED_MULT = 1;

        public static final double kDriverDeadband = 0.2;
    }

   /** Constants for the Coral Subsystems */
   public static final class CoralConstants {
        public static final class CANID {
            public static final int kCANCoralIntake = 31;
            public static final int kCANCoralMover = 32;    
        }
    
        /** States for Coral Handler */
        public static final class State {
            public static final int UNKNOWN = 0;            // This is the unkown state for the Coral Handler
            public static final int OFF = 1;
            public static final int CORAL_INTAKE = 2;
            public static final int HAVE_CORAL = 3;
            public static final int CORAL_READY = 4;
            public static final int DEPOSIT_CORAL = 5; 
            public static final int MANUAL = 99;
        }
    }

  /** Constants for the Algae Subsystems */
  public static final class AlgaeConstants {
    public static final class CANID {
        public static final int kCANAlgaeIntake = 41; 
        public static final int kCANAlgaeRotate = 42;
    }

    /** States for Algae Handler */
    public static final class State {
        public static final int UNKNOWN = 0;            // This is the unkown state for the Algae Handler
        public static final int OFF = 1;
        public static final int ALGAE_INTAKE = 2;
        public static final int HAVE_ALGAE = 3;
        public static final int DEPOSIT_ALGAE = 5; 
        public static final int MANUAL = 99;
    }
}

   /** Constants for the Cage Hang Subsystem */
   public static final class CageConstants {
    public static final class CANID {
        public static final int kCANCageHang = 21; 
    }
    
    /** States for Cage Hang */
    public static final class State_CH {
        public static final int UNKNOWN = 0;            // This is the unkown state for the Coral Handler
        public static final int OFF = 1;
        public static final int CORAL_INTAKE = 2;
        public static final int HAVE_CORAL = 3;
        public static final int CORAL_READY = 4;
        public static final int DEPOSIT_CORAL = 5; 
        public static final int MANUAL = 99;
    }
}

/** Constants for the Elevator Subsystems */
public static final class ElevatorConstants {
    public static final class CANID {
        public static final int kCANElevatorLeft = 51; 
        public static final int kCANElevatorRight = 52;
    }

    /** States for Elevator */
    public static final class State {
        public static final int UNKNOWN = 0;            // This is the unkown state for the Algae Handler
        public static final int STOP = 1;
        public static final int HOME = 2;
        public static final int GOING_UP = 3;
        public static final int GOING_DOWN = 4;
        public static final int AT_TARGET = 5;
        public static final int MANUAL = 99;
    }
}

    /** Configuration constants for LEDs */
    public static final class LEDConstants {
        public static final int PWM_LEDS = 0;
        public static final int STATE_LEDS_OFF = 0;
        public static final int STATE_LEDS_INIT = 1;
        public static final int STATE_LEDS_STATUS = 2;
        public static final int STATE_LEDS_COLOR = 3;
        public static final int STATE_LEDS_PIECE = 4;
        public static final int STATE_LEDS_AUTO = 5;
        public static final int STATE_LEDS_COUNTDOWN = 6;
        public static final int STATE_LEDS_COLLECT_SHOOT = 7;
        public static final int STATE_LEDS_ALIGN = 8;

        public static final int PIECE_TYPE_UNKNOWN = 0;
        public static final int PIECE_TYPE_CUBE = 1;
        public static final int PIECE_TYPE_CONE = 2;
    }

    //** Button Box Buttons */
    public static final class ButtonBOX {
        public static final int SHOOT_MAX = 1;
        public static final int SHOOT_SPEAKER = 2;
        public static final int SHOOT_AMP = 3;
        public static final int INTAKE_SHOOTER = 4;
        public static final int INTAKE_COLLECTOR = 5;
        public static final int INTAKE_OFF = 6;
        public static final int INTAKE_EJECT = 7;
        public static final int LIFTER_UP = 8;
        public static final int LIFTER_STOP = 9;
        public static final int LIFTER_DOWN = 10;
    }

    public static final class RobotButtons {
        public static final int kWhite = 0;
        public static final int kYellow = 1;
        public static final int kRed = 2;
        public static final int kGreen = 3;
        public static final int kBlue = 4;
    }
}
