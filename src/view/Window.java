package view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import model.PicTransferHandler;
import model.Preset;
import model.Propreties;

public class Window extends JFrame implements KeyListener
{
	private static final long serialVersionUID = 1L;
	
	private List<File> rootList = new ArrayList<File>();
	private List<File> folderList = new ArrayList<File>();
	private PicTree picTree = new PicTree(this, rootList, folderList);
	private JMenuBar menuBar = new JMenuBar();
	private JMenu file = new JMenu("File"),
			edit = new JMenu("Edit"),
			tools = new JMenu("Tools"),
			windowButton = new JMenu("Window"),
			infos = new JMenu("?");
	private JMenuItem copy = new JMenuItem("Copy"),
			paste = new JMenuItem("Paste"),
			workspaceButton = new JMenuItem("Open WorkSpace..."),
			resetPerspective = new JMenuItem("Reset Perspective"),
			appInfos = new JMenuItem("..."),
			newPreset = new JMenuItem("New Preset..."),
			openPreset = new JMenuItem("Open Preset..."),
			resetPreset = new JMenuItem("Reset Preset");
	private JToolBar toolBar = new JToolBar();
	private JCheckBox onlyDetails = new JCheckBox("only details");
	private JSplitPane split, split2;
	private JScrollPane scrollPane;
	private JPanel fP = new JPanel();
	private PicPanel pP = new PicPanel(this, folderList);
	private JSlider sizeSlider = new JSlider();
	private final int PICTURE_MAX_SIZE = 400,
					  SIZE_SLIDER_DEFAULT_VALUE = 25;
	private Workspace workspace = new Workspace(this);
	private Preset preset = new Preset(rootList);
	private boolean isWorkspaceOpen = true;
	
	public Window(int w, int h)
	{
		super();
		this.setTitle("Photo Gallery");
		this.setMinimumSize(new Dimension(w, h));
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setDefaultLookAndFeelDecorated(true);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		initMenuBar();
		initToolBar();
		scrollPane = new JScrollPane(pP, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(24);
		
		split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, workspace, scrollPane);
		split2.setDividerLocation(h/4);
		
	    split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fP, split2);
		split.setDividerLocation(w/4);
		preset.loadPreset(this); // L'ordre est important
		picTree.loadNodes();
		fP.add(picTree);
		
		this.getContentPane().add(toolBar, BorderLayout.NORTH);
		this.getContentPane().add(split, BorderLayout.CENTER);
		
		Window self = this;
		
		copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Image image = new ImageIcon(Picture.getSelectedImg().getFile().getAbsolutePath()).getImage();
				if(image != null)
					Picture.setClipboard(image);
			}
		});
		
		workspaceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				openWorkspace();
			}
		});
		
		resetPreset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rootList.clear();
				folderList.clear();
				pP.getPicContainerList().clear();
				preset.setLastPicContainerFolder(null);
				sizeSlider.setValue(SIZE_SLIDER_DEFAULT_VALUE);
				picTree.reset();
			}
		});
		
		resetPerspective.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				split.setDividerLocation(self.getWidth()/5);
				split2.setDividerLocation(self.getHeight()/4);
			}
		});
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				preset.savePreset(self);
				System.exit(0);
				}
		});
		
		this.addMouseWheelListener(new MouseAdapter() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(e.getWheelRotation() > 0 && sizeSlider.getValue() < 100)
					sizeSlider.setValue(sizeSlider.getValue()+1);
				else if(e.getWheelRotation() < 0 && sizeSlider.getValue() > 0)
					sizeSlider.setValue(sizeSlider.getValue()-1);
			}
		});
		
		this.setVisible(true);
		this.addKeyListener(this);
		this.setFocusable(true);
	}
	
	public void initMenuBar()
	{
		this.setJMenuBar(menuBar);
		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(tools);
		menuBar.add(windowButton);
		menuBar.add(infos);
		
		file.add(newPreset);
		file.add(openPreset);
		file.add(resetPreset);
		
		edit.add(copy);
		edit.add(paste);
		
		tools.add(workspaceButton);
		
		windowButton.add(resetPerspective);
		
		infos.add(appInfos);
	}
	
	public void initToolBar()
	{
		
	    sizeSlider.setMaximum(100);
	    sizeSlider.setMinimum(0);
	    sizeSlider.setValue(SIZE_SLIDER_DEFAULT_VALUE);
	    sizeSlider.setPaintTicks(true);
	    sizeSlider.setPaintLabels(true);
	    sizeSlider.setMinorTickSpacing(10);
	    sizeSlider.setMajorTickSpacing(20);
	    sizeSlider.addChangeListener(e -> {
	    	changePicsSize();
	    });
	    
	    sizeSlider.setMaximumSize(new Dimension(200, 60));
	    toolBar.setPreferredSize(new Dimension(this.getWidth(), 60));
		toolBar.add(sizeSlider);
		
		toolBar.addSeparator();
		
		onlyDetails.setOpaque(false);
		
		onlyDetails.addChangeListener(e -> {
			changePicsSize();
		});
		
		toolBar.add(onlyDetails);
		
		toolBar.addSeparator();
		
		toolBar.add(new JLabel("Sort by : "));
		
		String[] items = {"Name", "LastModifiedTime", "CreationTime",
						  "Type", "Size"};
		JComboBox<String> sortBy = new JComboBox<String>(items);
		sortBy.setMaximumSize(sortBy.getPreferredSize());
		
		sortBy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pP.getActualPicContainer().sortBy(sortBy.getSelectedIndex());
				pP.getActualPicContainer().refresh();
			}
		});
		
		toolBar.add(sortBy);
	}
	
	public void changePicsSize()
	{
        PicContainer pC = pP.getActualPicContainer();
        if(pC != null)
        {
      	  if(sizeSlider.getValue() <= 15 || onlyDetails.isSelected())
      	  {
      		  pC.refreshPicType(onlyDetails.isSelected() ? 2 : 1);
      	  }
      	  else
      	  {
      		  pC.refreshPicType(0);
		          int newSize = convert(PICTURE_MAX_SIZE, sizeSlider.getValue());
		          pC.setPicturesSize(newSize);
      	  }
	          pC.repaint();
	          refresh();
        }
	}
	
	public int getSizeValue()
	{
		return convert(PICTURE_MAX_SIZE, sizeSlider.getValue());
	}

	public int convert(int size, int coeff) // coeff entre 0 et 100.
	{
		float c;

		if(coeff <= 0)
			coeff = 1;
		else if(coeff > 100)
			coeff = 100;
		
		c = (float)coeff/100;
		size *= c;
		return (int)size;		
	}
	
	public void refresh()
	{
		this.revalidate();
		//fP_child.revalidate();
		pP.repaint();
	}
	
	public PicPanel getPicPanel()
	{
		return pP;
	}
	
	public JTree getTree()
	{
		return picTree;
	}
	
	public JSplitPane getSplitPane()
	{
		return split;
	}
	
	public JSplitPane getSplitPane2()
	{
		return split2;
	}

	public List<JMenuItem> getMenuEditItems()
	{
		List<JMenuItem> l = new ArrayList<JMenuItem>();
		l.add(copy);
		l.add(paste);
		return l;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e)
	{
		Picture pic;
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_ESCAPE:			
			int option = JOptionPane.showConfirmDialog(null, "Leave the application ?", "Leave", 
						 JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						
			if(option == JOptionPane.OK_OPTION)
			{
				preset.savePreset(this);
				for(File f : rootList)
					System.out.println(f);
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
			break;
		case KeyEvent.VK_DELETE:
			pic = Picture.getSelectedImg();
			if(pic != null && deleteImg(pic.getFile()))
			{
				pic.delete();
				pic = null;
				if(pP.getActualPicContainer().nextFile() == null)
				{
					pP.getActualPicContainer().previousFile();
				}
				pP.getActualPicContainer().refreshContainer();
			}
			break;
		case KeyEvent.VK_ENTER:
			pic = Picture.getSelectedImg();
			if(pic != null)
			{
				new PictureViewer(pP.getActualPicContainer(), pic.getFile());
			}
			//rootList.add(new File("C:\\Users\\leopo\\Documents"));
			//preset.savePreset();
			preset.loadPreset(this);									//////////////////////////////////////////////////////////BIZARRE???????///////////////////////////////////////////////////////////////////////////////
			break;
		case KeyEvent.VK_LEFT:
			if(pP.getActualPicContainer() != null)
				pP.getActualPicContainer().previousFile();
			break;
		case KeyEvent.VK_RIGHT:
			if(pP.getActualPicContainer() != null)
				pP.getActualPicContainer().nextFile();
			break;
		case KeyEvent.VK_F5:
			pP.getActualPicContainer().refreshContainer();
			break;
		case KeyEvent.VK_F8:
			workspace.addToList(Picture.getSelectedImg().getPath(), true);
			break;
		case KeyEvent.VK_F9:
			workspace.addToList(Picture.getSelectedImg().getPath(), false);
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	
	public Window getWindow()
	{
		return this;
	}
	
	public class Workspace extends JInternalFrame
	{
		private static final long serialVersionUID = -2956508299505668929L;
		
		private Window win;
		private List<WorkPic> picList = new ArrayList<>();
		private JPanel pan = new JPanel();
		private JScrollPane scrollPan = new JScrollPane(pan);
		private JMenuBar menuBar = new JMenuBar();
		private JMenu tools = new JMenu("Tools");
		private JMenuItem export = new JMenuItem("Export ..."),
						  clear  = new JMenuItem("Clear");
		private String workPath;
		
		public Workspace(Window win)
		{
			super();
			this.setTitle("Workspace");
		    this.setClosable(true);
		    this.setVisible(true);
		    this.win = win;
		    this.workPath = PicTree.getProjectPath();
		    initMenuBar();
		    this.setTransferHandler(new PicTransferHandler());
	        this.addInternalFrameListener(new InternalFrameAdapter(){
	            public void internalFrameClosing(InternalFrameEvent e) {
	            	closeWorkspace();
	            }
	        });
	        pan.setLayout(new WrapLayout());
	        this.add(scrollPan);
		}

		private void initMenuBar()
		{
			this.setJMenuBar(menuBar);
			menuBar.add(tools);
			tools.add(export);
			tools.add(clear);
			export.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
							workPath = PicTree.getFolderPath();
							if(workPath != null)
							{
								if(setWorkspaceFolderAt(workPath) == 0)
									System.out.println("DONE");
								else
									System.out.println("EROR : CAN'T CREATE WORKSPACE FOLDER");
							}
				}
			});
			
			clear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					reset();
					revalidate();
				}
			});
			
		}
		
		public int setWorkspaceFolderAt(String path)
		{
			File folder = new File(path);
			if(folder != null)
			{
				if(folder.isDirectory())
				{
					if(picList.size() > 0)
					{
						for(WorkPic pic : picList)
						{
							if(pic.isCpyOrMove())
								Picture.copyImg(pic.getPicture(), path);
							else
								Picture.moveImg(pic.getPicture(), path);
						}
					}
				}
			}
			return 0;
		}
		
		public Window getWin()
		{
			return this.win;
		}
		
		public void addToList(String str, boolean bool)
		{
			File file = new File(str);
			if(file != null)
			{
				if(file.exists())
				{
					WorkPic pic = new WorkPic(getWindow(), file, 60, bool);
					if(!contains(pic.getFile()))
					{
						picList.add(pic);
						pan.add(pic);
						this.revalidate();
					}
				}
			}
		}
		
		public void addToList(WorkPic pic)
		{
			if(!contains(pic.getFile()))
			{
				picList.add(pic);
				pan.add(pic);
				this.revalidate();
			}		
		}
		
		public boolean contains(File file)
		{
			for(WorkPic pic : picList)
			{
				if(pic.getFile().equals(file))
					return true;
			}
			return false;
		}
	
		public List<WorkPic> getWorkPicList()
		{
			return picList;
		}
		
		public List<File> getFileList()
		{
			List<File> l = new ArrayList<>();
			if(picList.size() > 0)
			{
				for(WorkPic wp : picList)
					l.add(wp.getFile());
				return l;
			}
			return null;
		}
		
		public void reset()
		{
			pan.removeAll();
			if(picList != null)
				picList.clear();
			this.setJMenuBar(menuBar);
		}
		
	}
	
	public class WorkPic extends Picture
	{
		private static final long serialVersionUID = 734422693442519947L;
		
		private boolean cpy_or_move;
		public WorkPic(Window win, File file, int size, boolean bool)
		{
			super(win, file, size);
			this.cpy_or_move = bool;
		}
		
		public Picture getPicture()
		{
			return (Picture)this;
		}
		
		public boolean isCpyOrMove()
		{
			return cpy_or_move;
		}
	}

	public boolean deleteImg(File pic)
	{
		if(pic == null)
			return false;
		
		int option = JOptionPane.showConfirmDialog(null, new InfoPan("Are you sure you want to permanently delete this file ?", pic), "Delete File", 
				 JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(getClass().getClassLoader().getResource("deleteFile.png")));

		return option == JOptionPane.YES_OPTION ? true : false;
	}
	
	public class InfoPan extends JPanel
	{
		private static final long serialVersionUID = 1L;

		private String title = "";
		private File file;
		private Image img;
		
		public InfoPan(File file)
		{
			this.file = file;
			if(file.exists())
			{
				this.img = (new ImageIcon(file.getAbsolutePath())).getImage();
			}
		}
		
		public InfoPan(String title, File file)
		{
			this.title = title;
			this.file = file;
			this.setPreferredSize(new Dimension(350, 150));
			if(file.exists())
			{
				this.img = (new ImageIcon(file.getAbsolutePath())).getImage();
			}
		}
		
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.setFont(new Font(Font.SANS_SERIF, 0, 13));
			int coeff = 10, size=110, x=coeff, y=coeff;
			Propreties prop = new Propreties(file.getAbsolutePath());

			g.drawString(title, x, y);
			y += coeff;
			
			Dimension dim = Picture.resize(img, size, size);
			
			if(img != null)
			{
				g.drawImage(img, x+((size-(int)dim.getWidth())/2), y+((size-(int)dim.getHeight())/2), 
								 (int)dim.getWidth(), (int)dim.getHeight(), null);
			}
			
			x += size + 3;
			y += coeff;
			coeff += 10;
			g.drawString(prop.getName(), x, y);
			g.drawString("File type: "+prop.getExtension(), x, y+coeff);
			g.drawString("File Dimension: "+prop.getImgWidth()+"x"+prop.getImgHeight(), x, y+coeff*2);
			g.drawString("Date created: "+prop.creationTime(), x, y+coeff*3);
			String str = prop.size();
			if(str.length() >= 2 && isNumber(str.charAt(str.length()-2)))
				g.drawString("Size: "+prop.binSize(), x, y+coeff*4);
			else
				g.drawString("Size: "+prop.binSize()+" ("+prop.size()+")", x, y+coeff*4);
		}
		
		public boolean isNumber(char c)
		{
			return c >= '0' && c <= '9';
		}
		
	}
	
	public void openWorkspace()
	{
    	int divLocation = split.getDividerLocation();
    	workspace.setVisible(true);
    	split2.setLeftComponent(workspace);
    	split2.setRightComponent(scrollPane);
    	split2.setDividerLocation(this.getHeight()/4);
    	split.setRightComponent(split2);
    	split.setDividerLocation(divLocation);
    	isWorkspaceOpen = true;
	}
	
	public void closeWorkspace()
	{
    	int divLocation = split.getDividerLocation();
    	split.setRightComponent(scrollPane);
    	split.setDividerLocation(divLocation);
    	isWorkspaceOpen = false;
	}
	
	public Workspace getWorkspace()
	{
		return this.workspace;
	}
	
	public boolean isOnlyDetailsSelected() {
		return onlyDetails.isSelected();
	}
	
	public void setOnlyDetails(boolean selected) {
		onlyDetails.setSelected(selected);
	}
	
	public Preset getPreset()
	{
		return this.preset;
	}
	
	public JSlider getSizeSlider()
	{
		return this.sizeSlider;
	}

	public boolean isWorkspaceOpen() {
		return isWorkspaceOpen;
	}
	
	public int[] getSplitBarLocationTab()
	{
		return new int[] {split.getDividerLocation(), split2.getDividerLocation()};
	}
	
}
