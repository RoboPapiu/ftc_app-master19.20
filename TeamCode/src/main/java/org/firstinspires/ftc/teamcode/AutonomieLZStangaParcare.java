package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="AutonomieLZStangaParcareFTC", group="FTC")
//@Disabled
public class AutonomieLZStangaParcare extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareMapFTC         robot   = new HardwareMapFTC();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 383.6;
    static final double     DRIVE_GEAR_REDUCTION    = 2;
    static final double     WHEEL_DIAMETER_CM   = 10.0 ;
    static final double     COUNTS_PER_CM         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_CM * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;
    static final double     stdTimeOut              = 5.0;
    int                     indexLine               = 0;
    double                  strafeLeft              = 140.0;
    int                     strafeIndex             = 0;

    static final double     COUNTS_PER_MOTOR_ARM    = 288.0;
    static final double     COUNTS_PER_LIFT_MOTOR   = 1120.0;
    static final double     LIFT_ARM_REDUCTION      = 45.0/125.0;
    static final int        SLEEPTIME               = 100;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
//        telemetry.addData("Status", "Resetting Encoders");    //
//        telemetry.update();

        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.armMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.armMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);



        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        if(opModeIsActive()) {

            robot.servoCub.setPosition(0.87);
            robot.servoFoundation0.setPosition(0.3);
            robot.servoFoundation1.setPosition(0.7);
            robot.servoAutonomous.setPosition(0.6);
            robot.servoAutonomousRight.setPosition(0.3);


            encoderDrive(DRIVE_SPEED, 63, 63, stdTimeOut);
            sleep(SLEEPTIME);

            strafeDrive(DRIVE_SPEED, 'r', 60, stdTimeOut);
            sleep(SLEEPTIME);

            telemetry.addData("Path", "Complete");
            telemetry.update();
        }
    }

    void strafeDrive(double speed, char direction, double distance, double timeoutS)
    {

        resetEncoder();

        int targetDistace;

        targetDistace = (int)(distance * COUNTS_PER_CM);


        if(direction == 'L' || direction == 'l') {
            robot.frontLeft.setTargetPosition(-targetDistace);
            robot.frontRight.setTargetPosition(-targetDistace);
            robot.backLeft.setTargetPosition(targetDistace);
            robot.backRight.setTargetPosition(targetDistace);
        }
        else  if(direction == 'R' || direction == 'r')
        {
            robot.frontLeft.setTargetPosition(targetDistace);
            robot.frontRight.setTargetPosition(targetDistace);
            robot.backLeft.setTargetPosition(-targetDistace);
            robot.backRight.setTargetPosition(-targetDistace);
        }

        robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        runtime.reset();

        robot.frontLeft.setPower(speed);
        robot.frontRight.setPower(speed);
        robot.backLeft.setPower(speed);
        robot.backRight.setPower(speed);


        while((robot.frontLeft.isBusy() && robot.frontRight.isBusy() && robot.backLeft.isBusy() && robot.backRight.isBusy()) && (runtime.seconds() < timeoutS))
        {
            telemetry.addData("encoder fl: " , robot.frontLeft.getCurrentPosition());
            telemetry.addData("encoder fr: " , robot.frontRight.getCurrentPosition());
            telemetry.addData("encoder bl: " , robot.backLeft.getCurrentPosition());
            telemetry.addData("encoder br: " , robot.backRight.getCurrentPosition());
            telemetry.update();
            idle();
        }

        robot.frontLeft.setPower(0);
        robot.frontRight.setPower(0);
        robot.backLeft.setPower(0);
        robot.backRight.setPower(0);

        robot.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        indexLine++;

    }


    void encoderDrive(double speed, double leftCM, double rightCM, double timeoutS)
    {
        /** Functia pe care o folosim pentru miscarea robotului */

        resetEncoder();

        int targetLeft = 0;
        int targetRight = 0;

        targetLeft = (int)(leftCM * COUNTS_PER_CM);
        targetRight = (int)(rightCM * COUNTS_PER_CM);


        robot.frontLeft.setTargetPosition(targetLeft);
        robot.frontRight.setTargetPosition(-targetRight);
        robot.backLeft.setTargetPosition(targetLeft);
        robot.backRight.setTargetPosition(-targetRight);

        robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.frontLeft.setPower(speed);
        robot.frontRight.setPower(speed);
        robot.backLeft.setPower(speed);
        robot.backRight.setPower(speed);

        runtime.reset();


        while((robot.frontLeft.isBusy() && robot.frontRight.isBusy() && robot.backLeft.isBusy() && robot.backRight.isBusy()) && (runtime.seconds() < timeoutS))
        {
            telemetry.addData("encoder fl: " , robot.frontLeft.getCurrentPosition());
            telemetry.addData("encoder fr: " , robot.frontRight.getCurrentPosition());
            telemetry.addData("encoder bl: " , robot.backLeft.getCurrentPosition());
            telemetry.addData("encoder br: " , robot.backRight.getCurrentPosition());
            telemetry.update();
            idle();
        }

        robot.frontLeft.setPower(0);
        robot.frontRight.setPower(0);
        robot.backLeft.setPower(0);
        robot.backRight.setPower(0);

        robot.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        indexLine++;
    }

    void resetEncoder()
    {
        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }



}
