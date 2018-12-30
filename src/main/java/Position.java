
public class Position {
	private int x;
	private int y;
	private boolean isEmpty=true;
	private Creature obj=null; //位置上放置的物体
	//背景图片每个方格（x轴）长123，（y轴）宽105；原点从3，109开始
	public static final int HOLDER_LRNGTH=123;
	public static final int HOLDER_WIDTH=105;
	public static final int X_START=3+3*HOLDER_LRNGTH;
	public static final int Y_START=109;

	public Position(int x,int y) {
		this.x=x;
		this.y=y;
	}


	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public Creature getObj() {
		return obj;
	}

	public void setObj(Creature obj) {
		this.obj = obj;
	}
	
	public void removeObj() {
		obj=null;
		isEmpty=true;
	}

}
