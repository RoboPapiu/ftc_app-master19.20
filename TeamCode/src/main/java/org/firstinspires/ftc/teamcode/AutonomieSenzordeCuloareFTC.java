package org.firstinspires.ftc.teamcode;

import android.provider.Telephony;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="AutonomieDreaptaFTC", group="FTC")
//@Disabled
public class AutonomieSenzordeCuloareFTC extends LinearOpMode {

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


            telemetry.addData("red: ",  robot.colorSensor.red());
            telemetry.addData("green: ",  robot.colorSensor.green());
            telemetry.addData("blue: ",  robot.colorSensor.blue());
            telemetry.addData("alpha: ",  robot.colorSensor.alpha());
            telemetry.update();

            robot.servoCub.setPosition(0.87);
            robot.servoFoundation0.setPosition(0.3);
            robot.servoFoundation1.setPosition(0.7);
            robot.servoAutonomous.setPosition(0.2);
            robot.servoAutonomousRight.setPosition(0.2);

            //72 cm fata, index 1 (stiu marc ca trebuie 0, dar cacatul asta de cod il face sa fie 1, incrementeaza indexLine in encoderDrive)
            encoderDrive(DRIVE_SPEED, 68, 68, stdTimeOut);
            printLineDone(indexLine); //functie scisa de mine (aka Dragos) uitate ce face, e super easy, dar ajuta destul de mult, e jos jos
            sleep(SLEEPTIME);

            while (opModeIsActive()) {

                sleep(SLEEPTIME);
                double alphared = (double)(robot.colorSensor.alpha())/robot.colorSensor.red();


                telemetry.addData("red: ",  robot.colorSensor.red());
                telemetry.addData("green: ",  robot.colorSensor.green());
                telemetry.addData("blue: ",  robot.colorSensor.blue());
                telemetry.addData("alpha: ",  robot.colorSensor.alpha());
                telemetry.addData("alphared: ", alphared);
                telemetry.update();

                sleep(SLEEPTIME);

                if (isSkystone())
                {
                    break;
                }
                else
                    {
                    strafeDrive(DRIVE_SPEED, 'r', 23, stdTimeOut);
                    printLineDone(indexLine);

                    sleep(SLEEPTIME);
                    strafeLeft += 23.0;
                    strafeIndex++;
                    }

            }

            //se pozitioneaza pt a putea agata - se alinieaza cu bratul de agatat
            strafeDrive(DRIVE_SPEED, 'r', 17, stdTimeOut);
            sleep(SLEEPTIME);

            //merge putin in fata pt a putea agata cu bratul
            encoderDrive(DRIVE_SPEED, 5, 5, stdTimeOut);
            sleep(SLEEPTIME);

            //prinde cubul - il agata
            robot.servoAutonomous.setPosition(0.8);
            sleep(SLEEPTIME);

            //spate
            encoderDrive(DRIVE_SPEED, -26.0, -26.0, stdTimeOut);
            printLineDone(indexLine);
            sleep(SLEEPTIME);


            //face strafe exact cat a mers in spre skystone
            strafeDrive(DRIVE_SPEED, 'l', strafeLeft, stdTimeOut);
            printLineDone(indexLine);
            sleep(SLEEPTIME);

            //merge in fata
            encoderDrive(DRIVE_SPEED, 10, 10, stdTimeOut);
            sleep(SLEEPTIME);

            //lasa cubul
            robot.servoAutonomous.setPosition(0.2);
            sleep(SLEEPTIME);


            //se intoarce la randul de cuburi pt a gasi al doilea skystone
            //pt parcare distance = 60;
            strafeDrive(DRIVE_SPEED, 'r', 119.0, stdTimeOut);
            sleep(SLEEPTIME);

            //merge putin in fata
            encoderDrive(DRIVE_SPEED, 18, 20, stdTimeOut);
            sleep(SLEEPTIME);

            strafeDrive(DRIVE_SPEED, 'r', strafeIndex * 23.0 + 23.0 * 3, stdTimeOut);
            sleep(SLEEPTIME);

//            robot.servoAutonomous.setPosition(0.8);
//            sleep(SLEEPTIME);

            telemetry.addData("Strafe Index", strafeIndex);
            telemetry.update();

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

    void printLineDone(int value)
    {
        telemetry.addData("Line: ", value);
        telemetry.update();
    }

    void armHorizontal(double time, int direction)
    {
        //1 pt fata, -1 pt spate, sorin e gay

        runtime.reset();
        while (runtime.seconds() < time)
            robot.armMotor.setPower(0.3 * direction);
        robot.armMotor.setPower(0);
    }

    void armLift(double liftDistance)
    {

        int targetLiftDistance = (int)(liftDistance * LIFT_ARM_REDUCTION * COUNTS_PER_LIFT_MOTOR);

        robot.armMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.armMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.armMotorLeft.setTargetPosition(targetLiftDistance);
        robot.armMotorRight.setTargetPosition(-targetLiftDistance);

        robot.armMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.armMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.armMotorLeft.setPower(0.5);
        robot.armMotorRight.setPower(0.5);

        while (robot.armMotorRight.isBusy() && robot.armMotorLeft.isBusy()) {
            idle();
        }

        robot.armMotorLeft.setPower(0);
        robot.armMotorRight.setPower(0);

    }

    boolean isSkystone ()
    {
        boolean ok = false;
        double alphared = (double)(robot.colorSensor.alpha())/robot.colorSensor.red();
        if (alphared>=2.5) {
            ok = true;
        }
        else {
            ok = false;
        }
        return ok;
    }


}
