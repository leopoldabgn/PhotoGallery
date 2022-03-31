package model;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;

public class Propreties extends File
{
	private static final long serialVersionUID = 1L;

	private BasicFileAttributes attr;
	private String path;
	private Image img;
	
	public Propreties(String path)
	{
		super(path);
		this.path = path;
		Path p = this.toPath();
		try {
			this.attr = Files.readAttributes(p, BasicFileAttributes.class);
		} catch (IOException e) {}

		
	}
	
	public BasicFileAttributes getAttr()
	{
		return attr;
	}
	
	public long byteSize()
	{
		return attr != null ? attr.size() : null;
	}
	
	public String binSize()
	{
		if(attr == null)
			return null;
		double[] t = toSize(byteSize(), 1);
		return truncate(t[0])+getSuffix(1, (int)t[1]);
	}
	
	public static String binSize(long octet)
	{
		double[] t = toSize(octet, 1);
		return truncate(t[0])+getSuffix(1, (int)t[1]);
	}
	
	public String size()
	{
		if(attr == null)
			return null;
		double[] t = toSize(byteSize(), 0);
		return truncate(t[0])+getSuffix(0, (int)t[1]);
	}
	
	public static String size(long octet)
	{
		double[] t = toSize(octet, 0);
		return truncate(t[0])+getSuffix(0, (int)t[1]);
	}
	
	private static double[] toSize(long octet, int type)
	{
		double oct = octet;
		int var = type == 0 ? 1000 : 1024;
		int loop = 0;
		
		while(oct >= var && loop != 3)
		{
			oct /= var;
			loop++;
		}
		
		return new double[] {oct, loop};
	}

	private static String insertChar(String str, char c, int pos)
	{
		if(pos < 0)
			return c+str;
		else if(pos >= str.length()-1)
			return str+c;
		
		return str.substring(0, pos+1)+c+str.substring(pos+1);
	}

	private static String getSuffix(int type, int nb)
	{
		String str = "o";
		switch(nb)
		{
		case 0:
			return "o";
		case 1:
			str = "Ko";
			break;
		case 2:
			str = "Mo";
			break;
		case 3:
			str = "Go";
			break;
		default:
			if(type >= 3)
				str = "Go";
			else
				return "Error: Type";
			break;
		}
		
		return type == 0 ? str : insertChar(str, 'i', 0);
	}
	
	public static String truncate(double nb)
	{
		String str = String.valueOf(nb);
		
		if(str.lastIndexOf(".") <= str.length()-2)
		{
			if(str.lastIndexOf(".")+2 < str.length() && str.charAt(str.lastIndexOf(".")+2) >= '5')
				return str.substring(0, str.lastIndexOf(".")+3);
			else if(str.contains(".0"))
				return ""+(int)nb;
			else
				return str.substring(0, str.lastIndexOf(".")+2);
		}

		return ""+nb;
	}
	
	public String getExtension()
	{
		if(!this.exists())
			return null;
		
		return (this.getName().substring(this.getName().lastIndexOf(".")+1)).toUpperCase();
	}
	
	public Object fileKey()
	{
		return attr != null ? attr.fileKey() : null;
	}
	
	public String lastAccessTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return attr != null ? df.format(attr.lastAccessTime().toMillis()) : null;
	}
	
	public String creationTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return attr != null ? df.format(attr.creationTime().toMillis()) : null;
	}
	
	public String lastModifiedTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return attr != null ? df.format(attr.lastModifiedTime().toMillis()) : null;
	}
	
	public boolean refreshImage()
	{
		img = (new ImageIcon(path)).getImage();
		return img == null ? false : true;
	}
	
	public Image getImg()
	{
		return img != null ? img : (refreshImage() == false ? null : img);
	}
	
	public int getImgWidth()
	{
		if(img == null)
			refreshImage();
		return img != null ? img.getWidth(null) : -1;
	}
	
	public int getImgHeight()
	{
		if(img == null)
			refreshImage();
		return img != null ? img.getHeight(null) : -1;
	}
	
	public Dimension getImgSize()
	{
		if(img == null)
			refreshImage();
		return img != null ? new Dimension(img.getWidth(null), img.getHeight(null)) : null;
	}
	
}
