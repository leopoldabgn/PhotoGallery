package model;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import view.Picture;
import view.Window.Workspace;

public class PicTransferHandler extends TransferHandler
{
	private static final long serialVersionUID = 1L;

	  public boolean canImport(TransferHandler.TransferSupport info) {
		    if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
		      return false;
		    }
		    return true;
		  }

	  public boolean importData(TransferHandler.TransferSupport support){

	    Transferable data = support.getTransferable();
	    String str = "";
	    try {
	      str = (String)data.getTransferData(DataFlavor.stringFlavor);
	    } catch (UnsupportedFlavorException e){
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    Workspace frame = (Workspace)support.getComponent();
	    frame.addToList(str, true);
	    return false;
	  }

	  protected void exportDone(JComponent c, Transferable t, int action){
	    if(action == COPY)
	    {
	    	System.out.println("DONE");
	    }
	  }

	  protected Transferable createTransferable(JComponent c) {
	    return new StringSelection(((Picture)c).getPath());
	  }

	  public int getSourceActions(JComponent c) {
	    return COPY;
	  }   
	}