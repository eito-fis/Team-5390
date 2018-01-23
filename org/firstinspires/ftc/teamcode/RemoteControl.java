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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import commands.Command;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Remote", group="Linear Opmode")
public class RemoteControl extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        DcMotor leftDrive;
        DcMotor rightDrive;
        DcMotor lift;
        DcMotor rotation;
        //DcMotor board;

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        lift = hardwareMap.get(DcMotor.class, "lift");
        rotation = hardwareMap.get(DcMotor.class, "rotation");

        //board = hardwareMap.get(DcMotor.class, "board");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        lift.setDirection(DcMotor.Direction.FORWARD);
        rotation.setDirection(DcMotor.Direction.FORWARD);

        Servo clawR;
        Servo clawL;
        Servo ballArm;

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        clawR = hardwareMap.get(Servo.class, "one");
        clawL = hardwareMap.get(Servo.class, "two");
        ballArm = hardwareMap.get(Servo.class, "bA");


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery

        //Color Sensor
        //Only for testing - not used in remote
        /*
        ColorSensor colorSensor;

        colorSensor = hardwareMap.get(ColorSensor.class, "cS");

        colorSensor.enableLed(true);
        */

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();


        //Initial claw positions
        double clawRP = 1;
        double clawLP = 0;

        //Set initial claw postions
        clawR.setPosition(clawRP);
        clawL.setPosition(clawLP);
        ballArm.setPosition(0);

        //Variable to keep track or direction of lift rotation
        int rot = 1;
        // run until the end of the match (driver presses STOP)

        rotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rotation.setPower(0.25 * rot);
        rotation.setTargetPosition((int)(Command.ENCODERTICKS / 8 * rot));

        while (opModeIsActive()) {

            //Motor powers
            double leftPower;
            double rightPower;
            double liftPower;
            double rotationPower;
            //double boardPower;


            //Claw controls
            //Each side of bumpers controls open/close lf claws
            if (gamepad1.right_trigger != 0)
                clawLP += 0.2;
            else if (gamepad1.right_bumper)
                clawLP -= 0.2;

            if (gamepad1.left_trigger != 0)
                clawRP -= 0.2;
            else if (gamepad1.left_bumper)
                clawRP += 0.2;

            //Clip claw postions to make sure they're within range
            clawRP = Range.clip(clawRP, 0.2, 1.0);
            clawLP = Range.clip(clawLP, 0, 0.8);

            /*
            if (gamepad1.y)
                boardPower = -0.7;
            else if (gamepad1.b)
                boardPower = 0.7;
            else
                boardPower = 0;
                */

            //Lift is controlled by right stick
            //Clip value to make sure they're within range
            liftPower = Range.clip(-gamepad1.right_stick_y, -1, 1);

            /*Old arcade style remote - ignore
            if (gamepad1.left_stick_y > 0 || (gamepad1.left_stick_x > 0 && gamepad1.left_stick_y == 0)) {
                leftPower = 1;
                rightPower = 1;
                if (gamepad1.left_stick_x > 0)
                    rightPower -= Math.abs(gamepad1.left_stick_x * 2);
                else if (gamepad1.left_stick_x < 0)
                    leftPower -= Math.abs(gamepad1.left_stick_x * 2);
            }
            else if (gamepad1.left_stick_y < 0 || (gamepad1.left_stick_x < 0 && gamepad1.left_stick_y == 0)) {
                leftPower = -1;
                rightPower = -1;
                if (gamepad1.left_stick_x > 0)
                    leftPower += Math.abs(gamepad1.left_stick_x * 2);
                else if (gamepad1.left_stick_x < 0)
                    rightPower += Math.abs(gamepad1.left_stick_x * 2);
            }
            else {
                leftPower =0;
                rightPower = 0;
            }
            */

            //Spins lift
            //Doesn't run if it is already running, or if one of the claws isn't closed
            //Flips rot sign every time it rotates so it spins back and forth
            if (gamepad1.a && !rotation.isBusy()) { // && clawRP == 1 && clawLP == 0) {
                rotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rotation.setPower(0.25 * rot);
                rotation.setTargetPosition((int)(Command.ENCODERTICKS / 2 * rot));
                rot *= -1;
            }

            if (gamepad1.b && !rotation.isBusy()) { // && clawRP == 1 && clawLP == 0) {
                rotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rotation.setPower(0.25 * rot);
                rotation.setTargetPosition((int)(Command.ENCODERTICKS / 8 * rot));
                rot *= -1;
            }

            if (gamepad1.dpad_left && !rotation.isBusy()) {
                rotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rotation.setPower(0.10);
                rotation.setTargetPosition((int)(Command.ENCODERTICKS / 20));
            }

            if (gamepad1.dpad_right && !rotation.isBusy()) {
                rotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rotation.setPower(-0.10);
                rotation.setTargetPosition((int)(-Command.ENCODERTICKS / 20));
            }

            //Arcade style driving
            //Left is Y value minus X value. When X is to the left, left wheel goes faster. When X is to the right, left wheel goes slower, then negative.
            //Clip value to make sure they're within range
            leftPower = gamepad1.left_stick_y - gamepad1.left_stick_x;
            leftPower = Range.clip(leftPower, -1, 1);
            //Right is Y value plus X value. When X is to the left, right wheel goes slower, then negative. When X is to the right, right wheel goes faster.
            //Clip value to make sure they're within range
            rightPower = gamepad1.left_stick_y + gamepad1.left_stick_x;
            rightPower = Range.clip(rightPower, -1, 1);


            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            //Set motor powers
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);
            //board.setPower(boardPower);
            lift.setPower(liftPower);

            //Set claw powers
            clawR.setPosition(clawRP);
            clawL.setPosition(clawLP);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addData("Lift", "Power: " + liftPower);
            //telemetry.addData("Color", "Red Value: " + colorSensor.red());
            telemetry.update();
        }
    }
}
