package client;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import ch.randelshofer.quaqua.util.Methods;


public class MyClient {

	static String IP;
	static int PORT;
	static JFrame frame;
	static SystemTray st=SystemTray.getSystemTray();
	public static void main(String[] args) 
	{
		Properties ini=new Properties();
		try {
			ini.load(new FileInputStream(".\\ipAndport.ini"));
			IP=ini.getProperty("IP");
			PORT=Integer.valueOf(ini.getProperty("PORT"));
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null,"没有服务器ip，port，请在右下角设置","错误",JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		
		try {
			System.setProperty("Quaqua.tabLayoutPolicy", "wrap");
	        if (!System.getProperty("os.name").toLowerCase().startsWith("mac"))
	        {
	            Methods.invokeStatic(JFrame.class,"setDefaultLookAndFeelDecorated", Boolean.TYPE,Boolean.TRUE);

	            Methods.invokeStatic(JDialog.class,"setDefaultLookAndFeelDecorated", Boolean.TYPE,Boolean.TRUE);
	        }
			
				UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
				} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		frame = new ClientFream();
		frame.setVisible(true);
	}

}
