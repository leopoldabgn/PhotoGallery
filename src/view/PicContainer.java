package view;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

public class PicContainer extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private File folder;
	private List<Picture> imgPnl;
	private File[] imgList;
	private Window win;
	private boolean allFolders;
	
	public PicContainer(Window win, File folder, boolean allFolders)
	{
		super();
		this.folder = folder;
		this.win = win;
		this.allFolders = allFolders;
		this.setLayout(new WrapLayout());
		setup();
		//imgPnl = sortBy(imgPnl, 1);
		//refresh();
		
		//int[] arr = new int[] {-8};
		
		//System.out.println(Arrays.toString(arr));
		//System.out.println(Arrays.toString(quickSort(arr)));
		
	}
	
	public void setup()
	{
		setup(true);
	}
	
	public void setup(boolean fullSetup)
	{
		if(fullSetup)
		{
			if(allFolders)
				imgList = getAllFoldersImg(this.folder);
			else
				imgList = getFolderImg(this.folder);
		}
		imgPnl = new ArrayList<Picture>();
		if(imgList != null)
		{
			this.removeAll();
			for(int i=0;i<imgList.length;i++)
			{
				imgPnl.add(new Picture(this.win, imgList[i], win.getSizeValue()));
				this.add(imgPnl.get(i));
			}
		}
	}
	
	public static int[] quickSort(int[] tab)
	{
		if(tab == null || tab.length < 1)
			return null;
		else if(tab.length == 1)
			return tab;
		
		int pivot = tab[tab.length-1];
		
		int var1 = 0;
		
		for(int i=0;i<tab.length-1;i++)
		{
			if(tab[i] < pivot)
				var1++;
		}
		
		int[] smallVal = new int[var1];
		int[] biggerVal = new int[tab.length-1-var1];
		var1 = 0;
		
		for(int i=0;i<tab.length-1;i++)
		{
			if(tab[i] < pivot)
			{
				smallVal[var1] = tab[i];
				var1++;
			}
			else
			{
				biggerVal[i-var1] = tab[i];
			}
		}
		
		int[] lastArray = new int[tab.length];
		
		var1 = 0;
		
		smallVal = quickSort(smallVal);
		biggerVal = quickSort(biggerVal);
		
		if(smallVal != null)
		{
			for(int i=0;i<smallVal.length;i++)
			{
				lastArray[var1] = smallVal[i];
				var1++;
			}
		}
		
		lastArray[var1] = pivot;
		var1++;
		if(biggerVal != null)
		{
			for(int i=0;i<biggerVal.length;i++)
			{
				lastArray[var1] = biggerVal[i];
				var1++;
			}
		}
		
		return lastArray;
	}
	
	public void refresh()
	{
		this.removeAll();
		
		for(int i=0;i<imgPnl.size();i++)
		{
			this.add(imgPnl.get(i));
		}
		this.revalidate();
	}
	
	public void refreshPicType(int type) {
		for(Picture pic : imgPnl)
			pic.setType(type);
	}
	
	public void refreshImgList()
	{
		File[] t = new File[imgPnl.size()];
		
		for(int i=0;i<t.length;i++)
		{
			t[i] = imgPnl.get(i).getFile();
		}
		imgList = t;
	}
	
	public void sortBy(int type)
	{
		sortBy(imgPnl, type);
		refreshImgList();
	}
	
	public static List<Picture> sortBy(List<Picture> list, int type)
	{
		if(list == null || list.size() < 1)
			return new ArrayList<>();
		else if(list.size() == 1)
			return list;
		
		Picture pivot = list.get(list.size()-1);
		String pivotDate = getTimeByType(pivot, type);
		List<Picture> smallVal = new ArrayList<>();
		List<Picture> biggerVal = new ArrayList<>();
		String d1;
		
		for(int i=0;i<list.size()-1;i++)
		{
			if(type >= 1 && type <= 3)
			{
				d1 = getTimeByType(list.get(i), type);
				if(compareDates(d1, pivotDate) < 0)
					smallVal.add(list.get(i));
				else
					biggerVal.add(list.get(i));
			}
		}
		
		List<Picture> lastList = new ArrayList<>();
		
		smallVal = sortBy(smallVal, type);
		biggerVal = sortBy(biggerVal, type);
		if(smallVal.size() > 0)
			for(int i=0;i<smallVal.size();i++)
				lastList.add(smallVal.get(i));
		
		lastList.add(pivot);

		if(biggerVal.size() > 0)
			for(int i=0;i<biggerVal.size();i++)
				lastList.add(biggerVal.get(i));

		return lastList;
	}
	
	public static String getTimeByType(Picture p, int type)
	{
		switch(type)
		{
		case 0:
			break;
		case 1:
			return p.getPropreties().lastModifiedTime();
		case 2:
			return p.getPropreties().creationTime();
		case 3:
			return p.getPropreties().lastAccessTime();
		case 4:
			return p.getPropreties().binSize();
		case 5:
			return p.getPropreties().binSize();
		default:
			break;
		}
		
		return "-1";
	}
	
	/*
	public void sortByLastModifiedTime()
	{
		Propreties p1 = new Propreties(imgPnl.get(0).getPath()), p2;
	
		System.out.println("Avant : \n\n");
		
		for(int j=0;j<imgPnl.size();j++)
		{
			System.out.println((new Propreties(imgPnl.get(j).getPath())).lastModifiedTime());
		}

		for(int j=0;j<imgPnl.size();j++)
		{
			p1 = new Propreties(imgPnl.get(j).getPath());
			for(int i=0;i<imgPnl.size();i++)
			{
				p2 = new Propreties(imgPnl.get(i).getPath());
				if(compareDates(p1.lastModifiedTime(), p2.lastModifiedTime()) > 0)
				{
					swap(imgList, j, i);
					swap(imgPnl,  j, i);
					System.out.println("Swap : "+p1.lastModifiedTime()+" "+p2.lastModifiedTime());
				}
			}
		}
		
		System.out.println("Apr�s :\n\n");
		
		
		
		for(int j=0;j<imgPnl.size();j++)
		{
			System.out.println((new Propreties(imgPnl.get(j).getPath())).lastModifiedTime());
		}
		
		this.removeAll();
		
		for(int i=0;i<imgPnl.size();i++)
		{
			this.add(imgPnl.get(i));
		}
		this.revalidate();
	}
	*/
	public static int compareDates(String d1, String d2)
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date1 = null, date2 = null;
		try {
			date1 = df.parse(d1);
			date2 = df.parse(d2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date1 != null && date2 != null ? date1.compareTo(date2) : -2;
	}
	
	public static int compareHours(String h1, String h2, String sep)
	{
		if(!h1.contains(sep) || !h2.contains(sep))
			return -2;
		
		String[] t1 = h1.split(sep), t2 = h2.split(sep);
		int nb = compareNumbers(toInt(t1[0]), toInt(t2[0]));
		
		return nb == 0 ? compareNumbers(toInt(t1[1]), toInt(t2[1])) : nb;
	}
	 
	public static void swap(File[] tab, int i, int j)
	{
		if((i < 0 || j < 0) || (i >= tab.length || j >= tab.length))
			return;
		
		File temp = tab[i];
		tab[i] = tab[j];
		tab[j] = temp;
	}
	
	public static void swap(List<Picture> l, int i, int j)
	{
		if((i < 0 || j < 0) || (i >= l.size() || j >= l.size()))
			return;
			
		Picture temp = l.get(i);
		l.set(i, l.get(j));
		l.set(j, temp);
	}
	
	public static int toInt(String nb)
	{
		try{
			return Integer.parseInt(nb);
		}catch(Exception e){}
		
		return -1;
	}
	
	public static int compareNumbers(int n1, int n2)
	{
		if(n1 < n2)
			return -1;
		else if(n1 > n2)
			return 1;
		
		return 0;
	}
	
	public static File[] getFolderImg(File folder)
	{
		List<File> lF = new ArrayList<File>();
		File[] files = folder.listFiles();
		
		for(File f : files)
		{
			if(isImg(f))
			{
				lF.add(f);
			}
		}
		
		if(lF.size() == 0)
			return null;

		File[] imgTab = new File[lF.size()];
		
		for(int i=0;i<lF.size();i++)
			imgTab[i] = lF.get(i);
		
		return imgTab;
	}
	
	public static boolean isImg(File f)
	{
		if(f == null)
			return false;
		if(f.isFile())
		{
			String ext;
			ext = f.getName().substring(f.getName().lastIndexOf(".")+1).toUpperCase();
			if(ext.equals("JPG") || ext.equals("PNG")) // || ext.equals("ICO"))
					return true;
		}
		return false;
	}
	
	public static String getFileExtension(File f)
	{
		if(f.isFile())
		{
			String ext = f.getName().substring(f.getName().lastIndexOf(".")+1).toUpperCase();
			return ext;
		}
		return null;
	}
	
	public static List<File> convertTabToList(File[] files)
	{
		List<File> list = new ArrayList<File>();
		for(File f : files)
		{
			list.add(f);
		}
		return list;
	}
	
	public static File[] getAllFoldersImg(File folder)
	{
		List<File> listA = new ArrayList<File>(), listB = new ArrayList<File>();
		File[] files = folder.listFiles(), imgList, pics;
		if(files != null)
		{
			for(File f : files)
			{
				if(f.isDirectory())
				{
					pics = getAllFoldersImg(f);
					if(pics != null)
					{
						listB.addAll(convertTabToList(pics));
					}
				}
				else if(isImg(f))
					listA.add(f);
			}
		}
		
		listA.addAll(listB);
		
		if(listA.size() > 0)
		{
			imgList = new File[listA.size()];
			
			for(int i=0;i<listA.size();i++)
				imgList[i] = listA.get(i);
		
			return imgList;
		}
		
		return null;
	}
	
	public static boolean checkFolderImg(File folder)
	{
		if(folder.isDirectory())
		{
			File[] list = folder.listFiles();
			if(list != null)
			{
				for(File f : list)
				{
					if(f.isFile())
					{
						if(isImg(f))
							return true;
					}
				}
			}
		}
		
		return false;
	}
	

	public static boolean isContainingPicture(File file)
	{
		if(file.isDirectory())
		{
			File[] list = file.listFiles();
			if(list != null)
			{
				for(File f : list)
				{
					if(f.isFile())
					{
						if(isImg(f))
								return true;
					}
					else if(f.isDirectory())
					{
						if(isContainingPicture(f))
							return true;
					}
				}
			}
		}
		
		return false;
	}
	
	
	public String getPath()
	{
		return folder.getAbsolutePath();
	}
	
	public void refreshContainer()
	{
		File[] list;
		
		if(allFolders)
			list = getAllFoldersImg(folder);
		else
			list = getFolderImg(folder);
		
		if(!Arrays.equals(list, imgList)) // if(folderContentIsDifferent)
		{
			imgList = list;
			this.removeAll();
			if(imgList != null)
			{
				imgPnl = new ArrayList<Picture>();
				for(int i=0;i<imgList.length;i++)
				{
					if(imgList[i].getAbsolutePath().equals(Picture.getSelectedImg().getFile().getAbsolutePath()))
					{
						
						imgPnl.add(Picture.getSelectedImg());
					}
					else
						imgPnl.add(new Picture(win, imgList[i], win.getSizeValue()));
					this.add(imgPnl.get(i));
				}
			}
		this.revalidate();
		}
		
	}
	
	public boolean contains(File file)
	{
		if(imgList == null)
			return false;
		for(int i=0;i<imgList.length;i++)
		{
			if(file.equals(imgList[i]))
					return true;
		}
		return false;
	}
	
	public int getIndex(File pic)
	{
		if(imgList == null)
			return -1;
		for(int i=0;i<imgList.length;i++)
		{
			if(imgList[i].equals(pic))
				return i;
		}
		return -1;
	}
	
	public File previousFile()
	{
		File pic = Picture.getSelectedImg().getFile();
		int index;
		
		if(contains(pic))
		{
			index = getIndex(pic);
			if(index > 0)
			{
				if(!imgPnl.get(index-1).getFile().exists())
				{
					refreshContainer();
				}
				else
				{
					Picture.setSelectedImg(imgPnl.get(index-1));
					return imgList[index-1];
				}
			}
		}
		
		return null;
	}
	
	public File nextFile()
	{		
		File pic = Picture.getSelectedImg().getFile();
		int index;
		
		if(contains(pic))
		{
			index = getIndex(pic);
			if(index+1 < imgList.length)
			{
				if(!imgPnl.get(index+1).getFile().exists())
				{
					refreshContainer();
				}
				else
				{
					Picture.setSelectedImg(imgPnl.get(index+1));
					return imgList[index+1];
				}
			}
		}
		
		return null;
	}
 
	/*
	public File getPreviousFile()
	{
		File pic = Picture.getSelectedImg().getFile();
		int index;
		
		if(contains(pic))
		{
			index = imgList.indexOf(pic);
			if(index > 0)
				return imgList.get(index-1).getFile();
		}
		
		return null;
	}
	
	public File getNextFile()
	{
		File pic = Picture.getSelectedImg().getFile();
		int index;
		
		if(imgPnl.contains(pic))
		{
			index = imgPnl.indexOf(pic);
			if(index != imgPnl.size()-1)
				return imgPnl.get(index+1).getFile();
		}
		
		return null;
	}
	*/
	
	public boolean getAllFoldersBool()
	{
		return this.allFolders;
	}
	
	public void setPicturesSize(int size)
	{
		if(imgPnl == null || size <= 0)
			return;
		for(Picture pic : imgPnl)
		{
			pic.setContainerSize(size);
		}
	}

	public Picture getPictureByFile(File f)
	{
		for(int i=0;i<imgPnl.size();i++)
		{
			if(imgPnl.get(i).getFile() == f)
				return imgPnl.get(i);
		}
		return null;
	}
	
	public File getFolder() 
	{
		return folder;
	}
	
	public Window getWin()
	{
		return win;
	}
	
	public int countImages()
	{
		if(imgList == null)
			return 0;
		
		return imgList.length;
	}
	
}
