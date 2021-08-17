package renderer.builder;

import renderer.shapes.BlockSquare;
import renderer.shapes.MySquare;

public class utils {
	public static boolean isCossilison(MySquare RectA, MySquare RectB) {
		if (RectA.getX() < RectB.getX()+RectB.getW() && RectA.getX()+RectA.getW() > RectB.getX() && RectA.getY() < RectB.getY() + RectB.getH() && RectA.getY() + RectA.getH() > RectB.getY()) {
			return true;
		}
		return false;
	}

	public static boolean isCossilison(MySquare RectA, MySquare RectB, double xOffset, double yOffset) {
		if (RectA.getX()+xOffset < RectB.getX()+RectB.getW() && RectA.getX()+xOffset+RectA.getW() > RectB.getX() && RectA.getY()+yOffset < RectB.getY() + RectB.getH() && RectA.getY()+yOffset + RectA.getH() > RectB.getY()) {
			return true;
		}
		return false;
	}
	
	
	public static boolean isPointOnRectCollision(int x, int y, MySquare square) {
		
		if(x >= square.getX() &&
			x <= square.getX() + square.getW() &&
			y >= square.getY() &&
			y <= square.getY() + square.getH()) {
			return true;
		}else {
			return false;
		}
		
	}
	
public static boolean isPointOnRectCollisionBlock(int x, int y, BlockSquare square) {
		
		if(x >= square.getX() &&
			x <= square.getX() + square.getW() &&
			y >= square.getY() &&
			y <= square.getY() + square.getH()) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public static double perlin(int x, int scale) {
		double perl = (0.2*(-1*Math.sin(-x)+0.8*Math.sin(1.2*0.8*x)+1.2*Math.sin(0.3*Math.PI)))*scale;
		System.out.println(perl);
		return perl;
	}
	
	public static boolean booleanByProba(double proba) {
		if(Math.random() < proba) {
			return true;
		}
		return false;
	}
	
	public static int randomRange(int a, int b) {
		if(a <= b) {
			return (int)(Math.random()*(b-a)+a);
		}else {
			return (int)(Math.random()*(a-b)+b);
		}
	}

	public static boolean isCossilisonSquareBlock(MySquare RectA, BlockSquare RectB, double xOffset, double yOffset) {
		if (RectA.getX()+xOffset < RectB.getX()+RectB.getW() && RectA.getX()+xOffset+RectA.getW() > RectB.getX() && RectA.getY()+yOffset < RectB.getY() + RectB.getH() && RectA.getY()+yOffset + RectA.getH() > RectB.getY()) {
			return true;
		}
		return false;
	}


	public static boolean isCossilison(MySquare RectA, BlockSquare RectB) {
		if (RectA.getX() < RectB.getX()+RectB.getW() && RectA.getX()+RectA.getW() > RectB.getX() && RectA.getY() < RectB.getY() + RectB.getH() && RectA.getY() + RectA.getH() > RectB.getY()) {
			return true;
		}
		return false;
	}


}
