import java.io.*;
import java.util.*;
import java.util.concurrent.*;


import javafx.stage.Stage;

public class Battle {
	private Land land;
	private List<Creature> creatures ;
	private ExecutorService executorService;
	private BufferedWriter bufferedWriter = null;
	
	Battle(){
		land = new Land();
		creatures = new ArrayList<>();
	}

	public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }
	
	public void battleStart() {
		try {
            bufferedWriter = new BufferedWriter(new FileWriter("record.txt"));
        }catch (IOException e){
            System.err.println("error while opening record.txt");
        }
		battleInit();
		
		executorService = Executors.newFixedThreadPool(creatures.size());
        for(Creature c : creatures)
            executorService.execute(c);
    }
    
    public void battleInit() {
    	creatures.clear();
    	land.removeAllObj();
    	//葫芦娃：（2，1）-》（2，7）爷爷:(3,4) 蝎：（8，4）蛇：（9，4）喽（9，3）（9，5）（10，2）（10，6）（11，3）（11，5）（12，4）
    	//初始化葫芦娃、爷爷、蝎子精蛇精小喽喽对象，每个对象初始化的同时确定它初始的x，y坐标（摆好阵形）用一个函数把他们各自的图片展示到界面上
    	Grandpa grandpa=new Grandpa("Grandpa",land,land.getPosition(1,4),this);
    	HuLuWa []huluwa=new HuLuWa[HuLuWa.huluwaNum];
    	huluwa[0]=new HuLuWa(1,HuLuWa.Color.yellow,land,land.getPosition(0,1),this);
		huluwa[1]=new HuLuWa(2,HuLuWa.Color.cyan,land,land.getPosition(0,2),this);
		huluwa[2]=new HuLuWa(3,HuLuWa.Color.blue,land,land.getPosition(0,3),this);
		huluwa[3]=new HuLuWa(4,HuLuWa.Color.red,land,land.getPosition(0,4),this);
		huluwa[4]=new HuLuWa(5,HuLuWa.Color.orange,land,land.getPosition(0,5),this);
		huluwa[5]=new HuLuWa(6,HuLuWa.Color.purple,land,land.getPosition(0,6),this);
		huluwa[6]=new HuLuWa(7,HuLuWa.Color.green,land,land.getPosition(0,7),this);
		creatures.add(grandpa);
    	for(int i=0;i<huluwa.length;i++) {
    		creatures.add(huluwa[i]);
    	}
    	
		Scorpion scorpion=new Scorpion("Scorpion",land,land.getPosition(4,4),this);			
		Snake snake=new Snake("Snake",land,land.getPosition(5,4),this);
		Lesser lessers[]=new Lesser[Scorpion.lesserNum];
		lessers[0]=new Lesser("Lesser",land,land.getPosition(5,3),this);	
		lessers[1]=new Lesser("Lesser",land,land.getPosition(5,5),this);	
		lessers[2]=new Lesser("Lesser",land,land.getPosition(6,2),this);
		lessers[3]=new Lesser("Lesser",land,land.getPosition(6,6),this);	
		lessers[4]=new Lesser("Lesser",land,land.getPosition(7,3),this);	
		lessers[5]=new Lesser("Lesser",land,land.getPosition(7,5),this);	
		lessers[6]=new Lesser("Lesser",land,land.getPosition(8,4),this);	
		creatures.add(scorpion);
		creatures.add(snake);
		for(int i=0;i<lessers.length;i++) {
    		creatures.add(lessers[i]);
    	}
		for(Creature creature:creatures) {
			land.setObj(creature.getPos().getX(),creature.getPos().getY(),creature);
		}
    }
    
    public void loadRecord(Stage primaryStage) {
    	
    }
    
    public void updateBattleStatus() {
		int aliveHumanNum = 0, aliveMonsterNum = 0;
		for(int i=0;i<land.LENGTH;i++)
			for(int j=0;j<land.WIDTH;j++){
				Creature c=land.getObj(i,j);
				if(c!=null && c.isAlive()) {
					if(c.isHuman(c)) {
						aliveHumanNum++;
					}
					else {
						aliveMonsterNum++;
					}
				}	
			}

        if(aliveHumanNum == 0 || aliveMonsterNum == 0){
        	executorService.shutdownNow();
        	try {
                bufferedWriter.close();
            }catch (IOException e){
                System.err.println("error while closing record.txt");
            }
        	System.out.println("battleOver!");
        }
    }
    
    
    public Land getLand() {
		return land;
	}

}
