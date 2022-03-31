package view;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;

public class PicPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private List<File> folderList;
	private List<PicContainer> picList = new ArrayList<PicContainer>();
	private PicContainer lastPC;
	private Window win;
	private JPopupMenu popPicMenu = new JPopupMenu(),
			popMenu = new JPopupMenu();
	private JMenu sortBy = new JMenu("Sort by");
	private ButtonGroup sortGroup = new ButtonGroup();
	private JRadioButtonMenuItem name, type, size, lastModifiedTime, creationTime;
	
	public PicPanel(Window win, List<File> folderList)
	{
		super();
		this.folderList = folderList;
		this.win = win;
		initPopMenus();
		this.setLayout(new FlowLayout());
		JPanel me = this;
		this.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3)
				{
					popMenu.show(me, e.getX(), e.getY());
				}
				else
					Picture.setSelectedImg(null);
			}
		});
	}
	
	public void paintComponent(Graphics g)
	{	
		super.paintComponent(g);
		
		PicContainer pC = getActualPicContainer();
		if(pC != null)
		{
			//System.out.println("Actual folder : " + pC.getPath());
			if(lastPC != null && lastPC != pC) // lastPicContainer !
				lastPC.setVisible(false);
			JSplitPane split = win.getSplitPane();
			int w = win.getWidth() - (split.getDividerLocation()+split.getDividerSize());

			pC.setBounds(pC.getX(), 50, w, this.getHeight());
			//pC.setSize(new Dimension(w,this.getHeight()));
			//pC.refreshContainer();
			if(pC.getComponentCount() == 0)
				pC.setVisible(false);
			else
				pC.setVisible(true);
			lastPC = pC;
		}
		else
			if(lastPC != null)
				lastPC.setVisible(false);
		
		File f = getActualFolder();
		if(f != null)
		{
			g.drawString(f.getAbsolutePath(), (getWidth()/2) - 50, 18);
		}
		if(f == null || !PicContainer.checkFolderImg(f))
			g.drawString("No image found", (getWidth()/2)-25, getHeight()/2);
	}
	
	public void initPopMenus()
	{
		// PopupMenu :
		
		popMenu.add(sortBy);
		
		name = new JRadioButtonMenuItem("Name");
		name.setSelected(true);
		lastModifiedTime = new JRadioButtonMenuItem("Last modified time");
		creationTime = new JRadioButtonMenuItem("Creation time");
		type = new JRadioButtonMenuItem("Type");
		size = new JRadioButtonMenuItem("Size");
		
		sortBy.add(name);
		sortBy.add(lastModifiedTime);
		sortBy.add(creationTime);
		sortBy.add(type);
		sortBy.add(size);
		
		sortGroup.add(name);
		sortGroup.add(lastModifiedTime);
		sortGroup.add(creationTime);
		sortGroup.add(type);
		sortGroup.add(size);
		
		// PopPicMenu : 
		List<JMenuItem> l = win.getMenuEditItems();

		popPicMenu.add(l.get(0));
		popPicMenu.add(l.get(1));
	}
	
	public List<PicContainer> getPicContainerList()
	{
		return picList;
	}
	
	public File getFileByName(String name)
	{
		if(name != null)
		{
			for(File f : folderList)
			{
				if(f.getName().equals(name))
					return f;
			}
		}
		
		return null;
	}
	
	public File getActualFolder()
	{
		String folderName = null;
		if(win.getTree().getLastSelectedPathComponent() != null)
			folderName = win.getTree().getLastSelectedPathComponent().toString();
		else
			return win.getPreset().getLastPicContainerFolder(); ///////////////////////// ATTENTION ????
		
		if(folderName != null && !folderName.equals("Add Folder"))
		{
			return getFileByName(folderName);
		}
		return null;
	}
	
	public void removePicContainer(String folderName)
	{
		File folder = getFileByName(folderName);
		PicContainer picC = null;
		if(picList != null && folder != null)
		{
			for(PicContainer pC : picList)
			{
				if(pC.getPath().equals(folder.getAbsolutePath()))
					picC = pC;
			}
		}
		
		if(picC != null)
			picList.remove(picC);
	}
	
	public PicContainer getPicContainerByFolderName(String folderName)
	{
		File folder = getFileByName(folderName);
		if(picList != null && folder != null)
		{
			for(PicContainer pC : picList)
			{
				if(pC.getPath().equals(folder.getAbsolutePath()))
					return pC;
			}
		}
		return null;
	}
	
	public void addToLists(boolean allFolders, File folder)
	{
		if(folder == null)
				folder = getActualFolder();
		if(folder != null)
		{
			if(!folderList.contains(folder))
			{
				System.out.println("Adding --> "+folder.getName());
				folderList.add(folder);
			}

			if(!checkPicContainerList(folder))
			{
				if(PicContainer.checkFolderImg(folder) || allFolders)
				{
					System.out.println(allFolders);
					picList.add(new PicContainer(win, folder, allFolders));
					this.add(picList.get(picList.size()-1));
					this.getComponent(this.getComponentCount()-1).setVisible(false);
				}
			}
		}
	}
	
	public boolean checkPicContainerList(File folder)
	{
		for(PicContainer pC : picList)
		{
			if(pC.getPath().equals(folder.getAbsolutePath()))
				return true;
		}
		return false;
	}
	
	/*public void refreshPicList()
	{
		if(folderList.size() != picList.size())
		{
			int nb = folderList.size() - picList.size();
			for(int i=picList.size();i<nb;i++)
			{
				File folder = folderList.get(i);
				picList.add(new PicContainer(win, folder));
				this.add(picList.get(i));
			}
			this.revalidate();
		}
	}*/
	
	public PicContainer getActualPicContainer()
	{
		if(getActualFolder() != null)
		{
			for(PicContainer pC : picList)
				if(pC.getPath().equals(getActualFolder().getAbsolutePath()))
					return pC;
				
		}
		return null;
	}
	
	public void setPicContainer(File file, boolean bool)
	{
		this.addToLists(bool, file);
		if(bool);
			//selectAllFolders(getLastSelectedPathComponent());
		win.refresh();
	}
	
	public void setPicContainer(String name, boolean valChanged, boolean bool)
	{
		PicContainer pC = this.getPicContainerByFolderName(name);
		bool = true;
		if(valChanged == false)
		{
			if(pC != null)
			{
				if(pC.getAllFoldersBool())
					bool = false;
				else
					bool = true;
			}
		}
		this.removePicContainer(name);
		this.addToLists(bool, null);
		if(bool);
			//selectAllFolders(getLastSelectedPathComponent());
		win.refresh();
	}
	
	public JPopupMenu getPopupPicMenu()
	{
		return popPicMenu;
	}
	
	public void copyFile()
	{
		/// Utiliser dans picture
	}
	
	public void pasteFile()
	{
		// Utiliser dans picture
	}
	
}
