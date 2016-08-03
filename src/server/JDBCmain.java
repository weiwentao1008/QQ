package server;
import static java.lang.System.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

class SwingFrameStart
{
	JFrame jf=new JFrame("QQ Chat����� ��������");
	JPanel jp=new JPanel();
	JTextField[] jtf=new JTextField[3];
	JPasswordField jpf=new JPasswordField();
	String[]str={"Oracle","MySQL","SQL Sever"};
	String []sqlstr={"Oracle����","com.mysql.jdbc.Driver","com.microsoft.sqlserver.jdbc.SQLServerDriver"};
	JButton jb1=new JButton("���Բ�����"),jb2=new JButton("���������");
	String []str2={"����url:","����:","�û���:","����:"};
	JComboBox jc=new JComboBox(str);
	String driver, url, user, pass;
	static Connection conn;
	
	void ReadConf() throws IOException
	{
		try
		{
			FileInputStream fs=new FileInputStream(".\\conf.ini");
			System.setIn(fs);
			Scanner sc=new Scanner(System.in);
				url=sc.nextLine().substring(str2[0].length());
				driver=sc.nextLine().substring(str2[1].length());
				user=sc.nextLine().substring(str2[2].length());
				pass=sc.nextLine().substring(str2[3].length());
				out.println(url+driver+user+pass);
				sc.close();
				fs.close();
				jtf[0].setText(url);
				jtf[1].setText(driver);
				jtf[2].setText(user);
				jpf.setText(pass);
		}
		catch(FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "�����ļ���ʧ�ܣ�������","error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	
	void FirstWindows() throws IOException
	{
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image image=Toolkit.getDefaultToolkit().getImage(".\\ico.gif");
		jf.setIconImage(image);
		jf.setSize(450,350);
		jf.setVisible(true);
		Border border=BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Dimension dm=Toolkit.getDefaultToolkit().getScreenSize();
		jf.setLocation((int)dm.getWidth()/2-450/2, (int)dm.getHeight()/2-350/2);
		{
			Box x=Box.createHorizontalBox();
			x.setBorder(border);
			jc.setMaximumSize(new Dimension(100,30));
			x.add(Box.createHorizontalStrut(110));
			x.add(new JLabel("���淽ʽ��"));
			x.add(jc);
			jf.add(x,BorderLayout.NORTH);
		}
		{
			Box y=Box.createVerticalBox();
			y.setBorder(border);
			for(int i=0;i<4;i++)
			{
				y.add(Box.createVerticalStrut(32));
				y.add(new JLabel(str2[i]));
			}
			jf.add(y,BorderLayout.WEST);
		}
		{
			Box y=Box.createVerticalBox();
			y.setBorder(border);
			Dimension di=new Dimension(350,20); 
			for(int i=0;i<3;i++)
			{
				y.add(Box.createVerticalStrut(30));
				jtf[i]=new JTextField();
				jtf[i].setMaximumSize(di);
				y.add(jtf[i]);
			}
			y.add(Box.createVerticalStrut(30));
			jpf.setMaximumSize(di);
			y.add(jpf);
			jf.add(y,BorderLayout.CENTER);
		}
		{
			Box x=Box.createHorizontalBox();
			x.setBorder(border);
			x.add(Box.createHorizontalStrut(110));
			jb1.addActionListener(new saveAndTextListener());
			x.add(jb1);
			jb2.addActionListener(new GoServerListener());
			x.add(jb2);
			jf.add(x, BorderLayout.SOUTH);
		}
		ReadConf();
	}
	/*���ݿ�����*/
	Connection JDBCConnection(String driver,String url,String user,String pass)
	{
		/*try
		{
		
		}
		catch(Exception e)
		{
			out.println("���ݿ�������ȡʧ��"+e);
		}*/
		try
		{
			Class.forName(driver);
		out.println("���ݿ����ӳɹ�");
		Connection con=DriverManager.getConnection(url, user, pass);
		out.println("���ݿ����ӳɹ�");
		return con;
		}
		catch(Exception e)
		{
			out.println("���ݿ�����ʧ��"+e);
		}
		return null;
		
	}
	
	/*�����뱣�水��������*/
	class saveAndTextListener implements ActionListener
	{
		public void actionPerformed(ActionEvent a)
		{
			/*��������*/
			try{
				FileWriter fw=new FileWriter(".\\conf.ini");
				String temp=(String)jc.getSelectedItem();
				for(int i=0;i<3;i++)
				{
					if(temp==str[i])
					{
						jtf[1].setText(sqlstr[i]);
						break;
					}
				}
					for(int i=0;i<3;i++)
					{
						fw.write(str2[i]);
						fw.write(jtf[i].getText()+"\r\n");
					}
					fw.write(str2[3]);
					fw.write(jpf.getPassword());
					fw.close();
					url=jtf[0].getText();
					driver=jtf[1].getText();
					user=jtf[2].getText();
					pass=String.valueOf(jpf.getPassword());
					if((conn=JDBCConnection(driver,url,user,pass))!=null)
						JOptionPane.showMessageDialog(null,"����&&���ӳɹ�","���",JOptionPane.INFORMATION_MESSAGE);
					else
						JOptionPane.showMessageDialog(null,"����ʧ�ܣ�������","error",JOptionPane.ERROR_MESSAGE);
			}
			catch(Exception e){
				JOptionPane.showMessageDialog(null,"�����ļ�����ʧ��","error",JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	
	/*���������������*/
	class GoServerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent a)
		{
				conn=JDBCConnection(driver,url,user,pass);
			if(conn==null)
			{
				JOptionPane.showMessageDialog(null,"����ʧ�ܣ�������","error",JOptionPane.ERROR_MESSAGE);
				return ;
			}
			jf.dispose();
			SecondWindows();
			
		}
	}
	void SecondWindows()
	{
		jf=new SecondWindows("QQ�����",conn);
	}
	
	
	
	
}
	
	

public class JDBCmain {

	
	public JDBCmain() throws Exception
	{
		SwingFrameStart sf=new SwingFrameStart();
		sf.FirstWindows();

	}

}
