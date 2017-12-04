//Checks ball, hits ball with arm
//Takes arm motors, servos and turret
//For now the distances will be hardcoded

//imports

package segments;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import commands.Command;
import commands.MoveForward;
import commands.MoveServo;
import commands.MoveTurn;


//import Commands

public class CheckBallHit extends Segment {

	//Variables

	private int index;

	//Command Array
	private Command[] commands = new Command[6];
	//blank arrays here

	//Color
	//1 is blue, 2 is red
	private int color;
	private int spot;

	//Motors
	//Arm motors
	private DcMotor motorR;
	private DcMotor motorL;
	private DcMotor lateral;

	//Servos
	//Claw servos
	private Servo clawR;
	private Servo clawL;
	private Servo ballHolder;
	private Servo ballArm;
	private Servo ballHitter;

	private ColorSensor colorSensor;

	//Initialization
	private MoveServo hitR = new MoveServo(0.5, 0);
	private MoveServo hitL = new MoveServo(0.5, 1);
	private MoveForward moveForward = new MoveForward(0.5, 0.5, 17, 17);
	private MoveForward moveBackwards = new MoveForward(-0.5, -0.5, 17, 17);
	private MoveServo returnR = new MoveServo(0, 0.3);
	private MoveServo returnL = new MoveServo(1, 0.3);
	private MoveServo raiseArm = new MoveServo(0.5, 0);
	private MoveServo closeHolder = new MoveServo(0, 1);
	private MoveTurn moveTurnF = new MoveTurn(0.5, 0, 85);
	private MoveTurn moveTurnB = new MoveTurn(0.5, 1, 85);

	//Constructor
	//Add values to be taken here
	public CheckBallHit(DcMotor mL, DcMotor mR, DcMotor l, Servo bHl, Servo bA, Servo bHt, ColorSensor cS, int clr, int spt){
		//Set passed values to object values here

		//Color
		color = clr;
		spot = spt;

		//Motors
		motorR = mR;
		motorL = mL;

		ballHolder = bHl;
		ballArm = bA;
		ballHitter = bHt;

		colorSensor = cS;
	}

	//Setup
	public void init () {
		//Make commands
		//First move arm and turret
		moveForward.setMotors(motorR, motorL);
		moveBackwards.setMotors(motorR, motorL);
		moveTurnF.setMotors(motorR, motorL);
		moveTurnB.setMotors(motorR, motorL);
		hitR.setServos(ballHitter);
		hitL.setServos(ballHitter);
		returnR.setServos(ballHitter);
		returnL.setServos(ballHitter);
		raiseArm.setServos(ballArm);
		closeHolder.setServos(ballHolder);
		colorSensor.enableLed(true);
		//ReadBall.init();
	}

	//Runs at start
	//Runs once
	public void start() {
		int imageNumber;
		//imageNumber = BeaconAnalyzer.bColor;
		if (colorSensor.red() >= 1)
			imageNumber = 1;
		else
			imageNumber = 0;

		//Intialize commands. Defualted to rotate right
		commands[0] = hitR;
		commands[1] = raiseArm;
		commands[2] = returnR;
		commands[3] = closeHolder;
		commands[4] = moveForward;
		commands[5] = moveTurnF;
		//Switch to roate left if ball is on other side
		if (imageNumber == color) {
			commands[0] = hitL;
			commands[2] = returnL;
		}
		if (spot == -1) {
			commands[4] = moveBackwards;
			commands[5] = moveTurnB;
		}
		index = 0;
		commands[index].init();
		commands[index].start();}

	//Loops
	public boolean loop() {
		if (commands[index].loop())
			return true;
		else {
			commands[index].stop();
			index++;
			if (index == commands.length)
				return false;
			else {
				commands[index].init();
				commands[index].start();
			}
			return true;
		}
	}

	//Stops
	public void stop(){
		//Stop motors
		motorR.setPower(0);
		motorL.setPower(1);
	}
}