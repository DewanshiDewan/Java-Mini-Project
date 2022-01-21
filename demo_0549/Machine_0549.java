package demo_0549;

import java.util.*;
import common.Machine;
import common.Location;

/* class Machine is an abstract class and is expected to implement methods:
● to be informed if it is faulty and if it is the leader.
● a method setMachines that is called before any phases, and provides the list of
machines in the game. The position of a machine in this list is to be used as its sourceID
when sending messages. The number of faulty machines (t) can be at most ⅓ of the
total number of machines.
● sendMessage: which is invoked by a source machine that wants to send a message to
this instance, during the multi-round protocol
● move: which is invoked by the base class at each time step
● other methods to return name, position etc
● Machines are initially at (0,0) and start moving in direction (0,1) */

public class Machine_0549 extends Machine {

	private int num_of_machines, rNum, left_1, left_2, right_1, right_2, id, curr_phase = -1, step, faulty;
	private ArrayList<Machine> machines;
	private boolean state;
	private Location pos = new Location(0, 0), dir = new Location(1, 0);
	private String name;
	private Random rand;
	private int id
	public Machine_0549() {

		this.name = "Machine_0549" + Integer.toString(id);
		this.rand = new Random();
		this.machines = new ArrayList<Machine>();
		left_1 = left_2 = right_1 = right_2 = 0;
	}

	@Override
	public void setStepSize(int stepSize) {
		step = stepSize;
	}

	@Override
	public void setMachines(ArrayList<Machine> machines) {
		this.machines = machines;
		num_of_machines = machines.size();
		faulty = num_of_machines / 3;
		this.id = machines.indexOf(this);
	}

	@Override
	public void setState(boolean isCorrect) {
		state = isCorrect;
		curr_phase = curr_phase + 1;
		rNum = 0;
		left_1 = left_2 = right_1 = right_2 = 0;
	}

	@Override
	public void setLeader() {
		rNum = 0;
		int decision = rand.nextInt(2);
		if (state == true) 
		{
			for (Machine m : machines) 
			{
				m.sendMessage(id, curr_phase, rNum, decision);
			}
		} 
		else 
		{
			Set<Integer> Normal_machines = new HashSet<Integer>();
			int n;
			int rand2 = num_of_machines - (2 * faulty + 1) + 1;
			n = 2 * faulty + 1 + rand.nextInt(rand2);
			while (Normal_machines.size() != n) 
			{
				Normal_machines.add(rand.nextInt(num_of_machines));
			}
			int i = 0;
			while (i < num_of_machines)
			{
				if (Normal_machines.contains(i)) 
				{
					machines.get(i).sendMessage(id, curr_phase, rNum, decision);
				} 
				else 
				{
					machines.get(i).sendMessage(id, curr_phase, rNum, rand.nextInt(2));
				}
				i++;
			}
		}
	}

	@Override
	public void sendMessage(int sourceId, int phaseNum, int roundNum, int decision) {
		if (roundNum == 0) //the one we are reciving the message from
		{
			rNum = 1;//our machine's round
			if (state)
			{
				for (Machine m : machines)
				{
					m.sendMessage(id, curr_phase, rNum, decision);
				}
			} 
			else 
			{
				int n = rand.nextInt(2);
				int decision2 = rand.nextInt(2);
				if (n == 0) 
				{
					for (Machine m : machines) 
					{
						m.sendMessage(id, curr_phase, rNum, decision2);
					}
				}
			}
		} 
		else if (roundNum == 1 && rNum != 2)
		{
			if (decision == 0) 
			{
				left_1 = left_1 + 1;
			} 
			else 
			{
				right_1 = right_1 + 1;
			}
			if (left_1 + right_1 >= (2 * faulty + 1)) 
			{
				int decision2;
				if (left_1 >= faulty + 1) 
				{
					decision2 = 0;
				} 
				else 
				{
					decision2 = 1;
				}
				rNum = 2;
				if (state) 
				{
					for (Machine m : machines)
					{
						m.sendMessage(id, curr_phase, rNum, decision2);
					}
				} 
				else 
				{
					int n = rand.nextInt(2);
					int decision3 = rand.nextInt(2);
					if (n == 0) 
					{
						for (Machine m : machines)
						{
							m.sendMessage(id, curr_phase, rNum, decision3);
						}
					}
				}
			}
		} 
		else if (roundNum == 2 && rNum != -1) 
		{
			if (decision == 0)
			{
				left_2 = left_2 + 1;
			}
			else
			{
				right_2 = right_2 + 1;
			}
			if (left_2 >= 2 * faulty + 1) 
			{
				if (dir.getY() == 0)
				{
					dir.setLoc(0, dir.getX());
				}
				else if (dir.getY() != 0)
				{
					dir.setLoc(dir.getY() * -1, 0);
				}
			} 
			else 
			{
				if (dir.getY() == 0)
				{
					dir.setLoc(0, dir.getX() * -1);
				}
				else if (dir.getY() !=0)
				{
					dir.setLoc(dir.getY(), 0);
				}
			}
			move();
			right_1 = right_2 = 0;
			left_1 = left_2 = 0;
			rNum = -1;
		}
	}

	@Override
	public Location getPosition() {
		return new Location(pos.getX(), pos.getY());
	}

	@Override
	public void move() {
		pos.setLoc(pos.getX() + dir.getX() * step, pos.getY() + dir.getY() * step);
		System.out.println(pos.getX() + " " + pos.getY());
	}

	@Override
	public String name() {
		return name;
	}

}
