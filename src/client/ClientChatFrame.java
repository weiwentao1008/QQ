package client;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import java.awt.Component;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;

public class ClientChatFrame extends JFrame {

	private JPanel contentPane;
	 JTextArea text=new JTextArea();
	 AsynchronousSocketChannel clientChannel;
	 static String ID;
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					
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
					
					
					
					
					ClientChatFrame frame = new ClientChatFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/
	 
	 /*获取时间*/
	 public static String getNowTime()
	 {
		 Calendar c=Calendar.getInstance();
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		 return sdf.format(c.getTime()) ;
		 
	 }
	 /*窗口抖动*/
	 public void WindowsJitter()
	 {
		 this.setVisible(true);
		 Point point=this.getLocation();
		 double x=point.getX();
		 double y=point.getY();
		 for(int i=0;i<100;i++)
		 {
			 Point p=new Point();
			 p.setLocation(x, y-3);
			 this.setLocation(p);
			 p.setLocation(x-3, y);
			 this.setLocation(p);
			 p.setLocation(x, y+3);
			 this.setLocation(p);
			 p.setLocation(x+3, y);
			 this.setLocation(p);
			 this.setLocation(point);
		 }
	 }
	 public void ShowInformation(String ID,String name,String message)
	 {
		 String time=getNowTime();
		text.append(name+"[ "+time+" ]\r\n"+message+"\r\n");
	 }
	 
	 
	 
	/**
	 * Create the frame.
	 */
	public ClientChatFrame(final String ID,final String[] info,final AsynchronousSocketChannel clientChannel) {
		this.clientChannel=clientChannel;
		this.setTitle("与"+info[2]+"( "+info[0]+" )聊天中");
		this.ID=ID;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		int x=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int y=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setBounds(x/2-600/2,y/2-550/2, 689, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel top = new JPanel();
		contentPane.add(top, BorderLayout.NORTH);
		top.setLayout(new BorderLayout(0, 0));
		
		Box horizontalBox = Box.createHorizontalBox();
		top.add(horizontalBox, BorderLayout.NORTH);
		
		JLabel picture = new JLabel();
		picture.setIcon(new ImageIcon(".\\head\\"+info[6]+".gif"));
		horizontalBox.add(picture);
		
		Box verticalBox = Box.createVerticalBox();
		horizontalBox.add(verticalBox);
		
		JLabel name = new JLabel("\u6635\u79F0");
		name.setFont(new Font("幼圆", Font.BOLD, 16));
		name.setText(info[2]);
		verticalBox.add(name);
		
		JLabel geXing = new JLabel("\u4E2A\u6027\u7B7E\u540D");
		verticalBox.add(geXing);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		top.add(horizontalBox_1, BorderLayout.SOUTH);
		
		JButton call = new JButton("\u8BED\u97F3");
		horizontalBox_1.add(call);
		
		JButton video = new JButton("\u89C6\u9891");
		horizontalBox_1.add(video);
		
		JButton control = new JButton("\u8FDC\u7A0B\u684C\u9762");
		horizontalBox_1.add(control);
		
		JButton SendFile = new JButton("\u4F20\u9001\u6587\u4EF6");
		horizontalBox_1.add(SendFile);
		/*启动聊天服务连接*/
		//ConnectChat(IP,PORT);
		
		SwingUtilities.updateComponentTreeUI(getContentPane());
		
		JPanel mainPanel = new JPanel();
		contentPane.add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		
		text.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(text);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		mainPanel.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		Box horizontalBox_2 = Box.createHorizontalBox();
		panel.add(horizontalBox_2, BorderLayout.NORTH);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		horizontalBox_2.add(horizontalStrut_1);
		
		JButton Button1 = new JButton("抖动");
		Button1.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					clientChannel.write(ByteBuffer.wrap((MyProtocol.PRIVATE_MESSAGE+MyProtocol.FRIEND_ID_MESSAGE+info[0]
							+MyProtocol.ID_MESSAGE+ID+MyProtocol.MESSAGE_START+MyProtocol.JITTY_MESSAGE+MyProtocol.MESSAGE_END).getBytes("utf-8"))).get();
					String time=getNowTime();
					text.append("[ "+time+" ]\r\n您向"+info[0]+"发送了窗口抖动\r\n");
				} catch (UnsupportedEncodingException | InterruptedException
						| ExecutionException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				WindowsJitter();
				
				
			}
			
		});
		horizontalBox_2.add(Button1);
		
		JButton Button_2 = new JButton("New button");
		horizontalBox_2.add(Button_2);
		
		JButton Button_3 = new JButton("New button");
		horizontalBox_2.add(Button_3);
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(80);
		horizontalBox_2.add(horizontalStrut_3);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		horizontalBox_2.add(horizontalGlue_1);
		
		JButton Button_4 = new JButton("\u804A\u5929\u8BB0\u5F55");
		horizontalBox_2.add(Button_4);
		
		final JTextArea textArea = new JTextArea();
		textArea.setRows(4);
		panel.add(textArea, BorderLayout.CENTER);
		
		Box horizontalBox_3 = Box.createHorizontalBox();
		panel.add(horizontalBox_3, BorderLayout.SOUTH);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox_3.add(horizontalGlue);
		
		JButton close = new JButton("\u5173\u95ED");
		close.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				//ClientSecondFrame.friendFrameMap.remove(info[0]);
				//dispose();
				DefaultMutableTreeNode  node=(DefaultMutableTreeNode)ClientSecondFrame.friend.getChildAt(ClientSecondFrame.friendIDMap.get(info[0]));
				JLabel jl=(JLabel)node.getUserObject();
				jl.setText(info[2]+" ( "+info[0]+" )");
				ClientSecondFrame.treeModel=new DefaultTreeModel(ClientSecondFrame.friend);
				setVisible(false);
			}
			
		});
		horizontalBox_3.add(close);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(10);
		horizontalBox_3.add(horizontalStrut_2);
		
		JButton send = new JButton("\u53D1\u9001");
		
		send.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!textArea.getText().trim().equals(""))
				{
					
					try {
						ShowInformation(ClientChatFrame.ID,ClientSecondFrame.MyName.getText(),textArea.getText().trim());
						clientChannel.write(ByteBuffer.wrap((MyProtocol.PRIVATE_MESSAGE+MyProtocol.FRIEND_ID_MESSAGE+info[0]
								+MyProtocol.ID_MESSAGE+ID+MyProtocol.MESSAGE_START+textArea.getText().trim()+MyProtocol.MESSAGE_END).getBytes("utf-8"))).get();
						textArea.setText("");
					} catch (UnsupportedEncodingException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
				}
			}
			
		});
		
		
		horizontalBox_3.add(send);
		
		Component horizontalStrut = Box.createHorizontalStrut(10);
		horizontalBox_3.add(horizontalStrut);
		
	}
	
	/*聊天系统服务器连接*/
	/*private void ConnectChat(String IP,int PORT)
	{
		try {
			 executor = Executors.newFixedThreadPool(1);

			 channelGroup =AsynchronousChannelGroup.withThreadPool(executor);
				
			clientChannel = AsynchronousSocketChannel.open(channelGroup);
				
				
			clientChannel.connect(new InetSocketAddress(IP, PORT)).get();
				
				} 
			catch (InterruptedException e) {
				
				e.printStackTrace();
			} 
			catch (ExecutionException e) {
				JOptionPane.showMessageDialog(null, "服务器连接失败&线程池问题", "连接失败", JOptionPane.ERROR_MESSAGE);
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(null, "IO异常", "失败", JOptionPane.ERROR_MESSAGE);
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
	}*/

}
