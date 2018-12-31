
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author xiexian
 *
 */
public class Creature extends ImageView implements Runnable{
	protected String name;
	protected boolean alive=true;
	protected Battle battle;
	protected boolean testFlag=false;
	
	Creature(String name,Battle battle,boolean testFlag){
		this.name=name;
		this.battle=battle;
		if(!testFlag) {
			setImage();
		}
	}
	
	
	@Override
	public void run() {
		Lock landLock = battle.getLand().getLock();
	    try{
	            while(this.isAlive()) {
	            	int x = (int)(getLayoutX()-Position.X_START) / Position.HOLDER_LRNGTH;
	                int y = (int)(getLayoutY()-Position.Y_START) / Position.HOLDER_WIDTH;
	                //����߶�
	        		Random rand = new Random();
	            	int newX = x+rand.nextInt(3)-1;
	                int newY = y+rand.nextInt(3)-1;
	                int tryTimes=1;
	                landLock.lock();//����
	                System.out.println(name+" : gets the lock");
	                //��ȡ������ʱ������Ѿ���ɱ��
	                if(!this.isAlive()) {
	                	landLock.unlock();
	                	System.out.println(name+" : return the lock");
	                	break;
	                }
	                //��Խ�������ߵ��л���ѵ�λ��ʱ������ѡ���ж�λ��
	                while(( outOfBound(newX,newY) ||  hasAliveCompanion(newX,newY)) && tryTimes<5){
		                newX = x+rand.nextInt(3)-1;
		                newY = y+rand.nextInt(3)-1;
		                tryTimes++;
		            }
	                //�ƶ�����λ��
	                moveTo(newX, newY);
	                //����ս��״̬�����Ƿ����
	                battle.updateBattleStatus();
	                TimeUnit.MILLISECONDS.sleep(50+rand.nextInt(30));   
	                landLock.unlock();//����
	                System.out.println(name+" : return the lock");
	            }      
	     }catch (InterruptedException e) {
	    	 	landLock.tryLock();
	    	 	landLock.unlock();	    	 
	    	 	System.out.println(name+" : return the lock");
	            System.out.println(name+" : thread is interrupted");
	     }
	
        System.out.println(name+" : thread is over");
	}
	
	
	public boolean outOfBound(int x,int y) {
		if(x<0 || x>=Land.LENGTH || y<0 || y>=Land.WIDTH) {
			return true;
		}
		return false;
	}
	
	//�ж�һ��λ���Ƿ��л�Ķ���
	public boolean hasAliveCompanion(int newX,int newY) {
		Creature c = battle.getLand().getObj(newX,newY);
		if(c!=null && c.isAlive()) {
			if((isHuman(c) && isHuman(this)) || (!isHuman(c) && !isHuman(this))) {
				//System.out.println(name+" : thread hasAliveCompanion:"+newX+","+newY);
				return true;
			}
		}
		return false;
	}
	
	//�ж�һ��λ���Ƿ��л�ĵ���
	public boolean hasAliveEnemy(int newX,int newY) {
		Creature c = battle.getLand().getObj(newX,newY);
		if(c!=null && c.isAlive()) {
			if((isHuman(c) && !isHuman(this)) || (!isHuman(c) && isHuman(this))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isHuman(Creature c) {
		if(c instanceof HuLuWa || c instanceof Grandpa) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//���������ͼƬλ��
	public void setLayout(int x, int y){
        setLayoutX(x * Position.HOLDER_LRNGTH + Position.X_START);
        setLayoutY(y * Position.HOLDER_WIDTH + Position.Y_START);
    }

	public boolean isAlive() {
		return alive;
	}
	
	public boolean getTestFlag() {
		return testFlag;
	}
	
	//������������ѡȡͼƬ
	private void setImage(){
        String imageName = name + ".png";
        if(!isAlive()) {
        	imageName = "tomb.png";
        }      
        setImage(new Image(getClass().getResource(imageName).toExternalForm()));
        setFitWidth(Position.HOLDER_LRNGTH);
        setFitHeight(Position.HOLDER_WIDTH);
    }
	
	public void moveTo(int newX,int newY) {
		assert this.isAlive();
		if(outOfBound(newX,newY) || hasAliveCompanion(newX,newY)) {
			return;
		}
		if(hasAliveEnemy(newX,newY)) {//�������˵��ˣ���ս��
			fight(newX,newY);
		}
		else if(battle.getLand().isEmpty(newX, newY)) {//���ǿ�λ�����߹�ȥ
			stepInto(newX, newY);
		}
	}

	
	public void fight(int newX,int newY) {
		int x = (int)(getLayoutX()-Position.X_START) / Position.HOLDER_LRNGTH;
        int y = (int)(getLayoutY()-Position.Y_START) / Position.HOLDER_WIDTH;
		Creature enemy=battle.getLand().getObj(newX,newY);
		//����ƽ��
		Random rnd = new Random();
		int code=rnd.nextInt(100); 
		if(code>50) {
			enemy.die();
			stepInto(newX, newY);
		}
		else {
			this.die();
			enemy.stepInto(x,y);
		}
	}
	
	//�ߵ�һ����λ
	public void stepInto(int newX,int newY) {	
		int x = (int)(getLayoutX()-Position.X_START) / Position.HOLDER_LRNGTH;
        int y = (int)(getLayoutY()-Position.Y_START) / Position.HOLDER_WIDTH;
		System.out.println(name+" : ("+x+","+y+") -> ("+newX+","+newY+")");
		battle.getLand().removeObj(x,y);
		battle.getLand().setObj(newX, newY,this);
		if(!battle.isRecord()) {
			try {
	            battle.getBufferedWriter().write(String.format("(%d,%d)->(%d,%d)%n", x, y, newX, newY));
	        }catch (IOException e){
	            System.err.println("error while recording");
	        }
		}
	}
	
	public void die() {
		//����Լ�λ���ϵ�ͼƬ����һ��Ĺ��
		int x = (int)(getLayoutX()-Position.X_START) / Position.HOLDER_LRNGTH;
        int y = (int)(getLayoutY()-Position.Y_START) / Position.HOLDER_WIDTH;
		battle.getLand().removeObj(x,y);
		alive=false;
		setImage();
		if(!battle.isRecord()) {
			try {
	            battle.getBufferedWriter().write(String.format("(%d,%d)->die%n", x, y));
	        }catch (IOException e){
	            System.err.println("error while recording");
	        }
		}
	}

}
