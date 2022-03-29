package view;
import java.io.File;

import javax.swing.JButton;

public class FolderButton extends JButton
{
	private static final long serialVersionUID = 1L;
	
	private File folder;
	private boolean selected;
	private int index;
	
	public FolderButton(File folder, int index)
	{
		super(folder.getName());
		this.folder = folder;
		this.selected = false;
		this.index = index;
	}
	
	public String getName()
	{
		return folder.getName();
	}
	
	public String getPath()
	{
		return folder.getAbsolutePath();
	}
	
	public void setState(boolean state)
	{
		this.selected = state;
	}
	
	public boolean isSelected()
	{
		return selected;
	}
	
	public int getIndex()
	{
		return index;
	}
	
}
