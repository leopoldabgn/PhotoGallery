package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import view.PicContainer;
import view.PicPanel;
import view.Window;

public class Preset implements Serializable
{
	private static final long serialVersionUID = -8633076393244518190L;
	
	private List<File> rootList;
	private List<File> workPicList = null;
	private File lastPicContainerFolder;
	private boolean lastPicContainerBool, isWorkspaceOpen, onlyDetails;
	private int[] splitLocationBars = new int[2];
	private int sizeSliderVal = -1;
	private File file = new File("preset.ini");
	
	public Preset(List<File> rootList)
	{
		super();
		this.rootList = rootList;
	}
	
	public void savePreset(Window win)
	{
		ObjectOutputStream oos = null;
		List<File> picList = win.getWorkspace().getFileList();
		PicContainer pC = win.getPicPanel().getActualPicContainer();
		if(picList != null)
			this.setWorkPicList(picList);
		if(pC != null)
		{ 
			if(pC.getFolder().exists())
			{
				lastPicContainerFolder = pC.getFolder();
				lastPicContainerBool = pC.getAllFoldersBool();
			}
		}
		
		sizeSliderVal = win.getSizeSlider().getValue();
		isWorkspaceOpen = win.isWorkspaceOpen();
		onlyDetails = win.isOnlyDetailsSelected();
		splitLocationBars = win.getSplitBarLocationTab();
		
		try {
			oos =  new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(this);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
	public void loadPreset(Window win)
	{
		ObjectInputStream ois = null;
		Preset preset;
		if(file.exists())
		{
			try {
				ois =  new ObjectInputStream(new FileInputStream(file)) ;
				preset = (Preset)ois.readObject();
				copyPreset(preset, win);
				ois.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}	
		}
	}

	public List<File> getRootList()
	{
		return rootList;
	}

	private void copyPreset(Preset preset, Window win)
	{
		Window.Workspace workspace = win.getWorkspace();
		PicPanel pP = win.getPicPanel();
		List<File> l = preset.getRootList();
		if(l != null)
		{
			this.rootList.removeAll(rootList);
			for(File f : l)
			{
				this.rootList.add(f);
			}
		}	

		if(workspace != null)
		{
			List<Window.WorkPic> list = workspace.getWorkPicList();
			List<File> list2 = preset.getWorkPicList();
			if(list != null && list2 != null)
			{
				list.removeAll(list);
				for(File f : list2)
				{
					workspace.addToList(f.getAbsolutePath(), true);
				}
			}
		}

		if(pP != null && preset.getLastPicContainerFolder() != null)
		{
			pP.setPicContainer(preset.getLastPicContainerFolder(), preset.getLastPicContainerBool());
			lastPicContainerFolder = preset.getLastPicContainerFolder();
			lastPicContainerBool  = preset.getLastPicContainerBool();
			//System.out.println("////Save PicContainer//// --> "+this.getLastPicContainerFolder());
		}
		if(preset.getSizeSliderVal() > 0 && preset.getSizeSliderVal() <= 100)
		{
			this.sizeSliderVal = preset.getSizeSliderVal();
			win.getSizeSlider().setValue(sizeSliderVal);
		}
	
		isWorkspaceOpen = preset.isWorkspaceOpen();
		if(!isWorkspaceOpen())
			win.closeWorkspace();
		
		onlyDetails = preset.onlyDetails;
		win.setOnlyDetails(onlyDetails);
		
		if(preset.getSplitLocationBars() != null)
		{
			splitLocationBars = preset.getSplitLocationBars();
			win.getSplitPane().setDividerLocation(splitLocationBars[0]);
			win.getSplitPane2().setDividerLocation(splitLocationBars[1]);
		}
		
	}

	public List<File> getWorkPicList() 
	{
		return workPicList;
	}

	public void setWorkPicList(List<File> workPicList) 
	{
		this.workPicList = workPicList;
	}

	public boolean getLastPicContainerBool() {
		return lastPicContainerBool;
	}

	public void setLastPicContainerBool(boolean lastPicContainerBool) {
		this.lastPicContainerBool = lastPicContainerBool;
	}

	public File getLastPicContainerFolder() {
		return lastPicContainerFolder;
	}

	public void setLastPicContainerFolder(File lastPicContainerFolder) {
		this.lastPicContainerFolder = lastPicContainerFolder;
	}

	public int getSizeSliderVal() {
		return sizeSliderVal;
	}

	public void setSizeSliderVal(int sizeSliderVal) {
		this.sizeSliderVal = sizeSliderVal;
	}

	public boolean isWorkspaceOpen() {
		return isWorkspaceOpen;
	}

	public int[] getSplitLocationBars() {
		return splitLocationBars;
	}
	
}
