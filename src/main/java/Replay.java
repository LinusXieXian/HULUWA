import java.io.*;
import java.util.concurrent.TimeUnit;

public class Replay extends Thread{
	private Battle battle;
	private BufferedReader br;
	
	Replay(Battle battle,File file) throws FileNotFoundException{
		this.battle=battle;
		this.battle.setRecord(true);
		br = new BufferedReader(new FileReader(file));
		start();
	}
	
	@Override
    public void run() {
    	System.out.println("\n\n\n Replaying Round"+battle.getRoundCount()+"\n\n\n");
        String line;  
        try {
        	//�ƶ�״ֻ̬�����֣�һ������ԭ�ر�ɱ�ˣ�һ�����ƶ�����λ��
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
					TimeUnit.MILLISECONDS.sleep(300);
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
