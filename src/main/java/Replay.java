import java.io.*;
import java.util.concurrent.TimeUnit;

public class Replay extends Thread{
	private Battle battle;
	private BufferedReader br;
	
	Replay(Battle battle,String fileName) throws FileNotFoundException{
		this.battle=battle;
		this.battle.setRecord(true);
		br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		start();
	}
	
	@Override
    public void run() {
    	System.out.println("\n\n\n Replaying Round"+battle.getRoundCount()+"\n\n\n");
        String line;  
        try {
			while ((line = br.readLine()) != null) {  
	            if(line.contains("die")) {
	            	int x=line.charAt(1)-'0';
	            	int y=line.charAt(3)-'0';
	            	Creature c=battle.getLand().getObj(x, y);
	            	c.die();
	            }
	            else {
	            	int x=line.charAt(1)-'0',y=line.charAt(3)-'0',newX=line.charAt(8)-'0',newY=line.charAt(10)-'0';	      
	            	Creature c=battle.getLand().getObj(x, y);
	            	c.stepInto(newX,newY);
	            }
	            try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        finally {
        	try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		
	}
	
}
