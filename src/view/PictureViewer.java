package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PictureViewer extends JFrame implements KeyListener
{
	private static final long serialVersionUID = 1L;
	
	private PicContainer pC;
	private Panel pan;
	private File picture;
	private JSplitPane filterSplit;
	private FilterPanel fP;
	private JToolBar toolBar;
	private JTextField zoomArea;
	private boolean filterMode = false;
	
	public PictureViewer(PicContainer pC, File picture)
	{
		super();
		this.pC = pC;
		this.picture = picture;
		this.toolBar = createToolBar();
		this.pan = new Panel(); // l'ordre est important. Il faut que picture soit defini.
		if(PicContainer.isImg(picture))
			this.setTitle(picture.getName());
		else
			this.setTitle("Photo Viewer");
		this.setMinimumSize(new Dimension(800, 600));
		this.setSize(this.getToolkit().getScreenSize().getSize());
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		
		setDefaultLookAndFeelDecorated(true);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setBackground(new Color(50, 58, 89));
		
		repaint();

		this.getContentPane().add(toolBar, BorderLayout.NORTH);
		this.getContentPane().add(pan, BorderLayout.CENTER);
		this.setVisible(true);
		this.addKeyListener(this);
		this.requestFocus();
		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
					quit(0);
				}
		});
	}

	public JToolBar createToolBar()
	{
		JToolBar toolBar = new JToolBar();
		toolBar.add(actCrop).setHideActionText(true);
		toolBar.add(actFilter).setHideActionText(true);
		toolBar.add(actPaint).setHideActionText(true);
		
		
		// FAIRE UN setSize() method --> pour ensuite faire toutes les modifications relatifs au zoom ( tels que le JTextArea par exemple pardi !!!)
		
		toolBar.addSeparator();
		
		zoomArea = new JTextField("100%");
		zoomArea.setColumns(4);
		zoomArea.setMaximumSize(zoomArea.getPreferredSize());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JButton b1 = new JButton();
		JButton b2 = new JButton();
		b1.setText("+");
		b2.setText("-");
		b1.setPreferredSize(new Dimension(1002, 12));
		b2.setPreferredSize(new Dimension(12, 12));
		
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pan.increaseZoom(pan.getCoeffSize());
				pan.repaint();
				requestFocus();
			}
		});
		
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pan.decreaseZoom(pan.getCoeffSize());
				pan.repaint();
				requestFocus();
			}
		});
		
		panel.add(b1);
		panel.add(b2);
		toolBar.add(zoomArea);
		toolBar.add(panel);
		toolBar.add(actReset);
		toolBar.add(actDel);
		toolBar.addSeparator();
		
		return toolBar;
	}
	
    private AbstractAction actCrop = new AbstractAction() 
    {  
		private static final long serialVersionUID = 1L;
		{
            putValue(Action.NAME, "Crop");
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("crop.png")));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_K);
            putValue(Action.SHORT_DESCRIPTION, "Crop (CTRL+K)");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_K, KeyEvent.CTRL_DOWN_MASK)); 
        }
        
        @Override public void actionPerformed( ActionEvent e ) 
        {
            System.out.println("Cropping...");
			if(!pan.isInCropMode())
			{
				pan.setCropMode(true);
				pan.repaint();
			}
			requestFocus();
        }
    };
    
    private AbstractAction actFilter= new AbstractAction() 
    {  
		private static final long serialVersionUID = 1L;
		{
            putValue(Action.NAME, "Filter");
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("filter.png")));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F);
            putValue(Action.SHORT_DESCRIPTION, "Filter (CTRL+F)");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK)); 
        }
        
        @Override public void actionPerformed( ActionEvent e ) 
        {
            System.out.println("Filter Mode");
            setFilterMode(!filterMode);
			requestFocus();
        }
    };
    
    private AbstractAction actPaint= new AbstractAction() 
    {  
		private static final long serialVersionUID = 1L;
		{
            putValue(Action.NAME, "Paint");
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("pencil.png")));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
            putValue(Action.SHORT_DESCRIPTION, "Filter (CTRL+E)");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK)); 
        }
        
        @Override public void actionPerformed(ActionEvent e) 
        {
            System.out.println("Paint");
    		try {
    			String sys = (System.getProperty("os.name")).substring(0, 3);
    			Process p = null;
    			if(sys.toUpperCase().equals("WIN"))
    			{
    				p = Runtime.getRuntime().exec("cmd.exe /c paint.jar "+picture);
    			}
    			else if(sys.toUpperCase().equals("LIN"))
    			{
    				p = Runtime.getRuntime().exec("paint.jar "+picture);
    			}
    			if(p != null)
    				p.waitFor();
    		} catch (InterruptedException | IOException e1) {
    			e1.printStackTrace();
    		}
    		requestFocus();
        }
    };
    
    private AbstractAction actDel= new AbstractAction() 
    {  
		private static final long serialVersionUID = 1L;
		{
            putValue(Action.NAME, "Delete");
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("redCross.png")));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_DELETE);
            putValue(Action.SHORT_DESCRIPTION, "Delete (DELETE)");
            putValue(Action.ACCELERATOR_KEY, KeyEvent.VK_DELETE); 
        }
        
        @Override public void actionPerformed(ActionEvent e) 
        {
            System.out.println("Delete");
            deleteImage();
            requestFocus();
        }
    };
    
    private AbstractAction actReset= new AbstractAction() 
    {  
		private static final long serialVersionUID = 1L;
		{
            putValue(Action.NAME, "Reset");
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("reset.png")));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(Action.SHORT_DESCRIPTION, "Reset (R)");
            putValue(Action.ACCELERATOR_KEY, KeyEvent.VK_R); 
        }
        
        @Override public void actionPerformed(ActionEvent e) 
        {
            System.out.println("Reset");
            pan.resetImg();
            pan.repaint();
        }
    };
    
	class Panel extends JPanel
	{
		private static final long serialVersionUID = 1L;

		private BufferedImage img;
		private int[] initial = new int[2], initTemp = new int[2], lastTemp = new int[2];
		private int size = 100, x, y, coeffSize = 20;
		private Dimension dim, lastDim = new Dimension(0, 0);
		private boolean crop = false;
		private ZoneSelector zone;
		
		public Panel()
		{
			super();
			this.setLayout(null);
			this.setBackground(new Color(50, 58, 89));
			this.setSize(new Dimension(1000, 1000));
			setup();
			addMouseListeners();
		}
		
		public void addMouseListeners()
		{
			this.addMouseListener(new MouseAdapter() {
				
				public void mousePressed(MouseEvent e)
				{
					if(e.getClickCount() == 2)
					{
						if(size > 100)
						{
							setImgSize(100);
							lastDim = null;
						}
						else if(size == 100)
						{
							setImgSize(400);
						}
						repaint();
						System.out.println(size);
					}
					initial[0] = x;
					initial[1] = y;
					initTemp[0] = e.getX();
					initTemp[1] = e.getY();
					lastTemp[0] = e.getX();
					lastTemp[1] = e.getY();
				}
				
				public void mouseReleased(MouseEvent e)
				{				
					repaint();
				}
				
			});
			
			this.addMouseMotionListener(new MouseAdapter() {
				
				public void mouseDragged(MouseEvent e)
				{	
					int tempX = (initial[0]-(initTemp[0] - lastTemp[0]));
					int tempY = (initial[1]-(initTemp[1] - lastTemp[1]));
					if((dim.getWidth() - Math.abs(tempX)) > dim.getWidth()/2)
						x = tempX;
					
					if((dim.getHeight() - Math.abs(tempY)) > dim.getHeight()/2)
						y = tempY;
					
					lastTemp[0] = e.getX();
					lastTemp[1] = e.getY();
					repaint();
				}
				
			});
			
			
			this.addMouseWheelListener(new MouseAdapter() {
				
				public void mouseWheelMoved(MouseWheelEvent e) {
	                    
	                    if (e.getWheelRotation() > 0) 
	                    {
	                    	if(size > 100)
	                    		decreaseZoom(coeffSize);
	                    	if(size == 100)
	                    	{
	                    		lastDim = null;
	                    	}
	                    } 
	                    else 
	                    {
	                    	if(size < 1000)
	                    		increaseZoom(coeffSize);
	                    }
	                    
	                    repaint();
	            }
				
			});
		}
		
		public void setup()
		{
			try 
			{
				if(picture != null)
					img = ImageIO.read(picture);
				else
					img = null;
			} 
			catch (IOException e1) {}
			setImgSize(100);
			lastDim = null;
			this.repaint();
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			if(picture == null || img == null)
			{
				g.drawString("No Photo Available", (this.getWidth()/2)-50, this.getHeight()/2);
				return;
			}
			
			dim = Picture.resize(img, this.getWidth(), this.getHeight());
			
			if(crop)
			{					
				setImgSize(100);
				dim = new Dimension(((int)dim.getWidth()*size)/100, ((int)dim.getHeight()*size)/100);
				x = (this.getWidth()/2)-((int)dim.getWidth()/2);
				y = (this.getHeight()/2)-((int)dim.getHeight()/2);
				
				g.drawImage(img, x, y, (int)dim.getWidth(), (int)dim.getHeight(), null);
				g.setColor(new Color(70, 70, 70, 200));
				lastDim = dim;
			}
			else
			{
				dim = new Dimension(((int)dim.getWidth()*size)/100, ((int)dim.getHeight()*size)/100);

				if(size == 100)
				{
					x = (this.getWidth()/2)-((int)dim.getWidth()/2);
					y = (this.getHeight()/2)-((int)dim.getHeight()/2);
				}

				if(lastDim != null && !lastDim.equals(dim))
				{
					x += (lastDim.getWidth()-dim.getWidth())/2;
					y += (lastDim.getHeight()-dim.getHeight())/2;
				}

				g.drawImage(img, x, y, (int)dim.getWidth(), (int)dim.getHeight(), null);
				
				lastDim = dim;
			}
			
		}
		
		public void setImgSize(int s)
		{
			this.size = s;
			zoomArea.setText(size+"%");
		}
		
		public void setCropMode(boolean bool)
		{
			this.crop = bool;
			if(bool)
			{
				zone = new ZoneSelector(this, dim, false);
				this.add(zone);
			}
			else
			{
				zone = null;
				this.removeAll();
			}
		}
		
		public boolean isInCropMode()
		{
			return this.crop;
		}

		public ZoneSelector getZoneSelector()
		{
			return this.zone;
		}
		
		public int getImgX()
		{
			return this.x;
		}
		
		public int getImgY()
		{
			return this.y;
		}
		
		public BufferedImage getImg()
		{
			return img;
		}
		
		public void setImg(BufferedImage img)
		{
			this.img = img;
		}
	
		public Dimension getDim()
		{
			return this.dim;
		}
		
		public int getImgSize()
		{
			return size;
		}
		
		public int getCoeffSize()
		{
			return coeffSize;
		}
		
		public void increaseZoom(int coeff)
		{
			this.setImgSize(size+coeff);
		}
		
		public void decreaseZoom(int coeff)
		{
			this.setImgSize(size-coeff);
		}
		
		public void resetImg()
		{
			try {
				img = ImageIO.read(picture);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void refreshTitle()
	{
		if(PicContainer.isImg(picture))
			this.setTitle(picture.getName());
		else
			this.setTitle("Photo Viewer");
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_DELETE:
			deleteImage();
			break;
		case KeyEvent.VK_ENTER:
			if(pan.isInCropMode())
			{
				if(question("Save", "Save this picture ?"))
				{
					File f = getNameFile(this);
					String s = "";
					if(f != null && !f.isFile())
					{
						s = f.getAbsolutePath();
						if(!s.substring(s.length()-4).toUpperCase().equals(".JPG"))
						{
							s = s+".jpg";
						}
						f = new File(s);
						try {
							ImageIO.write(getSelectionImg(this, pan.getZoneSelector(), pan.getDim(),  pan.getImg(), pan.getImgX(), pan.getImgY()), "jpg", f);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
					}
					else if(f != null && f.isFile())
					{
						if(f.getAbsolutePath().contains("."))
						{
							s = f.getParent()+"\\"+f.getName().substring(0, f.getName().lastIndexOf("."));
						}
						else
						{
							s = f.getAbsolutePath();
						}
						s += "_copy.jpg";
						f = new File(s);
						try {
							ImageIO.write(getSelectionImg(this, pan.getZoneSelector(), pan.getDim(), pan.getImg(), pan.getImgX(), pan.getImgY()), "jpg", f);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
				pan.setCropMode(false);
				pan.repaint();
			}
			break;
		case KeyEvent.VK_ESCAPE:
			if(pan.isInCropMode())
				quit(1);
			else
				quit(0);
			break;
		case KeyEvent.VK_LEFT:
			if(isAModeActive())
				break;
			previousPicture();
			break;
		case KeyEvent.VK_RIGHT:
			if(isAModeActive())
				return;
			nextPicture();
			break;
		default:
			break;
		
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	public static boolean question(String title, String q)
	{
		int option = JOptionPane.showConfirmDialog(null, q, title, 
				 JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				
		return option == JOptionPane.OK_OPTION;
	}
	
	public static File getNameFile(JFrame comp)
	{
		JFileChooser choice = new JFileChooser();
		File f = null;
		FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
			    ".jpg, .jpeg, .png", new String[] {"jpg", "jpeg", "png"});
		choice.setFileFilter(imageFilter);
		choice.setAcceptAllFileFilterUsed(false);
		int var = choice.showSaveDialog(comp);
		if(var==JFileChooser.APPROVE_OPTION)
		{
			f = choice.getSelectedFile();
		}
		
		return f;
	}
	
	public BufferedImage getSelectionImg(JFrame f, ZoneSelector z, Dimension actualDim, Image img, int x, int y)
	{
		double cW = img.getWidth(null)/actualDim.getWidth();
		double cH = img.getHeight(null)/actualDim.getHeight();
		BufferedImage output = new BufferedImage((int)(z.getWidth()*cW), (int)(z.getHeight()*cH), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2D = (Graphics2D)output.getGraphics();
		g2D.drawImage(img, (int)((x-z.getX())*cW), (int)((y-z.getY())*cH), img.getWidth(null), img.getHeight(null), null);
		g2D.dispose();
		
		return output;
	}
	
	public void quit(int i)
	{
		if(pan.isInCropMode())
		{
			if(!question("Quit", "Abandon the selection ?"))
				return;
			pan.setCropMode(false);
			pan.repaint();
		}
		if(i != 1)
			this.setVisible(false);
	}
	
	public boolean isInCropMode()
	{
		return pan.isInCropMode();
	}
	
	public boolean isInFilterMode()
	{
		return filterMode;
	}
	
	public void setFilterMode(boolean bool)
	{
		this.filterMode = bool;
		
		if(bool)
		{			
					
			JPanel panel = new JPanel();
				fP = new FilterPanel(this, this.picture.getAbsolutePath());
			panel.add(fP);
		    filterSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pan, new JScrollPane(panel));
			filterSplit.setDividerLocation(this.getWidth() - fP.getDefaultThumbnailFilterSize() - 35);

			this.getContentPane().remove(pan);
			this.getContentPane().add(filterSplit, BorderLayout.CENTER);
		}
		else
		{
			this.getContentPane().remove(filterSplit);
			this.getContentPane().add(pan, BorderLayout.CENTER);
		}
		
		this.revalidate();
	}
	
	public boolean isAModeActive()
	{
		return (isInFilterMode() || isInCropMode());
	}
	
	public void applyFilter(int n)
	{
		BufferedImage img = pan.getImg();
		if(img == null)
			return;
		switch(n)
		{
		case 0:
			pan.setImg(FilterPanel.toLum(img));
			break;
		case 1:
			pan.setImg(FilterPanel.toBW(img));
			break;
		case 2:
			pan.setImg(FilterPanel.toVerticalSym(img));
			break;
		case 3:
			pan.setImg(FilterPanel.toHorizontalSym(img));
			break;
		}
		pan.repaint();
	}
	
	public File previousPicture()
	{
		File pic = pC.previousFile();
		if(pic != null)
		{
			picture = pic;
			refreshTitle();
			pan.setup();
		}
		System.out.println(pic);
		return pic;
	}
	
	public File nextPicture()
	{
		File pic = pC.nextFile();
		if(pic != null)
		{
			picture = pic;
			refreshTitle();
			pan.setup();
		}
		System.out.println(pic);
		
		return pic;
	}

	public void deleteImage()
	{
		Picture p = Picture.getSelectedImg();
		if(p != null && pC.getWin().deleteImg(picture))
		{
			p.delete();
			if(nextPicture() == null)
				previousPicture();
			pC.refreshContainer();
			if(pC.countImages() == 0)
			{
				picture = null;
				refreshTitle();
				pan.setup();
			}
		} 
	}
	
}
