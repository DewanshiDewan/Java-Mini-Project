package demo;
import common.Location;
import common.Machine;
import java.util.*;
import java.io.*;

public class Machine_0525 extends Machine {
	private int numberOfFaultyMachines;
	private ArrayList<Machine> machines = new ArrayList<Machine>();


	private String name;
	private int id;
	private static int nextId = 0;
	private int sourceID;

	private Location pos = new Location(0,0);
	private Location dir = new Location(0,1);

	private int step;

	private boolean state;
	private int current_round = 0;
	private int phase_number = 0;
	private int leftTurns1, leftTurns2, rightTurns1, rightTurns2;
	
	private int final_decision = -1;

	public Machine_0525() {
		this.id = nextId++;
		name = "0525";
		
		leftTurns1 = leftTurns2 = rightTurns1 = rightTurns2 = 0;
	}

	@Override
	public void setMachines(ArrayList<Machine> machines) {
		this.machines = machines;
		numberOfFaultyMachines = machines.size()/3;
		if (machines.size() % 3 == 0) {
			numberOfFaultyMachines -= 1;
		}

		sourceID = machines.indexOf(this);

		current_round = 0;

		boolean leadership = false;
	}

	@Override
	public void setStepSize(int stepSize) {
		step = stepSize;
	}

	@Override
	public void setState(boolean isCorrect) {
		this.state = isCorrect;
		leftTurns1 = leftTurns2 = rightTurns1 = rightTurns2 = 0;
		++phase_number;
	}

	@Override
	public void setLeader() {
		boolean leadership = true;

		Random rand = new Random();
		int decision = rand.nextInt(2);

		if (state == true) {
			for(Machine machine: machines) {
				machine.sendMessage(sourceID, phase_number, 0, decision);
			}
		}

		else if ((state) == false) {
			int correctMessages = 2 * numberOfFaultyMachines + 1 + rand.nextInt(numberOfFaultyMachines);
			ArrayList<Integer> machinesGettingMsg = new ArrayList<Integer>();

			while (machinesGettingMsg.size() != correctMessages) {
				if (!machinesGettingMsg.contains(rand.nextInt(correctMessages))) {
					machinesGettingMsg.add(rand.nextInt(correctMessages));						
				}
			}

			for(int i = 0; i < machines.size(); i++) {
				if (machinesGettingMsg.contains(i)) {
					machines.get(i).sendMessage(sourceID, phase_number, 0, decision);
				}
			}
		}
		leadership = false;
	}

	void round1(int decision) {
		Random rand = new Random();
		if (state) {
			for (Machine machine : machines) {
				machine.sendMessage(sourceID, phase_number, 1, decision);
			}
		}
		else {
			int faultyDecision = rand.nextInt(3);  // faultyDecision == 2 means machine will stay silent.
			if (faultyDecision != 2) {
				for (Machine machine: machines) {
					machine.sendMessage(sourceID, phase_number, 1, faultyDecision);
				}
			}
		}
	}


	void round2(int decision) {
		Random rand = new Random();
		if (state) {
			for (Machine machine : machines) {
				machine.sendMessage(sourceID, phase_number, 2, decision);
			}
		}
		else {
			int faultyDecision = rand.nextInt(3);  // faultyDecision == 2 means machine will stay silent.
			if (faultyDecision != 2) {
				for (Machine machine: machines) {
					machine.sendMessage(sourceID, phase_number, 2, faultyDecision);
				}
			}
		}
	}

	@Override
	public void sendMessage(int sourceId, int phaseNum, int roundNum, int decision) {
		if (roundNum == 0) {
			current_round = 1;
			round1(decision);
		}

		else if(roundNum == 1 && current_round < 2) {
			if (decision == 0) { leftTurns1++; }
			else if (decision == 1) { rightTurns1++; }

			if (leftTurns1 + rightTurns1 >= 2*numberOfFaultyMachines + 1) {
				if (leftTurns1 > numberOfFaultyMachines) {
					current_round = 2;
					round2(0);
				}

				else if (rightTurns1 > numberOfFaultyMachines) {
					current_round = 2;
					round2(1);
				}
			}
		}

		else if(roundNum == 2 && current_round < 3){
			if (decision == 0) { leftTurns2++; }
			else if (decision == 1) { rightTurns2++; }

			if(leftTurns2 > 2*numberOfFaultyMachines) {
				current_round = -1;
				final_decision = 0;
			}
			else if(rightTurns2 > 2*numberOfFaultyMachines) {
				current_round = -1;
				final_decision = 1;
			}
			else {
				current_round = -1;
				final_decision = 2;
			}

			System.out.println(name() + ": Final decision " + final_decision);

			if(final_decision == 0) {
				if(dir.getY() == 0) { dir.setLoc(0, dir.getX()); }
				else { dir.setLoc(dir.getY() * -1, 0); }
			}
			else if(final_decision == 1) {
				if (dir.getY() == 0) { dir.setLoc(0, dir.getX() * -1); }
				else { dir.setLoc(dir.getY(), 0); }
			}
			else { dir.setLoc(dir.getX(), dir.getY()); }
		}
	}

	@Override
	public
	void move() {
		pos.setLoc(pos.getX() + dir.getX()*step, 
					pos.getY() + dir.getY()*step);
			
	}

	@Override
	public
	String name() {
		return "demo_"+ name;
	}

	@Override
	public Location getPosition() {
		
		return new Location(pos.getX(), pos.getY());
	}
}