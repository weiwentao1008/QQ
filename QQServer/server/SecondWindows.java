package server;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Toolkit;

import javax.swing.JTabbedPane;
import javax.swing.JTable;

import java.awt.Component;

import javax.swing.Box;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import static java.lang.System.*;

public class SecondWindows extends JFrame {

	static JTextArea HistoryText;
	private JPanel contentPane;
	final int READ_WRITE=1;
	final int READ_ONLY=2;
	final int RUNING=1;
	final int ALLUSER=0;
	private JTextField userID;
	JTable allUserTable;
	int colNumber;
	JLabel FoundNum;
	static JTextArea textArea;
	JTable UserTable;
	/**
	 * 读取数据库创建用户表格
	 * 
	 */
	private TableModel  ReadSql(Connection con ,String sql,int Model,int UserModel)
	{
		
		TableModel myTable=new DefaultTableModel();
		try {
			colNumber=0;
			Statement st = null;
			if(Model==2)
			 st=con.createStatement(ResultSet.CONCUR_READ_ONLY,ResultSet.TYPE_FORWARD_ONLY);
			if(Model==1)
			 st=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet rs=st.executeQuery(sql);
			   ResultSetMetaData   metaData;
			   metaData = rs. getMetaData();
			   int number=metaData. getColumnCount();
			   Vector columnNames=new Vector<String>();
			   
			   Vector rows=new Vector<String>();
			     for(int num=1;num<=number;num++)
			   {
			    columnNames.addElement(metaData. getColumnLabel(num));
			   }
			  while(rs.next())
			   {
				  colNumber++;
				  if(UserModel==1)
				  {
					  if(rs.getObject(number-2).equals("离线")==true)
						  continue;
				  }
			    Vector newRow = new Vector();
			       
			      for (int i = 1; i <= number; i++)
					{
						newRow.addElement(rs. getObject(i));
			       }
			      rows.addElement(newRow);
			   }
			  
			   myTable=new DefaultTableModel(rows,columnNames);
			   return myTable;
		} catch (SQLException e) {
			
			JOptionPane.showMessageDialog(null, "数据库读取失败","错误",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return null;
		}
	}
	
	/*创建时间数组*/
	 private Vector<Integer> CreateDateVector(int start,int end) {
		 
		 Vector<Integer> date =new Vector<Integer>();
		 for(int i=start;i<=end;i++)
			 date.addElement(i);
		 return date;
	}
	
	
	
	/**
	 * 启动通讯服务按键监听器
	 */
	
	class RefreshThread extends Thread
	{
		public void run()
		{
			while(true)
			{
				UserTable.setModel(ReadSql(SwingFrameStart.conn,"SELECT * FROM qq;",READ_ONLY,RUNING));
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * Create the frame.
	 */
	public SecondWindows(String Title,final Connection con) {
		super.setVisible(true);
		super.setTitle(Title);
		setIconImage(Toolkit.getDefaultToolkit().getImage("ico.gif"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 930, 689);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(100, 10));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		SwingUtilities.updateComponentTreeUI(getContentPane());
		
		
			/*服务器管理*/
		JPanel ServerManagment = new JPanel();
		tabbedPane.addTab("服务器管理", null, ServerManagment, null);
		ServerManagment.setLayout(new BorderLayout());
		
		JPanel runUserList = new JPanel();
		runUserList.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 255), 2), "在线用户列表", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 255)));
		ServerManagment.add(runUserList, BorderLayout.NORTH);
		runUserList.setLayout(new BorderLayout());
		
		UserTable =new JTable(ReadSql(con,"SELECT * FROM qq;",READ_ONLY,RUNING));
		new RefreshThread().start();
		JScrollPane userScrollPane = new JScrollPane(UserTable);
		userScrollPane.setPreferredSize(new Dimension(0,300));
		runUserList.add(userScrollPane, BorderLayout.CENTER);
		JPanel telentMassage = new JPanel();
		telentMassage.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 255), 2), "通讯信息提示", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 255)));
		ServerManagment.add(telentMassage, BorderLayout.CENTER);
		telentMassage.setLayout(new BorderLayout());
		
		
		textArea = new JTextArea(8,20);
		textArea.setEditable(false);
		JScrollPane massageScrollPane = new JScrollPane(textArea);
		telentMassage.add(massageScrollPane);
		
		
		
		JPanel Managment = new JPanel();
		Managment.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 255), 2), "服务器管理", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 255)));
		ServerManagment.add(Managment, BorderLayout.EAST);
		Managment.setLayout(new BorderLayout());
		
		final JLabel earthImage = new JLabel();
		earthImage.setIcon(new ImageIcon("earth_2.jpg"));
		Managment.add(earthImage, BorderLayout.WEST);
		
		Box verticalBox = Box.createVerticalBox();
		Managment.add(verticalBox, BorderLayout.EAST);
		
		final JButton runServer = new JButton("启动通讯服务");
		final JButton stopServer = new JButton("停止通讯服务");
		runServer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				earthImage.setIcon(new ImageIcon("earth.gif"));
				try {
					new AIOServer().startListen();
					SystemSendMessage ssm=  new SystemSendMessage();
					ssm.AdministroterLogin();
					
					runServer.setEnabled(false);
					stopServer.setEnabled(true);
				} catch (Exception e) {
				
					
					e.printStackTrace();
				}
			}
		});
		verticalBox.add(runServer);
		
		Component verticalGlue = Box.createVerticalGlue();
		verticalBox.add(verticalGlue);
		
		
		stopServer.setEnabled(false);
		stopServer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				earthImage.setIcon(new ImageIcon("earth_2.jpg"));
				try {
					AIOServer.stopListen();
					SecondWindows.textArea.append("---通讯服务停止---\n");
					runServer.setEnabled(true);
					stopServer.setEnabled(false);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		});
		verticalBox.add(stopServer);
		
		Component verticalGlue_1 = Box.createVerticalGlue();
		verticalBox.add(verticalGlue_1);
		
		JButton userstop = new JButton("强制用户下线");
		userstop.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
						
				Iterator<String> foreach=AIOServer.channelMap.keySet().iterator();
				while(foreach.hasNext())
				{
					String id=foreach.next();
							Statement st;
							System.out.println("keynum:"+AIOServer.channelMap.keySet().size()+"强制下线用户："+id);
							try
							{
								if(!id.equals(SystemSendMessage.Admin))
								{
								AIOServer.channelMap.get(id).write(ByteBuffer.wrap((MyProtocol.SYSTEM_OFFLINE_MESSAGE+MyProtocol.MESSAGE_END).getBytes(AIOServer.UTF_8))).get();
								st = SwingFrameStart.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
								ResultSet rs=st.executeQuery("SELECT ID,姓名,在线状态  FROM qq WHERE ID="+id+";");
								rs.first();
								String time=AIOServer.getTime();
								SecondWindows.textArea.append(" ["+time+"]  "+rs.getString("姓名")+"( "+id+" ) 下线了\r\n");
								rs.updateString("在线状态","离线");
								rs.updateRow();
								rs.close();
								st.close();
								}
							}
							catch (Exception ex)
							{
								System.out.println("用户强制下线时出错");
								ex.printStackTrace();
							}
						}
						
					}	
		});
		verticalBox.add(userstop);
		
		
		
		JPanel sengInformation = new JPanel();
		sengInformation.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 255), 2), "公告发布", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 255)));
		ServerManagment.add(sengInformation, BorderLayout.SOUTH);
		sengInformation.setLayout(new BorderLayout());
		
		final JTextArea Information = new JTextArea();
		Information.setRows(3);
		
		JScrollPane sendScrollPane = new JScrollPane(Information);
		sengInformation.add(sendScrollPane, BorderLayout.CENTER);
		
		
		
		Box horizontalBox = Box.createHorizontalBox();
		sengInformation.add(horizontalBox, BorderLayout.SOUTH);
		
		JCheckBox CheckQun = new JCheckBox("群公告");
		horizontalBox.add(CheckQun);
		
		JCheckBox CheckSystem = new JCheckBox("系统通知");
		horizontalBox.add(CheckSystem);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue);
		
		JButton send = new JButton("发送");
		Action sendAction=new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				//String content = MyProtocol.LOGIN_MESSAGE+Information.getText()+MyProtocol.PASSWORD_MASSAGE+Information.getText();
				String content = MyProtocol.SYSTEM_MESSAGE+Information.getText();
				if (content.trim().length() > 0)
				{
					try
					{
						SystemSendMessage.clientChannel.write(ByteBuffer.wrap(content.trim().getBytes("utf-8"))).get();    //①
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
				Information.setText("");
			}
		};
		send.addActionListener(sendAction);
		/*发送热键设置*/
		textArea.getInputMap().put(KeyStroke.getKeyStroke('\n', java.awt.event.InputEvent.CTRL_MASK) , "send");
		textArea.getActionMap().put("send", sendAction);
		horizontalBox.add(send);
		/*服务器管理     End*/
		
		
		/*用户管理*/
		JPanel UserManagment = new JPanel();
		tabbedPane.addTab("用户管理", null, UserManagment, null);
		UserManagment.setLayout(new BorderLayout());
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		horizontalBox_1.setBorder(new EmptyBorder(3, 2, 2, 2));
		UserManagment.add(horizontalBox_1, BorderLayout.NORTH);
		
		Component horizontalStrut = Box.createHorizontalStrut(300);
		horizontalBox_1.add(horizontalStrut);
		
		
		horizontalBox_1.add(new JLabel("账号："));
		userID = new JTextField(5);
		horizontalBox_1.add(userID);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		horizontalBox_1.add(horizontalStrut_2);
		
		
		
		JButton Find = new JButton("查询");
		Find.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent e)
				{
					String sqltemp=null;
					String temp=userID.getText();
					if(temp.equals(""))
						sqltemp="SELECT * FROM qq;";
					else
						sqltemp="SELECT * FROM qq WHERE ID="+temp+";";
					out.println(temp);
					allUserTable.setModel(ReadSql(con,sqltemp,READ_WRITE,ALLUSER));
					FoundNum.setText("总记录数："+colNumber);
				}
		});
		horizontalBox_1.add(Find);
		
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		horizontalBox_1.add(horizontalStrut_3);
		
		 FoundNum = new JLabel("总记录数："+colNumber);
		horizontalBox_1.add(FoundNum);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(300);
		horizontalBox_1.add(horizontalStrut_1);
		
		JPanel userInformation = new JPanel();
		userInformation.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 255), 2), "用户信息表", TitledBorder.LEFT, TitledBorder.TOP, null, Color.BLUE));
		UserManagment.add(userInformation, BorderLayout.CENTER);
		
		
		userInformation.setLayout(new BorderLayout());
		allUserTable =new JTable(ReadSql(con,"SELECT * FROM qq;",READ_WRITE,ALLUSER));
		JScrollPane allUserScrollPane = new JScrollPane(allUserTable);
		allUserScrollPane.setPreferredSize(new Dimension(0,300));
		userInformation.add(allUserScrollPane);
		
		Box horizontalBox_2 = Box.createHorizontalBox();
		horizontalBox_2.setBorder(new EmptyBorder(2, 2, 2, 2));
		UserManagment.add(horizontalBox_2, BorderLayout.SOUTH);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		horizontalBox_2.add(horizontalGlue_1);
		
		JButton AddUser = new JButton("添加用户");
		AddUser.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				AddUser au=new AddUser("账号注册",con);
				au.setVisible(true);
			}
			
		});
		horizontalBox_2.add(AddUser);
		
		JButton ModifyUser = new JButton("修改用户");
		ModifyUser.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				int row;
				if((row=allUserTable.getSelectedRow())>-1)
				{
					
					TableModel tm=allUserTable.getModel();
					Object ID=tm.getValueAt(row,0);
					
					try
					{
					Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
					ResultSet rs=st.executeQuery("SELECT * FROM `qq` WHERE ID="+ID+";");
					rs.first();
					for(int i=1;i<tm.getColumnCount();i++)
							rs.updateObject(i+1, tm.getValueAt(row,i));
					rs.updateRow();
					rs.close();
					}
					catch(SQLException e)
					{
						e.printStackTrace();
						JOptionPane.showMessageDialog(null,"数据库读取失败","错误",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		horizontalBox_2.add(ModifyUser);
		
		JButton DeleteUser = new JButton("删除用户");
		DeleteUser.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				
				try {
					Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
					int row;
					TableModel tm=allUserTable.getModel();
					Object ID;
					if((row=allUserTable.getSelectedRow())>-1)
						{
						int temp=JOptionPane.showConfirmDialog(null,"是否删除ID="+tm.getValueAt(row,0));
						if(temp==JOptionPane.OK_OPTION)
							ID=tm.getValueAt(row,0);
						else
							return ;
						}
						else
						ID=JOptionPane.showInputDialog("删除的ID：");
					ResultSet rs=st.executeQuery("SELECT * FROM `qq` WHERE ID="+ID+";");
					rs.first();
					rs.updateString("Model","STOP");
					rs.updateRow();
					CreateLog.InsertLog(rs.getString("姓名"), rs.getInt("ID"),CreateLog.DELETE);
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,"数据库读取失败","错误",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		horizontalBox_2.add(DeleteUser);
		
		JButton ResetPassword = new JButton("重置密码");
		ResetPassword.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				Statement st;
				try {
					st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
					int row;
					TableModel tm=allUserTable.getModel();
					Object ID;
					if((row=allUserTable.getSelectedRow())>-1)
						{
						int temp=JOptionPane.showConfirmDialog(null,"是否重置ID="+tm.getValueAt(row,0)+" 密码");
						if(temp==JOptionPane.OK_OPTION)
							ID=tm.getValueAt(row,0);
						else
							return ;
						}
						else
						ID=JOptionPane.showInputDialog("密码重置ID为：");
					ResultSet rs=st.executeQuery("SELECT * FROM `qq` WHERE ID="+ID+";");
					rs.first();
					String newPassword=JOptionPane.showInputDialog("密码重置为：");
						rs.updateString("密码",newPassword);
						rs.updateRow();
					newPassword=null;
					CreateLog.InsertLog(rs.getString("姓名"),(Integer) ID,CreateLog.CHANGE);
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,"数据库读取失败","错误",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		horizontalBox_2.add(ResetPassword);
		
		JButton ResetAllPassword = new JButton("重置所有密码");
		ResetAllPassword.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				Statement st;
				try {
					st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
					ResultSet rs=st.executeQuery("SELECT * FROM `qq` WHERE Model='RUN' ;");
					String newPassword=JOptionPane.showInputDialog("密码重置为：");
					while(rs.next())
						{
							rs.updateString("密码",newPassword);
							rs.updateRow();
						}
					newPassword=null;
					CreateLog.InsertLog("所有密码重置",00000,CreateLog.CHANGE);
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,"数据库读取失败","错误",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		horizontalBox_2.add(ResetAllPassword);
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		horizontalBox_2.add(horizontalGlue_2);
		/*用户管理End*/
		
		
		/*日志管理*/
		JPanel DateManagment = new JPanel();
		tabbedPane.addTab("日志管理", null, DateManagment, null);
		DateManagment.setLayout(new BorderLayout());
		
		JPanel DateSelect = new JPanel();
		DateSelect.setBorder(new EmptyBorder(2, 2, 2, 2));
		DateManagment.add(DateSelect, BorderLayout.NORTH);
		DateSelect.setLayout(new BorderLayout());
		
		Box horizontalBox_3 = Box.createHorizontalBox();
		DateSelect.add(horizontalBox_3, BorderLayout.CENTER);
		
		Component horizontalStrut_4 = Box.createHorizontalStrut(250);
		horizontalBox_3.add(horizontalStrut_4);
		
		Calendar time=Calendar.getInstance();
		JComboBox<Integer> YearComboBox = new JComboBox<Integer>();
		YearComboBox.setModel(new DefaultComboBoxModel<Integer>(CreateDateVector(1990,time.get(Calendar.YEAR))));
		horizontalBox_3.add(YearComboBox);
		horizontalBox_3.add(new JLabel("年"));
		YearComboBox.setSelectedIndex(time.get(Calendar.YEAR)-1990);
		
		JComboBox<Integer> MonthComboBox = new JComboBox<Integer>();
		MonthComboBox.setModel(new DefaultComboBoxModel<Integer>(CreateDateVector(1,12)));
		horizontalBox_3.add(MonthComboBox);
		horizontalBox_3.add(new JLabel("月"));
		MonthComboBox.setSelectedIndex(time.get(Calendar.MONTH)+1-1);
		
		JComboBox<Integer> DayComboBox = new JComboBox<Integer>();
		DayComboBox.setModel(new DefaultComboBoxModel<Integer>(CreateDateVector(1,31)));
		horizontalBox_3.add(DayComboBox);
		horizontalBox_3.add(new JLabel("日"));
		DayComboBox.setSelectedIndex(time.get(Calendar.DAY_OF_MONTH)-1);
		
		Component horizontalStrut_6 = Box.createHorizontalStrut(20);
		horizontalBox_3.add(horizontalStrut_6);
		horizontalBox_3.add(new JLabel("关键字："));
		JTextField KeyText=new JTextField();
		horizontalBox_3.add(KeyText);
		
		Component horizontalStrut_7 = Box.createHorizontalStrut(20);
		horizontalBox_3.add(horizontalStrut_7);
		
		JButton DateKeyFind = new JButton("查询");
		horizontalBox_3.add(DateKeyFind);
		Component horizontalStrut_5 = Box.createHorizontalStrut(250);
		horizontalBox_3.add(horizontalStrut_5);
		
		JPanel History = new JPanel();
		History.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 255), 2), "历史日志", TitledBorder.LEFT, TitledBorder.TOP, null, Color.BLUE));
		DateManagment.add(History, BorderLayout.CENTER);
		History.setLayout(new BorderLayout());
		
		HistoryText=new JTextArea();
		HistoryText.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(HistoryText);
		History.add(scrollPane);
		/*日志管理End*/
		
	}
	
	
	
	
	
	
	

	
}
