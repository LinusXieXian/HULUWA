import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.scene.layout.Pane;

public class Land extends Pane{
	public static final int LENGTH=9;	//大地的长
	public static final int WIDTH=8;	//宽
	private Position [][]position;//大地区域
	private Lock landLock = new ReentrantLock();
	
	Land(){
		position=new Position[LENGTH][WIDTH];
		for(int i=0;i<LENGTH;i++)
			for(int j=0;j<WIDTH;j++){
				position[i][j]=new Position(i,j);
				position[i][j].setEmpty(true);
			}
		
	}
	
    public Lock getLock() {
		return landLock;
	}
    
    public Lock getNewLock() {
    	landLock=new ReentrantLock();
		return landLock;
	}
	
	public Position getPosition(int x,int y) {
		return position[x][y];
	}
	
	public boolean isEmpty(int x,int y) {
		return position[x][y].isEmpty();
	}
	
	public Creature getObj(int x, int y){
        return position[x][y].getObj();
    }
	
	public void setObj(int x, int y,Creature obj){
        position[x][y].setObj(obj);
        position[x][y].setEmpty(false);

        obj.setLayout(x, y);

        if(!getChildren().contains(obj)){
            getChildren().add(obj);
        }
    }
	
	public void removeObj(int x, int y){
		position[x][y].removeObj();
    }

    public void removeAllObj(){
        this.getChildren().clear();
        for(int i=0;i<LENGTH;i++){
            for(int j=0;j<WIDTH;j++){
            	position[i][j].removeObj();
            }
        }
    }
    

	
}
