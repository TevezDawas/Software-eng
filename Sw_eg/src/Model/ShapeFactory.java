package Model;

public class ShapeFactory {
	
	public Shape getShape(String diff) {
		
		if(diff.equals("1")) return new EasyShape();
		else if(diff.equals("2")) return new MediumShape();
		else if(diff.equals("3")) return new HardShape();
		
		return null;
	}

}
