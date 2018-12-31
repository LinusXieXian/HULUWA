
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Battle {
	private Land land;
	private List<Creature> creatures ;
	private ExecutorService executorService;
	private BufferedWriter bufferedWriter = null;
	private boolean isRecord = false;//用于区分是否处于回放状态
	public static final String recordFileName="record.txt";
	private static int roundCount=0;
	
	Battle(){
		//初始化战场
		land = new Land();
		creatures = new ArrayList<>();
	}

	
	public void battleStart() {
		setRecord(false);
    	Battle.roundCount++;
		System.out.println("\n\n\nRound"+roundCount+"\n\n\n");
		try {
            bufferedWriter = new BufferedWriter(new FileWriter(recordFileName));
        }catch (IOException e){
            System.err.println("error while open record.txt");
        }
		//各生物线程开始执行，战斗开始！
		executorService = Executors.newFixedThreadPool(creatures.size());
        for(Creature c : creatures) {
            executorService.execute(c);
        }
    }
    
    public void battleInit() {
    	creatures=new ArrayList<>();
    	//打扫战场
    	land.removeAllObj();
    	//新的回合设置一把新锁
    	land.getNewLock();
    	//初始化葫芦娃、爷爷、蝎子精蛇精小喽喽对象，并确定它们的初始坐标（摆好阵形）
    	placeCreature(new Grandpa("Grandpa",this),1,4);
    	placeCreature(new HuLuWa(1,HuLuWa.Color.red,this),0,1);
    	placeCreature(new HuLuWa(2,HuLuWa.Color.orange,this),0,2);
    	placeCreature(new HuLuWa(3,HuLuWa.Color.yellow,this),0,3);
    	placeCreature(new HuLuWa(4,HuLuWa.Color.green,this),0,4);
    	placeCreature(new HuLuWa(5,HuLuWa.Color.cyan,this),0,5);
    	placeCreature(new HuLuWa(6,HuLuWa.Color.blue,this),0,6);
    	placeCreature(new HuLuWa(7,HuLuWa.Color.purple,this),0,7);

    	placeCreature(new Scorpion("Scorpion",this),4,4);			
    	placeCreature(new Snake("Snake",this),5,4);
    	placeCreature(new Lesser("Lesser",this),5,3);
    	placeCreature(new Lesser("Lesser",this),5,5);
    	placeCreature(new Lesser("Lesser",this),6,2);
    	placeCreature(new Lesser("Lesser",this),6,6);
    	placeCreature(new Lesser("Lesser",this),7,3);
    	placeCreature(new Lesser("Lesser",this),7,5);
    	placeCreature(new Lesser("Lesser",this),8,4);
    }
    
    
    public void placeCreature(Creature c,int x,int y) {
    	creatures.add(c);
    	land.setObj(x,y,c);
    }
       
    public void battleReplay(Stage primaryStage) {
    	//初始化战场用于回放
		battleInit();
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("open record file");
        fileChooser.setInitialDirectory(new File("."));
        File file = fileChooser.showOpenDialog(primaryStage);
        try {
			new Replay(this, file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    }
    
    public void updateBattleStatus() {
    	//检查战斗是否结束
		int aliveHumanNum = 0, aliveMonsterNum = 0;
		for(int i=0;i<Land.LENGTH;i++)
			for(int j=0;j<Land.WIDTH;j++){
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
        	System.out.println("Battle Over!");
        }
    }
    
    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }
    
    public Land getLand() {
		return land;
	}
    
    public int getRoundCount() {
		return roundCount;
	}
    
    public boolean isRecord() {
		return isRecord;
	}
    
    public void setRecord(boolean isRecord) {
		this.isRecord = isRecord;
	}

}
