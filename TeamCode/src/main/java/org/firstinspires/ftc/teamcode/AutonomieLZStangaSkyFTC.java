package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="AutonomieLZStangaSkyFTC", group="FTC")
//@Disabled
public class AutonomieLZStangaSkyFTC extends LinearOpMode {

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

            telemetry.addData("Distance: ", robot.distanceSensorBack.getDistance(DistanceUnit.CM));
            telemetry.update();

            robot.servoCub.setPosition(0.87);
            robot.servoFoundation0.setPosition(0.3);
            robot.servoFoundation1.setPosition(0.7);
            robot.servoAutonomous.setPosition(0.6);
            robot.servoAutonomousRight.setPosition(0.3);

            //72 cm fata, index 1 (stiu marc ca trebuie 0, dar cacatul asta de cod il face sa fie 1, incrementeaza indexLine in encoderDrive)
            encoderDrive(DRIVE_SPEED, 64, 64, stdTimeOut);
            printLineDone(indexLine); //functie scisa de mine (aka Dragos) uitate ce face, e super easy, dar ajuta destul de mult, e jos jos
            sleep(SLEEPTIME);

            while (opModeIsActive() && strafeIndex<4) {

                sleep(SLEEPTIME);
                double alphared = (double)(robot.colorSensorLeft.alpha())/robot.colorSensorLeft.red();


                sleep(SLEEPTIME);

                if (isSkystone())
                {
                    break;
                }
                else
                    {
                    strafeDrive(DRIVE_SPEED, 'l', 23, stdTimeOut);
                    printLineDone(indexLine);

                    sleep(SLEEPTIME);
                    strafeLeft += 23.0;
                    strafeIndex++;
                    }

            }

            telemetry.addData("al catele cub:", strafeIndex);
            telemetry.update();

            //se pozitioneaza pt a putea agata - se alinieaza cu bratul de agatat
            strafeDrive(DRIVE_SPEED, 'l', 28.5, stdTimeOut);
            sleep(SLEEPTIME + 400);

//            merge putin in fata pt a putea agata cu bratul
           // encoderDrive(DRIVE_SPEED, 2, 2, stdTimeOut);
            //sleep(SLEEPTIME + 200);

            //prinde cubul - il agata
            robot.servoAutonomousRight.setPosition(1);
            sleep(SLEEPTIME + 400);

//            sleep(1000000000);

            //merge in spate (l-am pus jos)
            encoderDrive(DRIVE_SPEED / 4, -30, -30, stdTimeOut);
            sleep(SLEEPTIME);

            //face strafe exact cat a mers in spre
            strafeDrive(DRIVE_SPEED, 'r', strafeLeft, stdTimeOut);
            printLineDone(indexLine);
            sleep(SLEEPTIME);

            //merge in fata
            encoderDrive(DRIVE_SPEED, 11, 10, stdTimeOut);
            sleep(SLEEPTIME);

            //lasa cubul
            robot.servoAutonomousRight.setPosition(0.3);
            sleep(SLEEPTIME);


            //se intoarce la randul de cuburi pt a gasi al doilea skystone
            //pt parcare distance = 60;
            //pt continuare = 119
            strafeDrive(DRIVE_SPEED, 'l', 120.0, stdTimeOut);
            sleep(SLEEPTIME);

            //se indreapta tata
            encoderDrive(DRIVE_SPEED, 4, 3, stdTimeOut);
            sleep(SLEEPTIME);

            //strafe pana in fata la al 2 lea skystone
            strafeDrive(DRIVE_SPEED, 'l', strafeIndex * 23.0 + 56, stdTimeOut);
            sleep(SLEEPTIME);

            //se apropi de skystone
            encoderDrive(DRIVE_SPEED, 31, 30, stdTimeOut);
            sleep(SLEEPTIME);

            //prinde skystone-ul
            robot.servoAutonomous.setPosition(0);
            sleep(SLEEPTIME + 400);

            //spate
            encoderDrive(DRIVE_SPEED / 3, -35, -35, stdTimeOut);
            sleep(SLEEPTIME);

            //strafe la stanga
            strafeDrive(DRIVE_SPEED, 'r', 190 + strafeIndex * 23.0, stdTimeOut);
            sleep(SLEEPTIME);

            //lasa skystone-ul
            robot.servoAutonomous.setPosition(0.7);
            sleep(SLEEPTIME +  400);

            //strafe dreapta parcare, probabil mai trebuie si mers putin in fata
  //          strafeDrive(DRIVE_SPEED, 'l', 58, stdTimeOut);
//            sleep(SLEEPTIME);

            //mergem un pic putinel an fatzuka
            encoderDrive(DRIVE_SPEED, 18, 18, stdTimeOut);
            sleep(SLEEPTIME);

            strafeDrive(DRIVE_SPEED, 'l', 58, stdTimeOut);
            sleep(SLEEPTIME);

            encoderDrive(DRIVE_SPEED, 3, 3 , stdTimeOut);
            sleep(SLEEPTIME);


            telemetry.addData("Ma muie!!! ", "AM TERMINAT!!!");
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
        double alphared = (double)(robot.colorSensorLeft.alpha())/robot.colorSensorLeft.red();
        if (alphared >= 3.0) {
            ok = true;
        }
        else {
            ok = false;
        }
        return ok;
    }


}


           /*     telemetry.addData("Distance: ", robot.distanceSensorBack.getDistance(DistanceUnit.CM));
                telemetry.update();
                while((robot.distanceSensorBack.getDistance(DistanceUnit.CM) > 45.0) && opModeIsActive())
                {
                    telemetry.addData("sunt pe bines ", "ceaw marc");
                    telemetry.update();
                    robot.frontRight.setPower(-1);
                    robot.backRight.setPower(-1);
                    robot.frontLeft.setPower(1);
                    robot.backLeft.setPower(1);
                }
                    telemetry.addData("sunt pe else ", "(ciclu/stop)");
                    telemetry.update();
                    robot.frontRight.setPower(0);
                    robot.backRight.setPower(0);
                    robot.frontLeft.setPower(0);
                    robot.backLeft.setPower(0);
            */