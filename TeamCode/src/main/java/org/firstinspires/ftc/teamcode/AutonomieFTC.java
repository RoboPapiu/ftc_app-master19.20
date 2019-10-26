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
    static final double     stdTimeOut              = 5.0;
    int                     indexLine               = 0;

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


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        if(opModeIsActive()) {


            telemetry.addData("red: ",  robot.colorSensor.red());
            telemetry.addData("green: ",  robot.colorSensor.green());
            telemetry.addData("blue: ",  robot.colorSensor.blue());
            telemetry.addData("alpha: ",  robot.colorSensor.alpha());
            telemetry.update();

            //72 cm fata, index 1 (stiu marc ca trebuie 0, dar cacatul asta de cod il face sa fie 1, incrementeaza indexLine in encoderDrive)
            encoderDrive(DRIVE_SPEED, 71.5, 71.5, stdTimeOut);
            printLineDone(indexLine); //functie scisa de mine (aka Dragos) uitate ce face, e super easy, dar ajuta destul de mult, e jos jos
            sleep(1500);

            //72 cm strafe dreapta, index 2
            strafeDrive(DRIVE_SPEED, 'r', 72.0, stdTimeOut);
            printLineDone(indexLine);
            sleep(1000);

            //prind cubul cu servo
            robot.servoCub.setPosition(1);
            sleep(1000);

            //30 cm spate, index 3
            encoderDrive(DRIVE_SPEED, -50.0, -50.0, stdTimeOut);
            printLineDone(indexLine);
            sleep(1000);

            //210 cm strafe stange, index 4
            strafeDrive(DRIVE_SPEED, 'l', 230.0, stdTimeOut);
            printLineDone(indexLine);
            sleep(1000);

            //lasa cubul cu servo
            robot.servoCub.setPosition(0.3);
            sleep(1000);


            encoderDrive(DRIVE_SPEED, -5.0, -5.0, stdTimeOut);
            sleep(1000);

            strafeDrive(DRIVE_SPEED, 'r', 25.0, stdTimeOut);

            sleep(100000000); //e un sleep sa nu termine programul si sa vad inca pe  ecran tot ce vreu de la telemetry, sterge-l daca vrei

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

        indexLine++;
    }

    void resetEncoder()
    {
        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    void printLineDone(int value)
    {
        telemetry.addData("Line: ", value);
        telemetry.update();
    }

    boolean isSkystone () {
        boolean ok = false;
        if (robot.colorSensor.alpha()>100) {
            if (robot.colorSensor.red()>40 && robot.colorSensor.red()<140 &&
                robot.colorSensor.green()>45 && robot.colorSensor.green()<115 &&
                robot.colorSensor.blue()>40 && robot.colorSensor.blue()<55) {
                ok = true;
            }
            else {
                ok = false;
            }
        }
        return ok;
    }


}
