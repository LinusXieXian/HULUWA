
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
	                //随机走动
	        		Random rand = new Random();
	            	int newX = x+rand.nextInt(3)-1;
	                int newY = y+rand.nextInt(3)-1;
	                int tryTimes=1;
	                landLock.lock();//上锁
	                System.out.println(name+" : gets the lock");
	                //获取到锁的时候可能已经被杀了
	                if(!this.isAlive()) {
	                	landLock.unlock();
	                	System.out.println(name+" : return the lock");
	                	break;
	                }
	                //当越界或可能走到有活队友的位置时，重新选择行动位置
	                while(( outOfBound(newX,newY) ||  hasAliveCompanion(newX,newY)) && tryTimes<5){
		                newX = x+rand.nextInt(3)-1;
		                newY = y+rand.nextInt(3)-1;
		                tryTimes++;
		            }
	                //移动到新位置
	                moveTo(newX, newY);
	                //跟新战斗状态，看是否结束
	                battle.updateBattleStatus();
	                TimeUnit.MILLISECONDS.sleep(50+rand.nextInt(30));   
	                landLock.unlock();//解锁
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
	
	//判断一个位置是否有活的队友
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
	
	//判断一个位置是否有活的敌人
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
	
	//设置生物的图片位置
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
	
	//根据生物名字选取图片
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
		if(hasAliveEnemy(newX,newY)) {//若碰上了敌人，则战斗
			fight(newX,newY);
		}
		else if(battle.getLand().isEmpty(newX, newY)) {//若是空位，则走过去
			stepInto(newX, newY);
		}
	}

	
	public void fight(int newX,int newY) {
		int x = (int)(getLayoutX()-Position.X_START) / Position.HOLDER_LRNGTH;
        int y = (int)(getLayoutY()-Position.Y_START) / Position.HOLDER_WIDTH;
		Creature enemy=battle.getLand().getObj(newX,newY);
		//众生平等
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
	
	//走到一个空位
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
		//清除自己位置上的图片，放一个墓碑
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
