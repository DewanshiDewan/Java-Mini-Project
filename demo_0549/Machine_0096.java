package demo;
import common.Location;
import common.Machine;
import java.util.*;

public class Machine_0096 extends Machine{
    private int sourceID, left1, right1, left2, right2;
    private int stepSize, numFault, decision, phaseNum;
    private String Name;
    private boolean isCorrect,leadership, flag1, flag2;
    private ArrayList<Machine> machines = new ArrayList<Machine>();
    private Location location; 
    private String CurDir;
    
    public Machine_0096(){
        Name = "0096";
        location = new Location(0,0);
        leadership = flag1 = flag2 = false;
        decision = left1 = right1 = left2 = right2 = phaseNum = 0;
        CurDir = "North";}
    public String name(){
        return "demo_" + Name;}
    public Location getPosition(){
        return location;}
    public void setMachines(ArrayList<Machine> machines){
        this.machines = machines;
        if(machines.size() % 3 == 0)
            numFault = (machines.size()/3) - 1;
        else 
            numFault = machines.size()/3;
        for(int i=0;i<machines.size();i++){
            if(machines.get(i) == this)
                sourceID = i;}
    }
    public void setLeader(){
        leadership = true;
        Random rand = new Random();
        if(isCorrect == false){
            int t;
            if(numFault == 0)
                t = 0;
            else
                t = rand.nextInt(numFault);
            ArrayList<Integer> choose = new ArrayList<Integer>();
            int n;
            while(true){
                if(choose.size()==t)
                    break;
                n = rand.nextInt(machines.size());
                if(choose.contains(n)==false)
                    choose.add(n);
            }
            n = rand.nextInt(2);
            for(int i=0;i<machines.size();i++){
                if(choose.contains(i)==false)
                    machines.get(i).sendMessage(sourceID, phaseNum, 0, n);
            }
        }
        else{
            int n;
            n = rand.nextInt(2);
            for(int i=0;i<machines.size();i++)
                machines.get(i).sendMessage(sourceID, phaseNum, 0, n);
        }
    }
    public void setState(boolean isCorrect){
        this.isCorrect = isCorrect;
        left1 = right1 = left2 = right2 = 0;
        flag1 = flag2 = false;
        leadership = false;}
    public void sendMessage(int sourceId, int phaseNum, int roundNum, int decision){
        Random rand = new Random();
        if(roundNum == 0){
            if(isCorrect == false)
                decision = rand.nextInt(3) - 1;
            for(int i=0;i<machines.size();i++){
                if(decision != -1)
                    machines.get(i).sendMessage(this.sourceID, phaseNum, 1, decision);}
        }
        else if(roundNum == 1){
            if(decision == 0)
                left1++;
            else if(decision == 1) 
                right1++;
            if(left1+right1 >= 2*numFault+1){
                if(isCorrect == true)
                    this.decision = (left1 > right1 ? 0 : 1);
                else if(isCorrect == false)
                    this.decision = rand.nextInt(3) - 1;
                if(this.decision != -1 && flag1 == false){
                    for(int i=0;i<machines.size();i++)
                        machines.get(i).sendMessage(this.sourceID, phaseNum, 2, this.decision);
                    flag1 = true;}
            }
        }
        else if(roundNum == 2){
            if(decision == 0)
                left2+=1;
            else if(decision == 1)
                right2+=1;
            if(left2 >= 2*numFault + 1 || right2 >= 2*numFault + 1){
                if(left2 > right2)
                    this.decision = 0;
                else 
                    this.decision = 1;
                if(isCorrect == false)
                    this.decision = rand.nextInt(2);
                flag2 = true;}            
        }
    }
    public void setStepSize(int stepSize){
        this.stepSize = stepSize;}
    public void move(){
        if(flag2 == true){
            if((CurDir == "East" && decision == 0) || (CurDir == "West" && decision == 1))
                CurDir = "North";
            else if((CurDir == "North" && decision == 0) || (CurDir == "South" && decision == 1))
                CurDir = "West";
            else if((CurDir == "North" && decision == 1) || (CurDir == "South" && decision == 0))
                CurDir = "East";
            else if((CurDir == "East" && decision == 1) || (CurDir == "West" && decision == 0))
                CurDir = "South";
            phaseNum++;
            flag2 = false;}
        if(CurDir == "North")
            location.setLoc(location.getX(), location.getY()+stepSize);
        else if(CurDir == "South")
            location.setLoc(location.getX(), location.getY()-stepSize);
        else if(CurDir == "West")
            location.setLoc(location.getX()-stepSize, location.getY());
        else if(CurDir == "East")
            location.setLoc(location.getX()+stepSize, location.getY());
        }
    
}
