
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
	protected Land homeTown;
	protected Position pos;
	protected Battle battle;
	
	Creature(String name,Land homeTown,Position pos,Battle battle){
		this.name=name;
		this.homeTown=homeTown;
		this.pos=pos;
		this.battle=battle;
		setImage();
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Random rand = new Random();
		Lock landLock = homeTown.getLock();
	    try{
	            while(this.isAlive()) {
	            	int newX = pos.getX()+rand.nextInt(3)-1;
	                int newY = pos.getY()+rand.nextInt(3)-1;
	                int tryTimes=1;
	                
	                landLock.lock();//上锁
	                System.out.println(name+" : thread gets the lock");
	                if(!this.isAlive()) {
	                	landLock.unlock();
	                	System.out.println(name+" : thread return the lock");
	                	break;
	                }
	                while(( outOfBound(newX,newY) ||  hasAliveCompanion(newX,newY)) && tryTimes<5){
		                newX = pos.getX()+rand.nextInt(3)-1;
		                newY = pos.getY()+rand.nextInt(3)-1;
		                tryTimes++;
		            }
	                moveTo(newX, newY);
	                battle.updateBattleStatus();
	                TimeUnit.MILLISECONDS.sleep(50 + rand.nextInt(10));   
	                landLock.unlock();//解锁
	                System.out.println(name+" : thread return the lock");
	            }
	    	
	    	TimeUnit.MILLISECONDS.sleep(100 + rand.nextInt(100));        
	     }catch (InterruptedException e) {
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
	
	public boolean hasAliveCompanion(int newX,int newY) {
		Creature c = homeTown.getObj(newX,newY);
		if(c!=null && c.isAlive()) {
			if((isHuman(c) && isHuman(this)) || (!isHuman(c) && !isHuman(this))) {
				//System.out.println(name+" : thread hasAliveCompanion:"+newX+","+newY);
				return true;
			}
		}
		return false;
	}
	
	public boolean hasAliveEnemy(int newX,int newY) {
		Creature c = homeTown.getObj(newX,newY);
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
	
	public void setLayout(int x, int y){
        setLayoutX(x * Position.HOLDER_LRNGTH + Position.X_START);
        setLayoutY(y * Position.HOLDER_WIDTH + Position.Y_START);
    }

	public boolean isAlive() {
		return alive;
	}

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
		//System.out.println(name+" : thread in moveTo:"+newX+","+newY);
		if(outOfBound(newX,newY) || hasAliveCompanion(newX,newY)) {
			try {
                battle.getBufferedWriter().write(String.format("(%d,%d)->(%d,%d)%n", pos.getX(), pos.getY(), pos.getX(), pos.getY()));
            }catch (IOException e){
                System.err.println("error while recording");
            }
			return;
		}
		if(hasAliveEnemy(newX,newY)) {//若碰上了敌人，则战斗
			fight(newX,newY);
		}
		else if(homeTown.isEmpty(newX, newY)) {//若是空位，则走过去
			stepInto(newX, newY);
		}

	}

	
	public void fight(int newX,int newY) {
		Creature enemy=homeTown.getObj(newX,newY);
		Random rnd = new Random();
		int code=rnd.nextInt(100);
		 
		if(code>50) {
			enemy.die();
			stepInto(newX, newY);
		}
		else {
			this.die();
			enemy.stepInto(pos.getX(),pos.getY());
		}
	}
	
	public void stepInto(int newX,int newY) {
		//System.out.println(name+" : thread ("+pos.getX()+","+pos.getY()+") -> ("+newX+","+newY);
		homeTown.removeObj(pos.getX(),pos.getY());
		pos=homeTown.getPosition(newX,newY);
		homeTown.setObj(newX, newY,this);
		try {
            battle.getBufferedWriter().write(String.format("(%d,%d)->(%d,%d)%n", pos.getX(), pos.getY(), newX, newY));
        }catch (IOException e){
            System.err.println("error while recording");
        }
	}
	
	public void die() {
		//清除自己位置上的生物，放一个墓碑
		homeTown.removeObj(pos.getX(),pos.getY());
		alive=false;
		setImage();
		try {
            battle.getBufferedWriter().write(String.format("(%d,%d)->die%n", pos.getX(), pos.getY()));
        }catch (IOException e){
            System.err.println("error while recording");
        }
	}

	public Position getPos() {
		return pos;
	}
	
}
