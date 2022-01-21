package demo;
import java.util.ArrayList;
import java.util.Random;
import common.Location;
import common.Machine;

public class Machine_0057 extends Machine {

	private int t_max;
	public Machine_0057() {
		id = nextId++;
		
	}
	
	public  void setMachines(ArrayList<Machine> machines)
	{
		t_max = machines.size()/3;
		this.machines = machines;
	}

	@Override
	public void setStepSize(int stepSize) {
		// if(step != 0)
		step = stepSize;
		// else
		// {
		// 	step = 0;
		// }
	}

	@Override
	public void setState(boolean isCorrect) 
	{
		this.isCorrect = isCorrect;
		phasenumber++;
	}

	@Override
	public void setLeader() 
	{
		//isLeader = true;
		int decision;
		Random rn = new Random();
		decision = rn.nextInt(2);
		
		if(isCorrect)
		{
			for(int i = 0;i<machines.size();i++)
			{
				machines.get(i).sendMessage(id, phasenumber, 0, decision);
			}
		}
		else
		{
			ArrayList<Integer> senders = new ArrayList<>();
			Random rd = new Random();
			int n = 2 * t_max + 1 + rd.nextInt(machines.size() - (2 * t_max + 1) + 1);
			while(senders.size() != n)
			{
				Random r = new Random();
				int k = r.nextInt(machines.size());
				if(!senders.contains(k))
				{
					senders.add(k);
				}
			}
			for(int i = 0;i<senders.size();i++)
			{
				machines.get(senders.get(i)).sendMessage(id, phasenumber, 0, decision);
			}
			for(int i = 0; i<machines.size();i++)
			{
				if(!senders.contains(i))
				{
					machines.get(i).sendMessage(id, phasenumber, 0, rd.nextInt(2));
				}
			}
		}
	}

	@Override
	public void sendMessage(int sourceId, int phaseNum, int r, int decision) 
	{
		
		if(r == 0)
		{
			leader_dec = decision;
			phasenumber = phaseNum;
			
			for(int i = 0;i<machines.size();i++)
			{
			    if(isCorrect)
			    {
			        machines.get(i).sendMessage(id, phaseNum, 1, leader_dec);
			    }
			    else
			    {
					Random rd = new Random();
					int dec = rd.nextInt(2);
					machines.get(i).sendMessage(id, phaseNum, 1, dec);
			    }
		    }
		}
		else if(r == 1 )
		{
			if(decision == 1)
			{
				ones++;
			}
			else if(decision == 0)
			{
				zeroes++;
			}
			if(zeroes+ones > 2*t_max+1)
			{
				go_round_2();
			}
		}
		else if (r == 2)
		{
			if(decision == 0)
			{
				d++;
			}
			else
			{
				nd++;
			}
			//if(d + nd == 3*t_max)
			{
				if(d >=2*t_max+1)
				{
					final_dec = 0;
				end_round2();
				}
				else{
					final_dec = 1;
					end_round2();
				}
			
			}
		}
	}
	void end_round2()
	{
		
			if (final_dec == 0) 
			{
				if (dir.getY() == 0)
				dir.setLoc(0, dir.getX());
				else
				dir.setLoc(dir.getY() * -1, 0);
				move();
			} 
			else 
			{
				if (dir.getY() == 0)
				dir.setLoc(0, dir.getX() * -1);
				else
				dir.setLoc(dir.getY(), 0);
				move();
			}
			
		zeroes = 0;
		ones = 0;
		leader_dec = -1;
		return;

	}
	void go_round_2()
	{
		if(zeroes >= t_max+1)
		{
			if(isCorrect)
			{
			for(int i = 0;i<machines.size();i++)
			{
				machines.get(i).sendMessage(id, phasenumber, 2, 0);
				
			}
			final_dec = 0;
		}
		else{
			Random rd = new Random();
			//int k = rd.nextInt(2);
			//if(k == 1)
			{
			for(int i = 0;i<machines.size();i++)
			{
				machines.get(i).sendMessage(id, phasenumber, 2, rd.nextInt(2));
				
			}
		}
		}
			
		}
		else
		{
			if(isCorrect)
			{
			for(int i = 0;i<machines.size();i++)
			{
				machines.get(i).sendMessage(id, phasenumber, 2, 1);
			}
			final_dec = 1;
		}
		else{
			Random rd = new Random();
			//int k = rd.nextInt(2);
			//if(k == 1)
			{
			for(int i = 0;i<machines.size();i++)
			{
				machines.get(i).sendMessage(id, phasenumber, 2, rd.nextInt(2));
				
			}
		}
		}
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
		return "0057_"+id;
	}

	@Override
	public Location getPosition() {
		
		return new Location(pos.getX(), pos.getY());
	}
	private int d,nd;
	private boolean isCorrect;
	private int leader_dec = -1;
	private int  ones = 0;
	private int zeroes = 0;
	//private boolean isLeader ;
	private int final_dec;
	private ArrayList<Machine> machines;
	private int phasenumber = 0;
	private int step;
	private Location pos = new Location(0,0);
	private Location dir = new Location(1,0); // using Location as a 2d vector. Bad!
	private static int nextId = 0;
	private int id ;
	
}
