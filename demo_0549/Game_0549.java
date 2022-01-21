package demo_0549;

import java.util.*;
import common.Game;
import common.Machine;
/* class Game:
This is initialized with the list of machines, and the number of faulty machines - t. We can
assume that the number of machines, n > 3t
It runs a series of phases. For each phase:
● it informs each machine whether it is correct or faulty (invoking a method setState
on the machine), by randomly tagging t of them as faulty.
● The leader is then expected to initiate that phase by executing
Round 0 as above.
The Game instance decides which are the faulty machines and which is the leader for each
phase. */

public class Game_0549 extends Game {

	private ArrayList<Machine> machines;
	private int num_of_machines, curr_phase;// Totalmachines,phase

	public Game_0549() {
		// initializing phase to 0
		this.curr_phase = 0;
		this.machines = new ArrayList<Machine>();
	}

	@Override
	public void addMachines(ArrayList<Machine> machines) {
		this.machines.addAll(machines);
		this.num_of_machines = machines.size();
	}

	@Override
	public void startPhase(int leaderId, ArrayList<Boolean> areCorrect) {
		System.out.println("The Phase: " + curr_phase + " has started");
		curr_phase = curr_phase + 1;
		System.out.println("Faulty Machines here are: ");
		for (int i = 0; i < num_of_machines; i++) 
		{
			if (!areCorrect.get(i)) 
			{
				machines.get(i).setState(false);
				System.out.print(machines.get(i).name() + " ");
			} 
			else 
			{
				machines.get(i).setState(true);
			}
		}
		System.out.println();
		for (Machine m : machines) 
		{
			m.setMachines(this.machines);
		}
		// Chooses one of the machines as the leader, and informs that machine that it
		// is the leader.
		//int n=rand.nextInt(machines.size());
		machines.get(leaderId).setLeader();
		System.out.println("The Leader here is: " + leaderId);
	}

}
