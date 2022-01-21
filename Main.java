import java.util.ArrayList;
import java.util.Random;
import common.*;
import demo_0549.*;

public class Main {

	public static void main(String[] args) {

		Random rand= new Random();
		ArrayList<Boolean> areCorrect= new ArrayList<Boolean>();
		Game game = new Game_0549();
		ArrayList<Machine> machines = new ArrayList<>();
	
		
		// add a bunch of different machines. For now, all demo machines
		// These will be replaced by machines of different students
		// Add as many as you would like, and change numFaults accordingly (but less than 1/3 of total machines)
		
		machines.add(new Machine_0549(1));
		machines.add(new Machine_0057(2));
		machines.add(new Machine_0096(3));
		machines.add(new Machine_0525(4));
		machines.add(new Machine_0096(5));
		machines.add(new Machine_0525(6));
		machines.add(new Machine_0057(7));
		
		// change the following as needed
		int numFaults = 2;
		int stepSize = 4;
		int sleepTime = 2000;
		
		// Try not to change anything beyond this
		
		int leaderId=rand.nextInt(machines.size());
		for(Machine machine: machines) {
			machine.setStepSize(stepSize); // pixels to move in each step
			machine.start(); // starts the machine running in its own thread
		}
		
		game.addMachines(machines);
		
		for(int i=0;i<100;i++) {
			for(Machine machine: machines) {
				Location pos = machine.getPosition();
				System.out.println(machine.name() + ":" + pos.getX() + "," + pos.getY() ); 
			}
			game.startPhase(leaderId, areCorrect);
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
