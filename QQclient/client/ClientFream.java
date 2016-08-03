package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.Box;
import javax.swing.JLabel;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JCheckBox;


public class ClientFream extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private ExecutorService executor;
	private AsynchronousChannelGroup channelGroup ;
	private AsynchronousSocketChannel clientChannel;
	
	/**
	 * 客户端启动连接
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 * 
	 * */
	private void  ClientConnect(String IP,int PORT)
	{
		try {
		 executor = Executors.newFixedThreadPool(2);

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
	}
	
	
	private void LoginServer() 
	{
		try {
			clientChannel.write(ByteBuffer.wrap((MyProtocol.LOGIN_MESSAGE+textField.getText().trim()+MyProtocol.PASSWORD_MASSAGE
													+String.valueOf(passwordField.getPassword())+MyProtocol.MESSAGE_END).getBytes("utf-8"))).get();
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		final ByteBuffer buff = ByteBuffer.allocate(1024);
		buff.clear();
		clientChannel.read(buff, null, new CompletionHandler<Integer,Object>() 
		{
			@Override
			public void completed(Integer result, Object attachment)
			{
				buff.flip();
				String content = StandardCharsets.UTF_8.decode(buff).toString();
				buff.clear();
				//System.out.println(content);
				if(content.substring(0,5).equals(MyProtocol.LOGIN_SUCCESS_MESSAGE))
				{
					content=content.substring(10);
					dispose();
					new ClientSecondFrame(textField.getText().trim(),clientChannel).setVisible(true);
				}
				else
				{
					if(content.substring(0,5).equals(MyProtocol.LOGIN_ERROR_MESSAGE))
					{
						content=content.substring(5);
						int n=content.indexOf(MyProtocol.MESSAGE_END);
						int num=Integer.valueOf(content.substring(0,n));
						switch(num)
						{
						case 0:JOptionPane.showMessageDialog(null,"这个账号没注册","登录失败",JOptionPane.ERROR_MESSAGE);break;
						case 1:JOptionPane.showMessageDialog(null,"密码错误","登录失败",JOptionPane.ERROR_MESSAGE);break;
						}
					}
				}
			}
			@Override
			public void failed(Throwable ex, Object attachment)
			{
				System.out.println("读取数据失败: " + ex);
			}
		});
		
		
	}
	
	
	
	
	
	
	
	/**
	 * Create the frame.
	 */
	public ClientFream() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.setTitle("QQ");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\QQbig2.png"));
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_1, BorderLayout.NORTH);
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		horizontalBox_1.add(horizontalGlue_2);
		
		JLabel icoqq = new JLabel();
		icoqq.setIcon(new ImageIcon(".\\QQbig2.png"));
		horizontalBox_1.add(icoqq);
		
		Component horizontalGlue_3 = Box.createHorizontalGlue();
		horizontalBox_1.add(horizontalGlue_3);
		
		JPanel login = new JPanel();
		contentPane.add(login, BorderLayout.CENTER);
		login.setLayout(new BorderLayout(0, 0));
		
		Box verticalBox = Box.createVerticalBox();
		verticalBox.setBorder(new EmptyBorder(0, 80, 0, 0));
		login.add(verticalBox, BorderLayout.WEST);
		
		Component verticalGlue = Box.createVerticalGlue();
		verticalBox.add(verticalGlue);
		
		JLabel user = new JLabel("\u7528\u6237\u540D");
		user.setAlignmentX(Component.RIGHT_ALIGNMENT);
		verticalBox.add(user);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalBox.add(verticalStrut);
		
		JLabel password = new JLabel("\u5BC6\u7801");
		password.setAlignmentX(Component.RIGHT_ALIGNMENT);
		verticalBox.add(password);
		
		Component verticalGlue_1 = Box.createVerticalGlue();
		verticalBox.add(verticalGlue_1);
		
		Box verticalBox_1 = Box.createVerticalBox();
		verticalBox_1.setBorder(new EmptyBorder(0, 20, 0, 10));
		login.add(verticalBox_1, BorderLayout.CENTER);
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		verticalBox_1.add(verticalStrut_2);
		
		textField = new JTextField();
		verticalBox_1.add(textField);
		textField.setColumns(10);
		
		Component verticalStrut_3 = Box.createVerticalStrut(15);
		verticalBox_1.add(verticalStrut_3);
		
		passwordField = new JPasswordField();
		verticalBox_1.add(passwordField);	
		
		Box horizontalBox_2 = Box.createHorizontalBox();
		verticalBox_1.add(horizontalBox_2);
		
		final JCheckBox remenberpass = new JCheckBox("\u8BB0\u4F4F\u5BC6\u7801");
		horizontalBox_2.add(remenberpass);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		horizontalBox_2.add(horizontalGlue_1);
		
		final JCheckBox autologin = new JCheckBox("\u81EA\u52A8\u767B\u5F55");
		horizontalBox_2.add(autologin);
		
		Box horizontalBox = Box.createHorizontalBox();
		login.add(horizontalBox, BorderLayout.SOUTH);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue);
		JButton loginButton = new JButton("\u767B\u5F55");
		loginButton.setPreferredSize(new Dimension(210, 30));
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Properties ini=new Properties();
				
				String ID=textField.getText().trim(),password=String.valueOf(passwordField.getPassword());
				
				if(!ID.equals("")&&!password.equals(""))
				{	
					ini.setProperty("userID",ID);
					if(autologin.isSelected())
					{
						remenberpass.setSelected(true);
						ini.setProperty("autologin","true");
					}
					else
						ini.setProperty("autologin","false");
					if(remenberpass.isSelected())
					{
						ini.setProperty("password",password);
					}
					else
						ini.setProperty("password","");
					
						try {
							ini.store(new FileOutputStream(".\\login.ini"),"login");
						} catch (IOException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
							LoginServer();//登录
				}
			}
		});
		
		Component horizontalStrut = Box.createHorizontalStrut(55);
		horizontalBox.add(horizontalStrut);
		loginButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		horizontalBox.add(loginButton);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(55);
		horizontalBox.add(horizontalStrut_2);
		
		final JLabel set = new JLabel("");
		set.addMouseListener(new MouseListener()
				{

					@Override
					public void mouseClicked(MouseEvent arg0) {
						// TODO 自动生成的方法存根
						String ip=JOptionPane.showInputDialog("请输入服务器IP：");
						Properties ini=new Properties();
						MyClient.IP=ip;
						ini.setProperty("IP",ip);
						String port=JOptionPane.showInputDialog("请输入服务器port：");
						MyClient.PORT=Integer.valueOf(port);
						ini.setProperty("PORT",port);
						try {
							ini.store(new FileOutputStream(".\\ipAndport.ini"),"ip and port");
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO 自动生成的方法存根
						set.setIcon(new ImageIcon(".\\set2.gif"));
					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO 自动生成的方法存根
						set.setIcon(new ImageIcon(".\\set.gif"));
					}

					@Override
					public void mousePressed(MouseEvent e) {
						// TODO 自动生成的方法存根
						
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO 自动生成的方法存根
						
					}

				});
		set.setIcon(new ImageIcon(".\\set.gif"));
		horizontalBox.add(set);
		
		Box verticalBox_2 = Box.createVerticalBox();
		verticalBox_2.setBorder(new EmptyBorder(0, 0, 0, 20));
		login.add(verticalBox_2, BorderLayout.EAST);
		
		Component verticalGlue_2 = Box.createVerticalGlue();
		verticalBox_2.add(verticalGlue_2);
		
		final JLabel AddUser = new JLabel("\u6CE8\u518C\u8D26\u6237");
		AddUser.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseClicked(MouseEvent arg0) {
				new ClientAddUser(clientChannel);
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				AddUser.setForeground(new Color(0, 153, 255));
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO 自动生成的方法存根
				AddUser.setForeground(new Color(0, 51, 255));
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO 自动生成的方法存根
				
			}
			
		});
		AddUser.setForeground(new Color(0, 51, 255));
		verticalBox_2.add(AddUser);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		verticalBox_2.add(verticalStrut_1);
		
		JLabel ResetPassword = new JLabel("\u5FD8\u8BB0\u5BC6\u7801");
		ResetPassword.setForeground(new Color(0, 51, 255));
		verticalBox_2.add(ResetPassword);
		
		Component verticalGlue_3 = Box.createVerticalGlue();
		verticalBox_2.add(verticalGlue_3);
		/*服务器连接*/ClientConnect(MyClient.IP,MyClient.PORT);
		
		
		/*自动登录设置*/
		Properties ini=new Properties();
		try {
			ini.load(new FileInputStream(".\\login.ini"));
			textField.setText(ini.getProperty("userID"));
			passwordField.setText(ini.getProperty("password"));
			if(ini.getProperty("autologin").equals("true"))
			{
				remenberpass.setSelected(true);
				autologin.setSelected(true);
				String ID=textField.getText().trim(),password1=String.valueOf(passwordField.getPassword());
				
				if(!ID.equals("")&&!password1.equals(""))
				{
					LoginServer();
				}
			}
			else
				if(!ini.getProperty("password").equals(""))
					remenberpass.setSelected(true);
			}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} 
		catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		
		
		
		
		
		
		SwingUtilities.updateComponentTreeUI(getContentPane());
				
		
	}

}
