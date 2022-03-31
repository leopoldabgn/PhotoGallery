package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Thumbnail extends JPanel
{
	private static final long serialVersionUID = 1L;

	private String title;
	private BufferedImage img;
	private Color focusColor, selectColor, pressColor;
	private boolean mouseSelected, mousePressed, mouseFocus;
	private int tSpace;
	private Dimension imgDim;
	private FilterPanel pnl;
	public static Thumbnail selectedThumbnail;
	
	public Thumbnail(String title, BufferedImage img)
	{
		super();
		this.title = title;
		this.img = img;
		this.tSpace = 48;
		int size = 100;
		this.imgDim = Picture.resize(img, size, size);
		this.setPreferredSize(new Dimension(size, (int)imgDim.getHeight()+tSpace));
		setup();
	}
	
	public Thumbnail(FilterPanel pnl, String title, BufferedImage img, int size)
	{
		super();
		this.pnl = pnl;
		this.title = title;
		this.img = img;
		this.tSpace = 20;
		this.imgDim = Picture.resize(img, size, size);
		this.setPreferredSize(new Dimension(size, (int)imgDim.getHeight()+tSpace));
		setup();
	}
	
	public Thumbnail(String title, BufferedImage img, int size)
	{
		super();
		this.title = title;
		this.img = img;
		this.tSpace = 20;
		this.imgDim = Picture.resize(img, size, size);
		this.setPreferredSize(new Dimension(size, (int)imgDim.getHeight()+tSpace));
		setup();
	}
	
	public Thumbnail(String title, Image img, int size)
	{
		super();
		this.title = title;
		this.img = toBufferedImg(img);
		this.tSpace = 20;
		this.imgDim = Picture.resize(img, size, size);
		this.setPreferredSize(new Dimension(size, (int)imgDim.getHeight()+tSpace));
		setup();
	}

	public void setup()
	{
		Thumbnail self = this;
		this.focusColor = new Color(138, 230, 255, 127);
		this.pressColor = new Color(100, 201, 255, 127);
		this.selectColor = new Color(0, 201, 230, 127);
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) 
			{
				if(e.getClickCount() == 1)
				{
					mousePressed = true;
					repaint();
				}
				else if(e.getClickCount() == 2)
				{

				}
			}

			@Override
			public void mouseReleased(MouseEvent e) 
			{
				mousePressed = false;
				mouseSelected = true;
				if(selectedThumbnail != null)
					selectedThumbnail.setState(false);
				selectedThumbnail = self;
				
				if(pnl != null)
					pnl.refreshPV();
			
				repaint();
			}
			
			@Override
			public void mouseEntered(MouseEvent e) 
			{
				mouseFocus = true;
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) 
			{
				mouseFocus = false;
				repaint();
			}
		});
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		g.drawImage(img, (int)(this.getWidth() - imgDim.getWidth())/2, (int)(this.getHeight() - imgDim.getHeight())/2, 
					(int)imgDim.getWidth(), (int)imgDim.getHeight()-tSpace, null);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		g.drawString(title, 0, this.getHeight()-tSpace+16);
		
		if(mouseFocus || mouseSelected || mousePressed)
		{
			if(mousePressed)
				g.setColor(pressColor);
			else if(mouseFocus && !mouseSelected)
				g.setColor(focusColor);
			else if(mouseSelected)
				g.setColor(selectColor);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		
	}
	
	public static BufferedImage toBufferedImg(Image img)
	{
		BufferedImage im = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics g = im.getGraphics();
		g.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);
		g.dispose();
		
		return im;
	}
	
	public void setState(boolean state)
	{
		mousePressed = false;
		mouseSelected = state;
		this.repaint();
	}
	
	public BufferedImage getImg() 
	{
		return img;
	}

	public void setImg(BufferedImage img) 
	{
		this.img = img;
	}

	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}
	
}
