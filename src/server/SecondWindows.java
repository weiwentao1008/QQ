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
	 * ��ȡ���ݿⴴ���û����
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
					  if(rs.getObject(number-2).equals("����")==true)
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
			
			JOptionPane.showMessageDialog(null, "���ݿ��ȡʧ��","����",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return null;
		}
	}
	
	/*����ʱ������*/
	 private Vector<Integer> CreateDateVector(int start,int end) {
		 
		 Vector<Integer> date =new Vector<Integer>();
		 for(int i=start;i<=end;i++)
			 date.addElement(i);
		 return date;
	}
	
	
	
	/**
	 * ����ͨѶ���񰴼�������
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
					// TODO �Զ����ɵ� catch ��
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
		
		
			/*����������*/
		JPanel ServerManagment = new JPanel();
		tabbedPane.addTab("����������", null, ServerManagment, null);
		ServerManagment.setLayout(new BorderLayout());
		
		JPanel runUserList = new JPanel();
		runUserList.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 255), 2), "�����û��б�", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 255)));
		ServerManagment.add(runUserList, BorderLayout.NORTH);
		runUserList.setLayout(new BorderLayout());
		
		UserTable =new JTable(ReadSql(con,"SELECT * FROM qq;",READ_ONLY,RUNING));
		new RefreshThread().start();
		JScrollPane userScrollPane = new JScrollPane(UserTable);
		userScrollPane.setPreferredSize(new Dimension(0,300));
		runUserList.add(userScrollPane, BorderLayout.CENTER);
		JPanel telentMassage = new JPanel();
		telentMassage.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 255), 2), "ͨѶ��Ϣ��ʾ", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 255)));
		ServerManagment.add(telentMassage, BorderLayout.CENTER);
		telentMassage.setLayout(new BorderLayout());
		
		
		textArea = new JTextArea(8,20);
		textArea.setEditable(false);
		JScrollPane massageScrollPane = new JScrollPane(textArea);
		telentMassage.add(massageScrollPane);
		
		
		
		JPanel Managment = new JPanel();
		Managment.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 255), 2), "����������", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 255)));
		ServerManagment.add(Managment, BorderLayout.EAST);
		Managment.setLayout(new BorderLayout());
		
		final JLabel earthImage = new JLabel();
		earthImage.setIcon(new ImageIcon("earth_2.jpg"));
		Managment.add(earthImage, BorderLayout.WEST);
		
		Box verticalBox = Box.createVerticalBox();
		Managment.add(verticalBox, BorderLayout.EAST);
		
		final JButton runServer = new JButton("����ͨѶ����");
		final JButton stopServer = new JButton("ֹͣͨѶ����");
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
					SecondWindows.textArea.append("---ͨѶ����ֹͣ---\n");
					runServer.setEnabled(true);
					stopServer.setEnabled(false);
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		});
		verticalBox.add(stopServer);
		
		Component verticalGlue_1 = Box.createVerticalGlue();
		verticalBox.add(verticalGlue_1);
		
		JButton userstop = new JButton("ǿ���û�����");
		userstop.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
						
				Iterator<String> foreach=AIOServer.channelMap.keySet().iterator();
				while(foreach.hasNext())
				{
					String id=foreach.next();
							Statement st;
							System.out.println("keynum:"+AIOServer.channelMap.keySet().size()+"ǿ�������û���"+id);
							try
							{
								if(!id.equals(SystemSendMessage.Admin))
								{
								AIOServer.channelMap.get(id).write(ByteBuffer.wrap((MyProtocol.SYSTEM_OFFLINE_MESSAGE+MyProtocol.MESSAGE_END).getBytes(AIOServer.UTF_8))).get();
								st = SwingFrameStart.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
								ResultSet rs=st.executeQuery("SELECT ID,����,����״̬  FROM qq WHERE ID="+id+";");
								rs.first();
								String time=AIOServer.getTime();
								SecondWindows.textArea.append(" ["+time+"]  "+rs.getString("����")+"( "+id+" ) ������\r\n");
								rs.updateString("����״̬","����");
								rs.updateRow();
								rs.close();
								st.close();
								}
							}
							catch (Exception ex)
							{
								System.out.println("�û�ǿ������ʱ����");
								ex.printStackTrace();
							}
						}
						
					}	
		});
		verticalBox.add(userstop);
		
		
		
		JPanel sengInformation = new JPanel();
		sengInformation.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 255), 2), "���淢��", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 255)));
		ServerManagment.add(sengInformation, BorderLayout.SOUTH);
		sengInformation.setLayout(new BorderLayout());
		
		final JTextArea Information = new JTextArea();
		Information.setRows(3);
		
		JScrollPane sendScrollPane = new JScrollPane(Information);
		sengInformation.add(sendScrollPane, BorderLayout.CENTER);
		
		
		
		Box horizontalBox = Box.createHorizontalBox();
		sengInformation.add(horizontalBox, BorderLayout.SOUTH);
		
		JCheckBox CheckQun = new JCheckBox("Ⱥ����");
		horizontalBox.add(CheckQun);
		
		JCheckBox CheckSystem = new JCheckBox("ϵͳ֪ͨ");
		horizontalBox.add(CheckSystem);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue);
		
		JButton send = new JButton("����");
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
						SystemSendMessage.clientChannel.write(ByteBuffer.wrap(content.trim().getBytes("utf-8"))).get();    //��
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
		/*�����ȼ�����*/
		textArea.getInputMap().put(KeyStroke.getKeyStroke('\n', java.awt.event.InputEvent.CTRL_MASK) , "send");
		textArea.getActionMap().put("send", sendAction);
		horizontalBox.add(send);
		/*����������     End*/
		
		
		/*�û�����*/
		JPanel UserManagment = new JPanel();
		tabbedPane.addTab("�û�����", null, UserManagment, null);
		UserManagment.setLayout(new BorderLayout());
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		horizontalBox_1.setBorder(new EmptyBorder(3, 2, 2, 2));
		UserManagment.add(horizontalBox_1, BorderLayout.NORTH);
		
		Component horizontalStrut = Box.createHorizontalStrut(300);
		horizontalBox_1.add(horizontalStrut);
		
		
		horizontalBox_1.add(new JLabel("�˺ţ�"));
		userID = new JTextField(5);
		horizontalBox_1.add(userID);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		horizontalBox_1.add(horizontalStrut_2);
		
		
		
		JButton Find = new JButton("��ѯ");
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
					FoundNum.setText("�ܼ�¼����"+colNumber);
				}
		});
		horizontalBox_1.add(Find);
		
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		horizontalBox_1.add(horizontalStrut_3);
		
		 FoundNum = new JLabel("�ܼ�¼����"+colNumber);
		horizontalBox_1.add(FoundNum);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(300);
		horizontalBox_1.add(horizontalStrut_1);
		
		JPanel userInformation = new JPanel();
		userInformation.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 255), 2), "�û���Ϣ��", TitledBorder.LEFT, TitledBorder.TOP, null, Color.BLUE));
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
		
		JButton AddUser = new JButton("����û�");
		AddUser.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				AddUser au=new AddUser("�˺�ע��",con);
				au.setVisible(true);
			}
			
		});
		horizontalBox_2.add(AddUser);
		
		JButton ModifyUser = new JButton("�޸��û�");
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
						JOptionPane.showMessageDialog(null,"���ݿ��ȡʧ��","����",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		horizontalBox_2.add(ModifyUser);
		
		JButton DeleteUser = new JButton("ɾ���û�");
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
						int temp=JOptionPane.showConfirmDialog(null,"�Ƿ�ɾ��ID="+tm.getValueAt(row,0));
						if(temp==JOptionPane.OK_OPTION)
							ID=tm.getValueAt(row,0);
						else
							return ;
						}
						else
						ID=JOptionPane.showInputDialog("ɾ����ID��");
					ResultSet rs=st.executeQuery("SELECT * FROM `qq` WHERE ID="+ID+";");
					rs.first();
					rs.updateString("Model","STOP");
					rs.updateRow();
					CreateLog.InsertLog(rs.getString("����"), rs.getInt("ID"),CreateLog.DELETE);
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,"���ݿ��ȡʧ��","����",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		horizontalBox_2.add(DeleteUser);
		
		JButton ResetPassword = new JButton("��������");
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
						int temp=JOptionPane.showConfirmDialog(null,"�Ƿ�����ID="+tm.getValueAt(row,0)+" ����");
						if(temp==JOptionPane.OK_OPTION)
							ID=tm.getValueAt(row,0);
						else
							return ;
						}
						else
						ID=JOptionPane.showInputDialog("��������IDΪ��");
					ResultSet rs=st.executeQuery("SELECT * FROM `qq` WHERE ID="+ID+";");
					rs.first();
					String newPassword=JOptionPane.showInputDialog("��������Ϊ��");
						rs.updateString("����",newPassword);
						rs.updateRow();
					newPassword=null;
					CreateLog.InsertLog(rs.getString("����"),(Integer) ID,CreateLog.CHANGE);
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,"���ݿ��ȡʧ��","����",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		horizontalBox_2.add(ResetPassword);
		
		JButton ResetAllPassword = new JButton("������������");
		ResetAllPassword.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				Statement st;
				try {
					st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
					ResultSet rs=st.executeQuery("SELECT * FROM `qq` WHERE Model='RUN' ;");
					String newPassword=JOptionPane.showInputDialog("��������Ϊ��");
					while(rs.next())
						{
							rs.updateString("����",newPassword);
							rs.updateRow();
						}
					newPassword=null;
					CreateLog.InsertLog("������������",00000,CreateLog.CHANGE);
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,"���ݿ��ȡʧ��","����",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		horizontalBox_2.add(ResetAllPassword);
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		horizontalBox_2.add(horizontalGlue_2);
		/*�û�����End*/
		
		
		/*��־����*/
		JPanel DateManagment = new JPanel();
		tabbedPane.addTab("��־����", null, DateManagment, null);
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
		horizontalBox_3.add(new JLabel("��"));
		YearComboBox.setSelectedIndex(time.get(Calendar.YEAR)-1990);
		
		JComboBox<Integer> MonthComboBox = new JComboBox<Integer>();
		MonthComboBox.setModel(new DefaultComboBoxModel<Integer>(CreateDateVector(1,12)));
		horizontalBox_3.add(MonthComboBox);
		horizontalBox_3.add(new JLabel("��"));
		MonthComboBox.setSelectedIndex(time.get(Calendar.MONTH)+1-1);
		
		JComboBox<Integer> DayComboBox = new JComboBox<Integer>();
		DayComboBox.setModel(new DefaultComboBoxModel<Integer>(CreateDateVector(1,31)));
		horizontalBox_3.add(DayComboBox);
		horizontalBox_3.add(new JLabel("��"));
		DayComboBox.setSelectedIndex(time.get(Calendar.DAY_OF_MONTH)-1);
		
		Component horizontalStrut_6 = Box.createHorizontalStrut(20);
		horizontalBox_3.add(horizontalStrut_6);
		horizontalBox_3.add(new JLabel("�ؼ��֣�"));
		JTextField KeyText=new JTextField();
		horizontalBox_3.add(KeyText);
		
		Component horizontalStrut_7 = Box.createHorizontalStrut(20);
		horizontalBox_3.add(horizontalStrut_7);
		
		JButton DateKeyFind = new JButton("��ѯ");
		horizontalBox_3.add(DateKeyFind);
		Component horizontalStrut_5 = Box.createHorizontalStrut(250);
		horizontalBox_3.add(horizontalStrut_5);
		
		JPanel History = new JPanel();
		History.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 255), 2), "��ʷ��־", TitledBorder.LEFT, TitledBorder.TOP, null, Color.BLUE));
		DateManagment.add(History, BorderLayout.CENTER);
		History.setLayout(new BorderLayout());
		
		HistoryText=new JTextArea();
		HistoryText.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(HistoryText);
		History.add(scrollPane);
		/*��־����End*/
		
	}
	
	
	
	
	
	
	

	
}
