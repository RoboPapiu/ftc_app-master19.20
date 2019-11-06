package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
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
    public DcMotor armMotor = null;

    public CRServo xServo = null;
    public Servo servoFoundation0 = null;
    public Servo servoFoundation1 = null;
    public Servo servoCub = null;

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



        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        armMotor.setPower(0);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Servo AICI

        servoCub = hwMap.get(Servo.class, "servoCub");
        servoCub.setPosition(0.1);

        xServo = hwMap.get(CRServo.class, "xServo");
        xServo.setPower(0);

        servoFoundation0 = hwMap.get(Servo.class, "servoFoundation0");
        servoFoundation0.setPosition(1);
        servoFoundation1 = hwMap.get(Servo.class, "servoFoundation1");
        servoFoundation1.setPosition(0);

        //Senzor de culoare AICI

        colorSensor = hwMap.get(ColorSensor.class, "colorSensor");
        colorSensorLeft = hwMap.get(ColorSensor.class, "colorSensorLeft");

    }
 }

