package client;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JTabbedPane;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JComboBox;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JTextField;
import javax.swing.JTree;

import java.awt.Font;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public class ClientSecondFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	JTree tree ;
	JComboBox comboBox = new JComboBox();
	static String ID;
	static AsynchronousSocketChannel clientChannel;
	JLabel MyPicture= new JLabel();
	static JLabel MyName = new JLabel();
	String age;
	String address;
	String sex;
	static DefaultTreeModel treeModel;
	static Map<Integer,String[]> friendMap=new HashMap<>();
	static Map<String,ClientChatFrame>  friendFrameMap=new HashMap<>();
	static Map<String ,Integer> friendIDMap=new HashMap<>(); 
	static DefaultMutableTreeNode friend=new DefaultMutableTreeNode("�ҵĺ���");
	int mapi=0;
	/**
	 * Create the frame.
	 */
	
	private void MyPersonSet(String m)
	{
		m=m.substring(5);
		int n=m.indexOf(MyProtocol.NAME_MESSAGE);
		MyPicture.setIcon(new ImageIcon(".\\head\\"+m.substring(0,n)+".gif"));
		//System.out.println(m.substring(0,n));
		
		m=m.substring(n+5);
		n=m.indexOf(MyProtocol.AGE_MESSAGE);
		MyName.setText(m.substring(0,n)+" ( "+ID+" )");
		//System.out.println(m.substring(0,n));
		
		m=m.substring(n+5);
		n=m.indexOf(MyProtocol.SEX_MESSAGE);
		age=m.substring(0,n);
		//System.out.println(m.substring(0,n));
		
		m=m.substring(n+5);
		n=m.indexOf(MyProtocol.ADDRESS_MESSAGE);
		sex=m.substring(0,n);
		//System.out.println(m.substring(0,n));
		
		m=m.substring(n+5);
		n=m.indexOf(MyProtocol.MESSAGE_END);
		address=m.substring(0,n);
		//System.out.println(m.substring(0,n));
		
		m=m.substring(n+5);
		
	}
	
	/*�ͻ��˽�����Ϣ����*/
	class ListenerThread extends Thread
	{
		public void run()
		{
				final ByteBuffer buff = ByteBuffer.allocate(2048);
				buff.clear();
				String temp="";
				clientChannel.read(buff, null, new CompletionHandler<Integer,Object>() 
				{
					@Override
					public void completed(Integer result, Object attachment)
					{
						buff.flip();
						String content = StandardCharsets.UTF_8.decode(buff).toString();
						String message=content.substring(5);
						
						switch(content.substring(0,5))
						{
						case MyProtocol.PERSON_MESSAGE:treeModel=ScanFrient(message);break;
						case MyProtocol.MY_PERSON_MESSAGE: MyPersonSet(message);break;
						case MyProtocol.PRIVATE_MESSAGE:ShowMessage(message);break;
						case MyProtocol.SYSTEM_MESSAGE:ShowSystemMessage(message);break;
						case MyProtocol.RUN_STATE_MESSAGE:FriendRunStateShow(message);break;
						case MyProtocol.USE_REPEAT_LOGIN_MESSAGE:RepeatLoginOffLine(message);break;
						case MyProtocol.SYSTEM_OFFLINE_MESSAGE:SystemOffline(message);break;
						}
						buff.clear();
						clientChannel.read(buff , null , this);
					}
					
					@Override
					public void failed(Throwable ex, Object attachment)
					{
						System.out.println("��ȡ����ʧ��: " + ex);
					}
				});
		}
	}
	
	/*�ظ���¼ǿ������*/
	private void RepeatLoginOffLine(String m)
	{
		m=m.substring(5);
		JOptionPane.showMessageDialog(null,"�˺��������ط���¼����ǿ�����ߡ���������","����֪ͨ",JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
	/*������ǿ������*/
	private void SystemOffline(String m)
	{
		m=m.substring(5);
		JOptionPane.showMessageDialog(null,"�����Ҫ����˺�����","����֪ͨ",JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
	
	
	/*��������״̬����*/
	private void FriendRunStateShow(String m)
	{
		String ID;
		String state;
		m=m.substring(5);
		int n=m.indexOf(MyProtocol.RUN_MESSAGE);
		ID=m.substring(0,n);
		m=m.substring(n+5);
		n=m.indexOf(MyProtocol.MESSAGE_END);
		state=m.substring(0,n);
		m=m.substring(n+5);
		String []temp=friendMap.get(friendIDMap.get(ID));
		
		DefaultMutableTreeNode  node=(DefaultMutableTreeNode)friend.getChildAt(friendIDMap.get(temp[0]));
		JLabel jl=(JLabel)node.getUserObject();
		String info=jl.getText();
		n=info.indexOf("["+temp[1]+"]");
		temp[1]=state;
		jl.setText(info.substring(0,n)+"["+state+"]"+info.substring(n+"[����]".length(),info.length()));
		treeModel=new DefaultTreeModel(friend);
	}
	
	/*��ʾϵͳ��Ϣ*/
	private void ShowSystemMessage(String m)
	{
		SystemMessage sm=new SystemMessage(m);
	}
	
	/*��ʾ��Ϣ*/
	private void ShowMessage(String m)
	{
		m=m.substring(5);
		int n=m.indexOf(MyProtocol.MESSAGE_START);
		String ID=m.substring(0,n);
		m=m.substring(n+5);
		n=m.indexOf(MyProtocol.MESSAGE_END);
		String message=m.substring(0,n);
		m=m.substring(n+5);
		System.out.println(ID+"����"+ClientSecondFrame.ID);
		ClientChatFrame frame=friendFrameMap.get(ID);
		String []temp=null;
		if(frame==null)
		{
			temp=friendMap.get(friendIDMap.get(ID));
			if(temp==null)
				{System.out.println(ID+"û���������");
				return ;
				}
			
			frame=new ClientChatFrame(ClientSecondFrame.ID,temp,clientChannel);
			friendFrameMap.put(ID,frame);
		}
		else
		{
			temp=friendMap.get(friendIDMap.get(ID));
			if(temp==null)
			{
				System.out.println(ID+"û���������");
				return ;
			}	
		}
		if(message.equals(MyProtocol.JITTY_MESSAGE))
			{
				frame.WindowsJitter();
				String time=ClientChatFrame.getNowTime();
				frame.text.append("[ "+time+" ]\r\n���������˴��ڶ���\r\n");
			}					//���ڶ�����Ϣ
		else
		{
		frame.ShowInformation(ID,temp[2],message);
		DefaultMutableTreeNode  node=(DefaultMutableTreeNode)friend.getChildAt(friendIDMap.get(temp[0]));
		JLabel jl=(JLabel)node.getUserObject();
		String info=jl.getText();
		if(!info.endsWith("<���������Ϣ>"))
		{
			jl.setText(info+"<���������Ϣ>");
		}
		treeModel=new DefaultTreeModel(friend);
		}
	}

	
	
	
	/*���������б�*/
	private DefaultTreeModel ScanFrient(String m) 
	{
		String ID;
		String run;
		String name;
		String age,sex,address,pic;
		DefaultTreeModel dtm = null;
		//System.out.println(m);
		while(m.length()-5>0)
		{
			if(m.substring(5,10).equals(MyProtocol.RUN_MESSAGE))
				m=m.substring(10);
			else
				m=m.substring(5);
		int n=m.indexOf(MyProtocol.PICTURE_MESSAGE);
		//if(n>0)
		run=m.substring(0,n);
		
		m=m.substring(n+5);
		n=m.indexOf(MyProtocol.ID_MESSAGE);
		//if(n>0)
		pic=m.substring(0,n);
		
		m=m.substring(n+5);
		n=m.indexOf(MyProtocol.NAME_MESSAGE);
		//if(n>0)
		ID=m.substring(0,n);
		
		m=m.substring(n+5);
		n=m.indexOf(MyProtocol.AGE_MESSAGE);
		//if(n>0)
		name=m.substring(0,n);
		
		m=m.substring(n+5);
		n=m.indexOf(MyProtocol.SEX_MESSAGE);
		//if(n>0)
		age=m.substring(0,n);
		
		m=m.substring(n+5);
		n=m.indexOf(MyProtocol.ADDRESS_MESSAGE);
		sex=m.substring(0,n);
		
		m=m.substring(n+5);
		n=m.indexOf(MyProtocol.MESSAGE_END);
		address=m.substring(0,n);
		
		m=m.substring(n+5);
		
		
		/*
		System.out.println(ID);
		System.out.println(run);
		System.out.println(name);
		System.out.println(age);
		System.out.println(sex);
		System.out.println(address);
		System.out.println(pic);
		*/
		String []temp={ID,run,name,age,sex,address,pic};
		JLabel jl;
		if(run.equals("����"))
			jl=new JLabel(name+" ( "+ID+" ) ["+run+"]");
		else
			jl=new JLabel(name+" ( "+ID+" ) [����]");
		ImageIcon image=new ImageIcon(Toolkit.getDefaultToolkit().createImage(".\\head\\"+pic+".gif"));
		jl.setIcon(image);
		DefaultMutableTreeNode node=new DefaultMutableTreeNode(jl);
		friend.add(node);
		//System.out.println(friend.getIndex(node));
		friendMap.put(friend.getIndex(node),temp);
		friendIDMap.put(temp[0],friend.getIndex(node));
		
		}
		//System.out.println(friendIDMap);
		dtm=new DefaultTreeModel(friend);
		return dtm;	
	}
	
	
	
	
	class MyTreeCellRenderer extends DefaultTreeCellRenderer {
		
		
	    public Component getTreeCellRendererComponent(JTree tree, Object value,boolean selected, boolean expanded,boolean leaf, int row, boolean hasFocus) {
	       
	   DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
	   
	   Object obj = node.getUserObject();
	   this.setOpenIcon(new ImageIcon("images/open.jpg"));
	   this.setClosedIcon(new ImageIcon("images/close.jpg"));

	   
	   if(obj instanceof JLabel) {
		   
	        JLabel label = (JLabel)obj;
	        
	        DefaultTreeCellRenderer tempCellRenderer = new DefaultTreeCellRenderer();
	        
	        tempCellRenderer.setLeafIcon(label.getIcon());
	        
	        return  tempCellRenderer.getTreeCellRendererComponent(tree,label.getText(),selected,expanded,true,row,hasFocus);
	   }
	   else if(obj instanceof String)
	   {
		   String s=(String) obj;
		   DefaultTreeCellRenderer tempCellRenderer = new DefaultTreeCellRenderer();
		   tempCellRenderer.setLeafIcon(new ImageIcon("images/close.jpg"));
		   
		   return  tempCellRenderer.getTreeCellRendererComponent(tree,s,selected,expanded,true,row,hasFocus);
	   }
	   
	   return super.getTreeCellRendererComponent(tree,value,selected,expanded,leaf,row,hasFocus);
	   
	   }
	}

	
	
	
	
	
	public ClientSecondFrame(final String ID,final AsynchronousSocketChannel clientChannel) {
		this.setVisible(true);
		this.clientChannel=clientChannel;
		this.ID=ID;
		this.setTitle("My QQ");
		this.addWindowListener(new WindowListener()
		{

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO �Զ����ɵķ������
				try {
					ClientSecondFrame.clientChannel.write(ByteBuffer.wrap((MyProtocol.CLOSING_MESSAGE+MyProtocol.ID_MESSAGE
							+ClientSecondFrame.ID+MyProtocol.RUN_STATE_MESSAGE
							+"����"+MyProtocol.MESSAGE_END).getBytes("utf-8"))).get();
				} catch (UnsupportedEncodingException | InterruptedException
						| ExecutionException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO �Զ����ɵķ������
				
			}
			
		}
				);
		setIconImage(Toolkit.getDefaultToolkit().getImage(".\\QQbig2.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		/*��������*/
		PopupMenu pm = new PopupMenu();
		Image image=Toolkit.getDefaultToolkit().getImage(".\\tray.gif");
		  
		   MenuItem  exit  = new MenuItem("�ر�"); 
		   exit.addActionListener(new ActionListener()
		   {
			   public void actionPerformed(ActionEvent a)
			   {
				   System.exit(0);
			   }
		   });
           TrayIcon ti = new TrayIcon( image,"QQ",pm);
		   ti.setToolTip(MyName.getText());
		   ti.addActionListener(new ActionListener(){
			   public void actionPerformed(ActionEvent a)
			   {
				   setVisible(true);
			   }
		   });
           pm.add(exit);
		   try {
			MyClient.st.add(ti);
		} catch (AWTException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		
		
		
		/*���������Ϣ*/new ListenerThread().start();
		
		try {
			this.clientChannel.write(ByteBuffer.wrap((MyProtocol.MY_PERSON_MESSAGE+ID+MyProtocol.MESSAGE_END).getBytes("utf-8"))).get();
			
		} catch (UnsupportedEncodingException | InterruptedException| ExecutionException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}//���������Ϣ
		
		
		JPanel top = new JPanel();
		top.setBorder(new EmptyBorder(0, 0, 0, 25));
		contentPane.add(top, BorderLayout.NORTH);
		top.setLayout(new BorderLayout(0, 0));
		
		Box horizontalBox = Box.createHorizontalBox();
		top.add(horizontalBox);
		
		horizontalBox.add(MyPicture);
		
		Box verticalBox = Box.createVerticalBox();
		horizontalBox.add(verticalBox);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_1);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		horizontalBox_1.add(horizontalStrut_2);
		MyName.setFont(new Font("��Բ", Font.BOLD, 15));
		
		
		horizontalBox_1.add(MyName);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(10);
		horizontalBox_1.add(horizontalStrut_1);
		
		
		comboBox.addItem("����");
		comboBox.addItem("����");
		comboBox.addItem("����");
		comboBox.setSelectedItem("����");
		comboBox.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO �Զ����ɵķ������
				System.out.println(comboBox.getSelectedItem());
				try 
				{
					ClientSecondFrame.clientChannel.write(ByteBuffer.wrap((MyProtocol.RUN_STATE_MESSAGE+MyProtocol.ID_MESSAGE+ClientSecondFrame.ID+MyProtocol.RUN_MESSAGE+comboBox.getSelectedItem()+MyProtocol.MESSAGE_END).getBytes("utf-8"))).get();
				} catch (UnsupportedEncodingException | InterruptedException| ExecutionException e) 
				{
					e.printStackTrace();
				}
			}
			
		});
		
		horizontalBox_1.add(comboBox);
		
		textField = new JTextField();
		textField.setEditable(false);
		verticalBox.add(textField);
		textField.setColumns(10);
		
		JPanel first=new JPanel();
		JPanel second=new JPanel();
		
		JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabPane, BorderLayout.CENTER);
		tabPane.addTab("˽��",new ImageIcon(".\\private.png"),first);
		first.setLayout(new BorderLayout(0, 0));
		
		try {
			this.clientChannel.write(ByteBuffer.wrap((MyProtocol.MY_FRIENT_MESSAGE+ID+MyProtocol.MESSAGE_END).getBytes("utf-8"))).get();
		} catch (UnsupportedEncodingException | InterruptedException
				| ExecutionException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		/*�����б�*/
		
		DefaultTreeModel dtm2=new DefaultTreeModel(friend);
		tree=new JTree(dtm2);
		tree.setRootVisible(true);
		tree.setShowsRootHandles(true);
		tree.setCellRenderer(new MyTreeCellRenderer());
		tree.setRowHeight(55);
		tree.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO �Զ����ɵķ������
				if(arg0.getClickCount()>=2)
				{
					String[] temp=friendMap.get(tree.getSelectionRows()[0]-1);
					
					DefaultMutableTreeNode  node=(DefaultMutableTreeNode)friend.getChildAt(friendIDMap.get(temp[0]));
					JLabel jl=(JLabel)node.getUserObject();
					String info=jl.getText();
					if(info.endsWith("<���������Ϣ>"))
					{	
						int n=info.indexOf("<���������Ϣ>");
						jl.setText(info.substring(0,n));
					}
					treeModel=new DefaultTreeModel(friend);
					
					ClientChatFrame newFrame=friendFrameMap.get(temp[0]);
					
					if(newFrame==null)
					{
						newFrame=new ClientChatFrame(ClientSecondFrame.ID,temp,clientChannel);
						friendFrameMap.put(temp[0],newFrame);
					}
					newFrame.setVisible(true);
					//System.out.println(friendFrameMap);
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO �Զ����ɵķ������
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO �Զ����ɵķ������
				
			}
			
		});
		first.add(new JScrollPane(tree), BorderLayout.CENTER);
		
		tabPane.addTab("Ⱥ����",new ImageIcon(".\\public.png"),second);
		JPanel down = new JPanel();
		contentPane.add(down, BorderLayout.SOUTH);
		
		new Thread()
		{
			public void run()
			{
				while(true)
				{
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
					tree.setModel(treeModel);
				}
			}
		}.start();
		
		
	}

}
