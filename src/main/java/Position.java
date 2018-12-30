
public class Position {
	private int x;
	private int y;
	private boolean isEmpty=true;
	private Creature obj=null; //λ���Ϸ��õ�����
	//����ͼƬÿ������x�ᣩ��123����y�ᣩ��105��ԭ���3��109��ʼ
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
