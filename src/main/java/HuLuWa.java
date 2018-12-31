
public class HuLuWa extends Creature{
	public static final int huluwaNum = 7; //葫芦娃总数
	private int rank;	//葫芦娃排行
	private String name;
	private Color color;
	
	public enum Color{
		red, orange, yellow, green, cyan, blue, purple
	}
	
	public HuLuWa(int rank,Color color,Battle battle){
		super(String.valueOf(rank),battle);
		this.rank=rank;
		this.color=color;
	}

	
	public void howl(int srcPosition,int desPosition)	//改变位置时报告
	{
		this.callOffByRank();		
		System.out.println(srcPosition+"->"+desPosition);
	}
	
	public void callOffByRank()
	{
		switch(this.rank)
		{
		case 1:System.out.println("老大");break;
		case 2:System.out.println("老二");break;
		case 3:System.out.println("老三");break;
		case 4:System.out.println("老四");break;
		case 5:System.out.println("老五");break;
		case 6:System.out.println("老六");break;
		case 7:System.out.println("老七");break;
		default:break;
		}
	}

	public void callOffByColor()
	{
		switch(this.color.ordinal()+1)
		{
		case 1:System.out.println("红色");break;
		case 2:System.out.println("橙色");break;
		case 3:System.out.println("黄色");break;
		case 4:System.out.println("绿色");break;
		case 5:System.out.println("青色");break;
		case 6:System.out.println("蓝色");break;
		case 7:System.out.println("紫色");break;
		default:break;
		}
		
	}

	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Color getColor() {
		return color;
	}


	public void setColor(Color color) {
		this.color = color;
	}
}
