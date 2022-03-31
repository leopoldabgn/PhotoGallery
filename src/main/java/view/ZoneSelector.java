package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class ZoneSelector extends JPanel
{
	private static final long serialVersionUID = 1L;

	private int[] initialPos = new int[] {0, 0};
	private int[] initialSize = new int[] {0, 0};
	private int k = 4, s1 = 4;
	private String zone;
	private boolean move = true;
	
	public ZoneSelector(JPanel pan, Dimension dim, boolean move)
	{
		super();
		this.move = move;
		this.setOpaque(false);
		this.setBounds(pan.getWidth()/2 - (int)dim.getWidth()/2, pan.getHeight()/2 - (int)dim.getHeight()/2, 
						(int)dim.getWidth(), (int)dim.getHeight());
		
		this.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e)
			{
				if(move)
				{
					initialPos[0] = e.getX();
					initialPos[1] = e.getY();
					initialSize[0] = getWidth();
					initialSize[1] = getHeight();
				}
				zone = getZone(e.getX(), e.getY());
			}
			
		});
		
		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = getX() + (e.getX() - initialPos[0]);
				int y = getY() + (e.getY() - initialPos[1]);
				int w = (e.getX() - initialPos[0]);
				int h = (e.getY() - initialPos[1]);
				
				switch(zone)
				{
				case "TL":
					setBounds(x, y, getWidth()+(-1)*w, getHeight()+(-1)*h);
					break;
				case "TR":
					setBounds(getX(),  y, initialSize[0]+w, getHeight()+(-1)*h);
					break;
				case "BR":
					setBounds(getX(),  getY(), initialSize[0]+w, initialSize[1]+h);
					break;
				case "BL":
					setBounds(x, getY(), getWidth()+(-1)*w, initialSize[1]+h);
					break;
				default:
					if(move)
						setLocation(getX() + (e.getX() - initialPos[0]), getY() + (e.getY() - initialPos[1]));
					break;
				}
			}
		});
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//g.drawImage(img, s1, s1, this.getWidth()-2*s1, this.getHeight()-2*s1, null);
		g.setColor(Color.BLUE);
		g.fillRect(s1, s1, k, this.getHeight()-2*s1);
		g.fillRect(this.getWidth()-k-s1, s1, k, this.getHeight()-2*s1);
		g.fillRect(s1, s1, this.getWidth()-2*s1, k);
		g.fillRect(s1, this.getHeight()-k-s1, this.getWidth()-2*s1, k);
		
		g.fillOval(0, 0, s1+k+s1, s1+k+s1);
		g.fillOval(this.getWidth()-s1*2-k, 0, s1+k+s1, s1+k+s1);
		g.fillOval(this.getWidth()-s1*2-k, this.getHeight()-s1*2-k, s1+k+s1, s1+k+s1);
		g.fillOval(0, this.getHeight()-s1*2-k, s1+k+s1, s1+k+s1);
		
		g.setColor(new Color(47, 47, 132, 140));
		g.fillRect(s1, s1, this.getWidth()-2*s1, this.getHeight()-2*s1);
	}
	
	public String getZone(int x, int y)
	{
		if((x >= 0 && x<= s1*2+k) && (y >= 0 && y<= s1*2+k)) // TOP-LEFT
			return "TL";
		else if((x >= this.getWidth()-s1*2-k && x<= this.getWidth()) // TOP-RIGHT
				&& (y >= 0 && y<= s1*2+k))
			return "TR";
		else if((x >= this.getWidth()-s1*2-k && x<= this.getWidth()) 
				&& (y >= this.getHeight()-s1*2-k && y<= this.getWidth())) // BOTTOM-RIGHT
			return "BR";
		else if((x >= 0 && x<= s1*2+k) 
				&& (y >= this.getHeight()-s1*2-k && y<= this.getHeight())) // BOTTOM-LEFT
			return "BL";
			
		return "";
	}
	
	public boolean isMove() 
	{
		return move;
	}
	
	public boolean checkCoord(int x, int y)
	{
		return ((x >= getX() && x <= getX()+getWidth()) && (y >= getY() && y <= getY()+getHeight()));
	}
	
}
