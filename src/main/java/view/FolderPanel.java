package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class FolderPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private List<FolderButton> folderList;
	private JButton addFolder = new JButton("add Folder...");
	private ActionListener fBListener;
	private FolderButton lastFB;
	
	public FolderPanel(Window win, List<FolderButton> folderList)
	{
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.folderList = folderList;
		
		this.add(addFolder);
		
		addFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File f = new File(getFolderPath());
				if(!(f.getName().equals("")))
				{
					folderList.add(new FolderButton(f, folderList.size()));
				
					FolderButton fB = folderList.get(folderList.size()-1);
					fB.setLayout(new BoxLayout(fB, BoxLayout.LINE_AXIS));
					fB.addActionListener(fBListener);
					if(folderList.size() == 1)
					{
						lastFB = fB;
						fB.setState(true);
					}
					add(fB);
					remove(addFolder);
					add(addFolder);
					win.refresh();
				}
			}
		});
		
		fBListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String buttonName = ((FolderButton)e.getSource()).getText();
				FolderButton fButton = getFolderButtonByName(buttonName);
				
				if(lastFB != null)
					lastFB.setState(false);

				fButton.setState(true);
				lastFB = fButton;
				win.refresh();
			}
		};
		
	}
	
	public boolean checkParentFolders(File f, File f2) // Check if f2 path is in f path
	{
		String path = f.getParent();
		
		while(path != null)
		{
			if(f2.getAbsolutePath().equals(path))
				return true;
			path = f.getParent();
		}
		
		
		return false;
	}
	
	public FolderButton getFolderButtonByName(String name)
	{
		for(FolderButton b : folderList)
			if(b.getText().equals(name))
				return b;
		return null;
	}
	
	public String getFolderPath()
	{
		JFileChooser choice = new JFileChooser();
		String path = "";
		int var = choice.showOpenDialog(this);
		if(var==JFileChooser.APPROVE_OPTION)
		{
		   path = choice.getCurrentDirectory().getAbsolutePath();
		}
		
		return path;
	}
	
}
