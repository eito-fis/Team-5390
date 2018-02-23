//Checks ball, hits ball with arm
//Takes arm motors, servos and turret
//For now the distances will be hardcoded

//imports

package segments;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import commands.Command;
import commands.MoveClaw;
import commands.MoveMotor;
import commands.MoveServo;


//import Commands

public class CheckBallDrop extends Segment {

	//Variables

	private int index;

	//Command Array
	private Command[] commands = new Command[1];
	//blank arrays here

	//Motors
	//Arm motors

	//Servos
	private Servo ballArm;

	//Initialization
	private MoveServo dropArm = new MoveServo(0, 0.9);

	//Constructor
	//Add values to be taken here
	public CheckBallDrop(Servo bA) {
		//Set passed values to object values here
		ballArm = bA;
	}

	//Setup
	public void init () {
		dropArm.setServos(ballArm);
	}

	//Runs at start
	//Runs once
	public void start() {

		//Intialize commands. Defaulted to rotate right
		commands[0] = dropArm;

		index = 0;
		commands[index].init();
		commands[index].start();
	}

	public boolean conditional() { return false; }

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
	}
}
