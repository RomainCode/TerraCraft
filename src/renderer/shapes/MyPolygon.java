package renderer.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.io.Serializable;

import renderer.point.MyPoint;
import renderer.world.Camera;

public class MyPolygon{
	
	private MyPoint[] points;
	
	public static Camera camera = new Camera(0,0,0,0);
	
	public MyPolygon(MyPoint... points) {
		this.points = points;
	}
	
	public void render(Graphics g) {
		Polygon poly = new Polygon();
		for(int i = 0; i< this.points.length; i++) {
			//Point p = pointConverter.convertPoint(this.getPoints()[i]);
			poly.addPoint((int)(this.points[i].getX()-camera.getX()), (int)(this.points[i].getY()-camera.getY()));
			
		}
		g.setColor(Color.BLUE);
		g.fillPolygon(poly);
	}
}
