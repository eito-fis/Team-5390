//Moves Claw
//Takes 4 sero positions

//imports
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class MoveClaw extends Command {

	//Variables
	
	//Servos
	private Servo claw;
	private Servo lateral;
	private Servo vertical;
	//Servo Position
	private int clawPosition;
	private int lateralPosition;
	private int vericalPosition;
	//Elapsed Time
	private ElapsedTime runTime;

	//Constructor
	public MoveClaw(Servo c, Servo l, Servo v, int cP, int lP, int vP) {
		//Sets to passed variables

		//Servos
		claw = c;
		lateral = l;
		vertical = v;
		//Servo Positions
		clawPosition = cP;
		lateralPosition = lP;
		verticalPosition = vP;
	}

	//Setup
	@Override
	public void init() {
		//Reset servo positions
		claw.setPosition(0);
		lateral.setPosition(0);
		vertical.setPosition(0);

		//Elapsed Time
		runTime = new ElapsedTime();
	}

	//Runs at start
	//Runs once
	@Override
	public void start() {
		//Set servo positionss
		claw.setPosition(clawPosition);
		lateral.setPosition(lateralPosition);
		vertical.setPosition(verticalPosition);

		//Reset Elapsed Time
		runTime.reset();
	}

	//Loops
	@Override
	public void loop() {
		//Wait for 1 second
		while(runTime.time() < 1) {}
	}

	//Stops
	@Override
	public void stop(){
		//Empty
	}
}