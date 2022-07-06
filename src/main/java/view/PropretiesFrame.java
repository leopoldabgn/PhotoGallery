package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import model.Propreties;

public class PropretiesFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	private PropPan pan;
	private Propreties prop;
	
	public PropretiesFrame(File file)
	{
		super();
		this.prop = new Propreties(file.getAbsolutePath());
		this.pan = new PropPan();
		this.setTitle("Propreties");
		this.setSize(new Dimension(350, 400));
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		setDefaultLookAndFeelDecorated(true);

		this.getContentPane().add(pan, BorderLayout.CENTER);
		this.setVisible(true);
		
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
					setVisible(false);
				}
		});
	}
	
	private class PropPan extends JPanel
	{
		private static final long serialVersionUID = 1L;

		public PropPan()
		{
			super();
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			int coeff=20, x=0, y=coeff;
			
			g.drawString(prop.getName(), x, y);
			g.drawString("File type: "+prop.getExtension(), x, y+coeff);
			g.drawString("File Dimension: "+prop.getImgWidth()+"x"+prop.getImgHeight(), x, y+coeff*2);
			g.drawString("Creation date: "+prop.creationTime(), x, y+coeff*3);
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
		
}
