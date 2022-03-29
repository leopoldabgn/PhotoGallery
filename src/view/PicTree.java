package view;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class PicTree extends JTree implements TreeSelectionListener
{
	private static final long serialVersionUID = 1L;
	
	private Window win;
	private List<File> rootList;
	private List<File> folderList;
	private boolean valChanged = false;
	
	public PicTree(Window win, List<File> rootList, List<File> folderList)
	{
		super(new DefaultMutableTreeNode("All Files"));
		buildTree();
		this.win = win;
		this.folderList = folderList;
		this.rootList = rootList;
		JTree self = this;
		this.addTreeSelectionListener(this);
		this.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if(getLastSelectedPathComponent() != null)
				{
					if(e.getButton() == 1)
					{
						if(e.getClickCount() == 2 && getLastSelectedPathComponent().toString().equals("Add Folder"))
						{
							File f = new File(getFolderPath(self));
		
							if(f != null)
							{
								if(!(f.getName().equals("")))
								{
									///// TEST --> Recuperer date de creation d'une photo.
									/*
								    Path file = Paths.get("C:\\Users\\leopo\\Desktop\\P1050850.JPG");
								    BasicFileAttributes attr;
									try {
										attr = Files.readAttributes(file, BasicFileAttributes.class);
										System.out.println("Date de cr�ation: " + attr.creationTime());
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								    */
									
									rootList.add(f);
									folderList.add(f);
									listRoot(f);
		
									
									/*File[] files = PicContainer.getAllFoldersImg(f);
									for(File fi : files)
										System.out.println(fi.getName());*/
									
									/*remove(addFolder);
									add(addFolder);
									win.refresh();*/
								}
							}
						}
						else if(!getLastSelectedPathComponent().toString().equals("Add Folder"))
						{
							PicPanel pP = win.getPicPanel();
							pP.setPicContainer(getLastSelectedPathComponent().toString(), valChanged, true);
							valChanged = false;
						}
					}
					else if(e.getButton() == 2)
					{
						
					}
				}
			}
		});
	}

	public void buildTree()
	{
		addNode("Add Folder");
	}
    
    public void addNode(DefaultMutableTreeNode nodeToAdd) 
    {
        DefaultTreeModel model = (DefaultTreeModel) this.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getModel().getRoot();
        DefaultMutableTreeNode child = nodeToAdd;
        model.insertNodeInto(child, root, root.getChildCount());
        this.scrollPathToVisible(new TreePath(child.getPath()));
    }
    
    public void addNode(final String nodeToAdd) 
    {
        DefaultTreeModel model = (DefaultTreeModel) this.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getModel().getRoot();
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(nodeToAdd);
        model.insertNodeInto(child, root, root.getChildCount());
        this.scrollPathToVisible(new TreePath(child.getPath()));
    }
	
	public static String getFolderPath(Component comp)
	{
		JFileChooser choice = new JFileChooser();
		String path = "";
		int var = choice.showSaveDialog(comp);
		if(var==JFileChooser.APPROVE_OPTION)
		{
		   path = choice.getCurrentDirectory().getAbsolutePath();
		}
		
		return path;
	}

	public void selectAllFolders(Object r)
	{
		//TreePath tree = new TreePath(this);
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) r;
        System.out.println(root);
    	for (int i = 0; i < root.getChildCount(); i++)
    	{
    		if (root.getChildAt(i).isLeaf())
    		{
    			this.addSelectionPath(new TreePath(root.getChildAt(i)));
    			System.out.println(root.getChildAt(i)+" "+new TreePath(root.getChildAt(i)));
    		}
    		else 
    			selectAllFolders(root.getChildAt(i));
    	}
        
	}
	
	public void getAllPaths()
	{
		for(int i=0;i<this.getRowCount();i++)
		{
			   TreePath path = this.getPathForRow(i);
			   System.out.println(path);
		}
	}
	
	private DefaultMutableTreeNode listFolder(File file, DefaultMutableTreeNode node)
	{

			FileFilter directoryFilter = new FileFilter() {
				public boolean accept(File f) {
					return f.isDirectory();
				}
			};

		      File[] list = file.listFiles(directoryFilter);
		      if(list == null)
		      {
		        return null;
		      }

		      for(File nom : list)
		      {
		    	  if(nom.isDirectory() && PicContainer.isContainingPicture(nom))
		    	  {
			          DefaultMutableTreeNode subNode;
	
			          subNode = new DefaultMutableTreeNode(nom.getName());
			          node.add(this.listFolder(nom, subNode));
	
			          if(subNode != null)
			        	  node.add(subNode);
		    	  }
		        }
		      
		      folderList.add(file);
		      
		      return node;
	}
	
	  private void listRoot(File startFile)
	  {
		    DefaultMutableTreeNode theNode = new DefaultMutableTreeNode(startFile.getName());
		    File[] list = startFile.listFiles();
		    if(list != null && PicContainer.isContainingPicture(startFile))// && !folderList.contains(startFile))
		    {
		    	if(!folderList.contains(startFile))
		    		folderList.add(startFile);
			    for(File nom : list)
			    {
			    	if(nom.isDirectory() && PicContainer.isContainingPicture(nom))
			    	{
				    	DefaultMutableTreeNode node = new DefaultMutableTreeNode(nom.getName());
				        DefaultMutableTreeNode temp = this.listFolder(nom, node);
				        if(temp != null)
				        {
				        	theNode.add(temp);      
				        }
			    	}
			    	if(!folderList.contains(nom))
			    		folderList.add(nom);
			    }
			    addNode(theNode);
			    //this.addSelectionPath(new TreePath(theNode.getPath()));
		    }
	  }
	  
	@Override
	public void valueChanged(TreeSelectionEvent e) 
	{
		if(this.getLastSelectedPathComponent() != null && !this.getLastSelectedPathComponent().toString().equals("Add Folder"))
		{
			valChanged = true;
			win.refresh();
		}
	}

	public static String getDefaultHomePath()
	{
		return new JFileChooser().getFileSystemView().getDefaultDirectory().toPath().toString();
	}
	
	public static String getProjectPath()
	{
		File currentDirFile = new File(".");
		String helper = currentDirFile.getAbsolutePath();
		try
		{
			String currentDir = helper.substring(0, helper.lastIndexOf("."));
			currentDir += "Workspace";
			return currentDir;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public void loadNodes()
	{
		if(rootList.size() > 0)
		{
			for(File f : rootList)
			{
				listRoot(f);
			}
		}
	}
	
	public void reset()
	{
		DefaultTreeModel model = (DefaultTreeModel) this.getModel();
		DefaultMutableTreeNode root  = (DefaultMutableTreeNode) model.getRoot();
		root.removeAllChildren();
		model.reload();
		buildTree();
	}
	
}

