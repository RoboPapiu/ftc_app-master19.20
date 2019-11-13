package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class HardwareMapFTC
{
    /* Public OpMode members. */
    public DcMotor frontLeft = null;
    public DcMotor frontRight = null;
    public DcMotor backLeft = null;
    public DcMotor backRight = null;
    public DcMotor armMotor = null; //Hex HD,  pt verticala (axa X)
    public DcMotor armMotorRight = null; //Hex de 40, pt uracare pe axa Y
    public DcMotor armMotorLeft = null; // Hex de 40,  pt uracare  pe axa Y

    public CRServo xServo = null;
    public Servo servoFoundation0 = null;
    public Servo servoFoundation1 = null;
    public Servo servoCub = null;
    public Servo servoAutonomous = null;
    public Servo servoAutonomousRight = null;

    public ColorSensor colorSensor = null;
    public ColorSensor colorSensorLeft = null;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareMapFTC(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        //Motoare AICI
        frontLeft  = hwMap.get(DcMotor.class, "frontLeft");
        frontRight = hwMap.get(DcMotor.class, "frontRight");
        backLeft = hwMap.get(DcMotor.class, "backLeft");
        backRight = hwMap.get(DcMotor.class, "backRight");

        armMotor = hwMap.get(DcMotor.class, "armMotor");
        armMotorRight = hwMap.get(DcMotor.class, "armMotorRight");
        armMotorLeft = hwMap.get(DcMotor.class, "armMotorLeft");


        //Motoare miscare
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Motoare brat
        armMotor.setPower(0);
        armMotorRight.setPower(0);
        armMotorLeft.setPower(0);

        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armMotorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armMotorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

       // armMotorRight.setMode(DcMotor.Direction.REVERSE);

        //Servo AICI

        servoCub = hwMap.get(Servo.class, "servoCub");
       // servoCub.setPosition(0.1);

        xServo = hwMap.get(CRServo.class, "xServo");
       // xServo.setPower(0);

        servoAutonomous = hwMap.get(Servo.class, "servoAutonomous");
//        servoAutonomous

        servoAutonomousRight = hwMap.get(Servo.class, "servoAutonomousRight");
        //servoAutonomousRight

        servoFoundation0 = hwMap.get(Servo.class, "servoFoundation0");
      //  servoFoundation0.setPosition(1);
        servoFoundation1 = hwMap.get(Servo.class, "servoFoundation1");
       // servoFoundation1.setPosition(0);

        //Senzor de culoare AICI

        colorSensor = hwMap.get(ColorSensor.class, "colorSensor");
        colorSensorLeft = hwMap.get(ColorSensor.class, "colorSensorLeft");

    }
 }

