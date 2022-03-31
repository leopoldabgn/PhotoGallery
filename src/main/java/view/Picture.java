package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import model.PicTransferHandler;
import model.Propreties;

public class Picture extends JPanel implements MouseListener
{
	private static final long serialVersionUID = 1L;
	
	private File file;
	private JPopupMenu popMenu;
	private Color focusColor, selectColor, pressColor;
	private boolean mouseFocus, mouseSelected, mouseIsPressed, temp;
	private JMenuItem propItem, copyTo, moveTo;
	public static Picture selectedImg;
	private Window win;
	private int type = 0, size;
	private Propreties prop;
	private Image img;
	
	public Picture(Window win, File file, int size, int type)
	{
		super();
		this.prop = new Propreties(file.getAbsolutePath());
		this.file = file;
		this.win = win;
		this.size = size;
		this.type = type;
		this.focusColor = new Color(138, 230, 255, 127);
		this.pressColor = new Color(100, 201, 255, 127);
		this.selectColor = new Color(0, 201, 230, 127);
		initPopMenu();
		this.setPreferredSize(new Dimension(size,size));
		this.addMouseListener(this);
		this.setTransferHandler(new PicTransferHandler());
		this.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				if(!temp)
				{
				    JComponent pic = (JComponent)e.getSource();
				    //Du composant, on recupere l'objet de transfert : le notre
				    TransferHandler handle = pic.getTransferHandler();
				    //On lui ordonne d'amorcer la procedure de drag'n drop
			        handle.exportAsDrag(pic, e, TransferHandler.COPY);
			        temp = true;
				}
			}
		});
	}
	
	public Picture(Window win, File file, int size)
	{
		super();
		
		this.prop = new Propreties(file.getAbsolutePath());
		this.file = file;
		this.win = win;
		this.size = size;
		this.type = 0;
		this.focusColor = new Color(138, 230, 255, 127);
		this.pressColor = new Color(100, 201, 255, 127);
		this.selectColor = new Color(0, 201, 230, 127);
		
		initPopMenu();
		
		this.setPreferredSize(new Dimension(size,size));
		this.addMouseListener(this);
		this.setTransferHandler(new PicTransferHandler());
		this.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				if(!temp)
				{
				    JComponent pic = (JComponent)e.getSource();
				    //Du composant, on r�cup�re l'objet de transfert : le n�tre
				    TransferHandler handle = pic.getTransferHandler();
				    //On lui ordonne d'amorcer la proc�dure de drag'n drop
			        handle.exportAsDrag(pic, e, TransferHandler.COPY);
			        temp = true;
				}
			}
		});
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//if(img == null)
		//{
			if(type == 2)
				img = new ImageIcon(PictureViewer.ICONS_FOLDER+"image.png").getImage();
			else
				img = new ImageIcon(file.getAbsolutePath()).getImage();
		//}
		
		if(type == 0)
		{
			this.setPreferredSize(new Dimension(size,size));
			Dimension dim = resize(img, this.getWidth(), this.getHeight());
			if(img != null)
			{
				g.drawImage(img, (this.getWidth()/2)-((int)dim.getWidth()/2), 
						(this.getHeight()/2)-((int)dim.getHeight()/2), 
						(int)dim.getWidth(), (int)dim.getHeight(), null);
			}
		}
		else
		{
			if(win.getPicPanel() != null && win.getPicPanel().getActualPicContainer() != null)
				this.setPreferredSize(new Dimension(win.getPicPanel().getActualPicContainer().getWidth(), 64));
			else
				this.setPreferredSize(new Dimension(300, 64));
			int x = 5, w = this.getHeight() - this.getHeight()/4, h = this.getHeight()/2;
			if(img != null || type == 2 && img != null)
			{
				Dimension dim = resize(img, w, h);
				g.drawImage(img, x+(w/2-((int)dim.getWidth()/2)), h-((int)dim.getHeight()/2), (int)dim.getWidth(), (int)dim.getHeight(), null);	
				x += w+7;
				Propreties p = new Propreties(this.getPath());
				String name = p.getName();
				name = name.equals(truncate(name, 170)) ? name : truncate(name, 170)+"...";
				g.drawString(name, x, h);
				x += 200;
				g.drawString(p.lastModifiedTime(), x, h);
				x += 150;
				g.drawString("fichier "+p.getExtension(), x, h);
				x += 150;
				g.drawString(p.binSize(), x, h);
			}
		}
		
		if(mouseFocus || mouseSelected || mouseIsPressed)
		{
			if(mouseIsPressed)
				g.setColor(pressColor);
			else if(mouseFocus && !mouseSelected)
				g.setColor(focusColor);
			else if(mouseSelected)
				g.setColor(selectColor);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			/*g.setColor(Color.BLACK);
			g.drawString(file.getName(), 0, this.getHeight()-10);*/
		}
		
	}

	public void initPopMenu()
	{
		popMenu = new JPopupMenu();
		propItem = new JMenuItem("Propreties");
		copyTo = new JMenuItem("Copy to...");
	  	moveTo = new JMenuItem("Move to...");
	  	
		popMenu.add(copyTo);
		popMenu.add(moveTo);
		popMenu.add(propItem);
		
		Picture self = this;
		
		copyTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = PicTree.getFolderPath("Select a directory", false);
				copyImg(self, path);
			}
		});
		
		moveTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = PicTree.getFolderPath("Select a directory", false);
				moveImg(self, path);
			}
		});
		
		propItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PropretiesFrame(getFile());
			}
		});
	}
	
	public static String truncate(String str, int maxWidth)
	{
		String s = "";
		for(int i=0;str.length() > i && (new JLabel(s+str.charAt(i))).getPreferredSize().getWidth() < maxWidth; i++)
			s += str.charAt(i);
		
		return s;
	}
	
	public static Dimension resize(Image pic, int w_max, int h_max)
	{
		int w, h;
		if(pic.getWidth(null) > w_max)
			w = w_max;
		else
			w = pic.getWidth(null);
		
		h = pic.getHeight(null);
		float coeff = (float)w / (float)pic.getWidth(null);
		h *= coeff;
		
		if(h > h_max)
		{
			coeff = (float)h_max / (float)h;
			h = h_max;
			w *= coeff;
		}	
		return new Dimension(w, h);
	}
	
	public boolean exists()
	{
		return this.getFile().exists();
	}
	
	public static void setSelectedImg(Picture pic)
	{
		if(selectedImg != null)
			selectedImg.setState(false);
		if(pic != null)
			pic.setState(true);
		selectedImg = pic;
	}
	
	public static Picture getSelectedImg()
	{
		return selectedImg;
	}
	
	public String delete()
	{
		this.getFile().delete();
		System.out.println(this.getPath()+" has been deleted");
		return this.getPath();
	}
	
	public void setFile(File file)
	{
		this.file = file;
	}
	
	public File getFile()
	{
		return file;
	}
	
	public String getPath()
	{
		return file.getAbsolutePath();
	}
	
	public String getName()
	{
		return file.getName();
	}
	
	@Override
	public void mousePressed(MouseEvent e) 
	{
		temp = false;
		if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
		{
			new PictureViewer(win.getPicPanel().getActualPicContainer(), this.getFile());
			return;
		}
		mouseIsPressed = true;
		repaint();
		setSelectedImg(this);
		win.requestFocus();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		mouseIsPressed = false;
		mouseSelected = true;
		if(selectedImg != null && selectedImg != this)
		{
			selectedImg.setState(false);
			selectedImg.repaint();
		}
		selectedImg = this;
		repaint();	
		System.out.println(this.getName());
		win.requestFocus();
		if(e.getButton() == MouseEvent.BUTTON3)
			popMenu.show(win.getPicPanel().getActualPicContainer(), getX()+e.getX(), getY()+e.getY());
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
	
	public void setState(boolean state)
	{
		mouseIsPressed = false;
		mouseSelected = state;
		this.repaint();
	}
	
	// This class is used to hold an image while on the clipboard.
	static class ImageSelection implements Transferable
	{
	  private Image image;

	  public ImageSelection(Image image)
	  {
	    this.image = image;
	  }

	  // Returns supported flavors
	  public DataFlavor[] getTransferDataFlavors()
	  {
	    return new DataFlavor[] { DataFlavor.imageFlavor };
	  }

	  // Returns true if flavor is supported
	  public boolean isDataFlavorSupported(DataFlavor flavor)
	  {
	    return DataFlavor.imageFlavor.equals(flavor);
	  }

	  // Returns image
	  public Object getTransferData(DataFlavor flavor)
	      throws UnsupportedFlavorException, IOException
	  {
	    if (!DataFlavor.imageFlavor.equals(flavor))
	    {
	      throw new UnsupportedFlavorException(flavor);
	    }
	    return image;
	  }
	}
	
	public static void setClipboard(Image image)
	{
		ImageSelection img = new ImageSelection(image);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(img, null);
	}

	public int getContainerSize() {
		return size;
	}
	
	public void setContainerSize(int size)
	{
		if(size > 0)
		{
			this.size = size;
			this.setSize(new Dimension(size,size));
			this.repaint();
		}
		
	}
	
	public Image getImg()
	{
		return new ImageIcon(this.getPath()).getImage();
	}
	
	public static String getExtension(File f)
	{
		if(f == null || !f.exists())
			return null;
		
		return (f.getName().substring(f.getName().lastIndexOf(".")+1)).toUpperCase();
	}
	
	public void setSelected(boolean bool)
	{
		this.mouseSelected = bool;
		repaint();
	}
	
	public static String copyImg(Picture pic, String path)
	{
		try
		{
	        BufferedImage inputImage = ImageIO.read(pic.getFile());
	        String extension = PicContainer.getFileExtension(pic.getFile());
	        String outputImagePath = path + "/" + pic.getName();
	        int width = pic.getImg().getWidth(null);
	        int height = pic.getImg().getHeight(null);
	        BufferedImage outputImage = new BufferedImage(width,
	                height, inputImage.getType());
	 
	        Graphics2D g2d = outputImage.createGraphics();
	        g2d.drawImage(inputImage, 0, 0, width, height, null);
	        g2d.dispose();

	        ImageIO.write(outputImage, extension, new File(outputImagePath));
	        return outputImagePath;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static int moveImg(Picture pic, String path)
	{
		String outputPath = copyImg(pic, path);
		if(outputPath == null)
			return 1;
		pic.delete();
		pic.setFile(new File(outputPath));
		
		return 0;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	public Propreties getPropreties()
	{
		return this.prop;
	}
	
}