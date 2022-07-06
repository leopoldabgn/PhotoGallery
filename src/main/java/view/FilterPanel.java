package view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class FilterPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private PictureViewer pV;
	private BufferedImage img;
	private Thumbnail[] filters;
	private int DEFAULT_THUMBNAIL_FILTER_SIZE = (int)((getToolkit().getScreenSize().getWidth()/6 + getToolkit().getScreenSize().getHeight()/4)/2);
	
	public FilterPanel(PictureViewer pV, String imgPath)
	{
		super();
		this.pV = pV;
		try 
		{
			this.img = ImageIO.read(new File(imgPath));
		} 
		catch (IOException e) {e.printStackTrace();}
		
		if(this.img != null)
			setup();
	}
	
	public FilterPanel(String imgPath)
	{
		super();
		try 
		{
			this.img = ImageIO.read(new File(imgPath));
		} 
		catch (IOException e) {e.printStackTrace();}
		
		if(this.img != null)
			setup();
	}
	
	public void setup()
	{
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		int s = DEFAULT_THUMBNAIL_FILTER_SIZE;
		
		filters = new Thumbnail[4];
		filters[0] = new Thumbnail(this, "Luminance", toLum(img), s);
		filters[1] = new Thumbnail(this, "Black and White", toBW(img), s);
		filters[2] = new Thumbnail(this, "Vertical Symmetry", toVerticalSym(img), s);
		filters[3] = new Thumbnail(this, "Horizontal Symmetry", toHorizontalSym(img), s);
		
		for(int i=0;i<filters.length;i++)
			this.add(filters[i]);
	}

	public static int[][] getRGBOfImage(BufferedImage img)
	{
		int w = img.getWidth();
		int h = img.getHeight();
		int[][] rgb = new int[w][h];
		for(int j=0;j<w;j++)
			for(int i=0;i<h;i++)
				rgb[j][i] = img.getRGB(j, i);

		return rgb;
	}
	
	public static int convertToRGB(int a, int r, int g, int b)
	{
		return (a<<24) | (r<<16) | (g<<8) | b;
	}
	
	public static BufferedImage createImg(int[][] rgb)
	{
		BufferedImage img = new BufferedImage(rgb.length, rgb[0].length, BufferedImage.TYPE_INT_RGB);
		
		for(int j=0;j<rgb.length;j++)
			for(int i=0;i<rgb[0].length;i++)
				img.setRGB(j, i, rgb[j][i]);
		
		return img;
	}
	
	public static int[][] LumFilter(int[][] rgb)
	{
		int r, g, b, a, av;
		for(int j=0;j<rgb.length;j++)
		{
			for(int i=0;i<rgb[0].length;i++)
			{
				a = (rgb[j][i] & 0xFF) >> 24;
	            r = (rgb[j][i] & 0xFF0000) >> 16;
	            g = (rgb[j][i] & 0xFF00) >> 8;
	            b = (rgb[j][i] & 0xFF);
				av = (r+g+b)/3;
				rgb[j][i] = convertToRGB(a, av, av, av);
			}
		}
		
		return rgb;
	}
	
	public static int[][] BWFilter(int[][] rgb)
	{
		int r, g, b, a, av;
		for(int j=0;j<rgb.length;j++)
		{
			for(int i=0;i<rgb[0].length;i++)
			{
				a = (rgb[j][i] & 0xFF) >> 24;
	            r = (rgb[j][i] & 0xFF0000) >> 16;
	            g = (rgb[j][i] & 0xFF00) >> 8;
	            b = (rgb[j][i] & 0xFF);
				av = (r+g+b)/3;
				if(av < 127)
					av = 0;
				else
					av = 255;
				rgb[j][i] = convertToRGB(a, av, av, av);
			}
		}
		
		return rgb;
	}
	
	public static int[][] verticalSymFilter(int[][] rgb)
	{
		int temp;
		for(int j=0;j<rgb.length;j++)
		{
			for(int i=0;i<rgb[0].length/2;i++)
			{
				temp = rgb[j][i];
				rgb[j][i] = rgb[j][rgb[0].length-1-i];
				rgb[j][rgb[0].length-1-i] = temp;
			}
		}
		
		return rgb;
	}
	
	public static int[][] horizontalSymFilter(int[][] rgb)
	{
		int temp;
		for(int j=0;j<rgb.length/2;j++)
		{
			for(int i=0;i<rgb[0].length;i++)
			{
				temp = rgb[j][i];
				rgb[j][i] = rgb[rgb.length-1-j][i];
				rgb[rgb.length-1-j][i] = temp;
			}
		}
		
		return rgb;
	}
	
	public static BufferedImage toLum(BufferedImage img)
	{
		return createImg(LumFilter(getRGBOfImage(img)));
	}
	
	public static BufferedImage toBW(BufferedImage img)
	{
		return createImg(BWFilter(getRGBOfImage(img)));
	}
	
	public static BufferedImage toVerticalSym(BufferedImage img)
	{
		return createImg(verticalSymFilter(getRGBOfImage(img)));
	}
	
	public static BufferedImage toHorizontalSym(BufferedImage img)
	{
		return createImg(horizontalSymFilter(getRGBOfImage(img)));
	}
	
	public int getDefaultThumbnailFilterSize()
	{
		return DEFAULT_THUMBNAIL_FILTER_SIZE;
	}
	
	public int getFilterNb(Thumbnail t)
	{
		if(filters == null || t == null)
			return -1;
		
		for(int i=0;i<filters.length;i++)
		{
			if(t.equals(filters[i]))
				return i;
		}
		
		return -1;
	}
	
	public void refreshPV()
	{
		if(pV != null)
		{
			pV.applyFilter(getFilterNb(Thumbnail.selectedThumbnail));
		}
	}
	
}
