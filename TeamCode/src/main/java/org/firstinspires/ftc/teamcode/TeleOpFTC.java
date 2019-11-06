/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;



@TeleOp(name="TeleOpFTC", group="FTC")
//@Disabled
public class TeleOpFTC extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    HardwareMapFTC robot = new HardwareMapFTC();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        // Tell the driver that initialization is complete.
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

  /*  void stopMotion()
    {
        runtime.reset();
        while (runtime.seconds() <= 0.1)
        {
            robot.frontLeft.setPower(-1);
            robot.frontRight.setPower(1);
            robot.backLeft.setPower(-1);
            robot.backRight.setPower(1);
        }
        robot.frontLeft.setPower(0);
        robot.frontRight.setPower(0);
        robot.backLeft.setPower(0);
        robot.backRight.setPower(0);
    } */


    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        double speedLeft = -gamepad1.left_stick_y + gamepad1.left_stick_x;
        double speedRight = -gamepad1.left_stick_y - gamepad1.left_stick_x;
        double strafePower = 0.25;

        double armPower = gamepad1.right_stick_y;

        telemetry.addData("armPower:", armPower);
        telemetry.update();

        speedLeft /= 2;
        speedRight /= 2;
        armPower /= 2;

        if(gamepad1.b)
        {

            strafePower /= 2;
            speedLeft /= 2;
            speedRight /= 2;
            armPower /= 2;

        }


        if (gamepad1.left_bumper) {
            robot.frontLeft.setPower(-strafePower);
            robot.frontRight.setPower(-strafePower);
            robot.backLeft.setPower(strafePower);
            robot.backRight.setPower(strafePower);
        }
        else if (gamepad1.right_bumper) {
            robot.frontLeft.setPower(strafePower);
            robot.frontRight.setPower(strafePower);
            robot.backLeft.setPower(-strafePower);
            robot.backRight.setPower(-strafePower);
        }
        else if (speedLeft != 0 || speedRight != 0) {
            robot.frontLeft.setPower(speedLeft);
            robot.frontRight.setPower(-speedRight);
            robot.backLeft.setPower(speedLeft);
            robot.backRight.setPower(-speedRight);

            telemetry.addData("motoare gay: ", "%f | %f", speedLeft, speedRight);
            telemetry.update();
        }
        else {
            robot.frontLeft.setPower(0);
            robot.frontRight.setPower(0);
            robot.backLeft.setPower(0);
            robot.backRight.setPower(0);
        }

        if (gamepad1.dpad_right) {
            robot.xServo.setPower(1);
        }
        else if (gamepad1.dpad_left) {
            robot.xServo.setPower(-1);
        }
        else {
            robot.xServo.setPower(0);
        }

        if(armPower!=0) {
            robot.armMotor.setPower(armPower);
        }
        else {
            //robot.armMotor.setPower(-0.01);
            robot.armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        if (gamepad1.dpad_down) {
            robot.servoCub.setPosition(0.8);
        }
        else if (gamepad1.dpad_up) {
            robot.servoCub.setPosition(0.4);
        }

        if (gamepad1.x) {
            robot.servoFoundation1.setPosition(0.7);
            robot.servoFoundation0.setPosition(0.3);
        }
        else if (gamepad1.y) {
            robot.servoFoundation1.setPosition(0);
            robot.servoFoundation0.setPosition(1);
        }





        double alphared = (double)(robot.colorSensor.alpha())/robot.colorSensor.red();
        double alphagreen = robot.colorSensor.alpha()/robot.colorSensor.green();
        double alphablue = robot.colorSensor.alpha()/robot.colorSensor.blue();

        telemetry.addData("red: ",  robot.colorSensor.red());
        telemetry.addData("green: ",  robot.colorSensor.green());
        telemetry.addData("blue: ",  robot.colorSensor.blue());
        telemetry.addData("alpha: ",  robot.colorSensor.alpha());
        telemetry.addData("alphared: ", alphared);
        telemetry.addData("alphagreen: ", alphagreen);
        telemetry.addData("alphablue: ", alphablue);
        telemetry.addData("rightspeed: ", speedRight);
        telemetry.addData("leftspeed: ", speedLeft);
        telemetry.update();

    }



    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

    void resetEncoder()
    {
        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    void armLiftDegrees(int degrees)
    {
        degrees *= 3;
        final double     COUNTS_PER_MOTOR_ARM    = 288.0;
        double speed = 0.3;
        int targetPosition = (int)(degrees * COUNTS_PER_MOTOR_ARM);

        resetEncoder();

        robot.armMotor.setTargetPosition(targetPosition);

        robot.armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.armMotor.setPower(speed);

        runtime.reset();

        while(robot.armMotor.isBusy() && (runtime.seconds() < 5.0))
        {
            Thread.yield();
        }

        robot.armMotor.setPower(0);

        robot.armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

}
