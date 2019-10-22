package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

@Autonomous(name="AutonomieFTC", group="FTC")
//@Disabled
public class AutonomieFTC extends LinearOpMode {

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
//
//        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        if(opModeIsActive()) {
            encoderDrive(DRIVE_SPEED, 20.0, 20.0, 5.0);

            sleep(5000);

            telemetry.addData("Gata 20", "cm");
            telemetry.update();

            strafeDrive(DRIVE_SPEED, 'r', 10.0, 5.0);


            sleep(1000);

            telemetry.addData("Path", "Complete");
            telemetry.update();
        }
    }

    void strafeDrive(double speed, char direction, double distance, double timeoutS)
    {
        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int targetDistace;

        targetDistace = (int)(distance * COUNTS_PER_CM);

        robot.frontLeft.setTargetPosition(targetDistace);
        robot.frontRight.setTargetPosition(targetDistace);
        robot.backLeft.setTargetPosition(targetDistace);
        robot.backRight.setTargetPosition(targetDistace);

        robot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        runtime.reset();

        if(direction == 'L' || direction == 'l') {
            robot.frontLeft.setPower(speed);
            robot.frontRight.setPower(speed);
            robot.backLeft.setPower(-speed);
            robot.backRight.setPower(-speed);
        }
        else if(direction == 'R' || direction == 'r')
        {
            robot.frontLeft.setPower(-speed);
            robot.frontRight.setPower(-speed);
            robot.backLeft.setPower(speed);
            robot.backRight.setPower(speed);
        }

        while((robot.frontLeft.isBusy() && robot.frontRight.isBusy() && robot.backLeft.isBusy() && robot.backRight.isBusy()) && (runtime.seconds() < timeoutS))
        {

        }

        robot.frontLeft.setPower(0);
        robot.frontRight.setPower(0);
        robot.backLeft.setPower(0);
        robot.backRight.setPower(0);


    }


    void encoderDrive(double speed, double leftCM, double rightCM, double timeoutS)
    {
        /** Functia pe care o folosim pentru miscarea robotului */



        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int targetLeft;
        int targetRight;

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

        }

        robot.frontLeft.setPower(0);
        robot.frontRight.setPower(0);
        robot.backLeft.setPower(0);
        robot.backRight.setPower(0);

    }

}
