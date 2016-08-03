package server;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.concurrent.*;

import javax.swing.text.AbstractDocument.Content;
import javax.swing.text.html.parser.ContentModel;

import static java.lang.System.*;
public class SystemSendMessage {

	final static String Admin="10000";
	final static String AdminPassword="123456";
	final static String UTF_8 = "utf-8";
	final static int PORT = 30000;
	// ���������ͨ�ŵ��첽Channel
	static AsynchronousSocketChannel clientChannel;
	//JFrame mainWin = new JFrame("��������");
	//JTextArea jta = new JTextArea(16 , 48);
	//JTextField jtf = new JTextField(40);
	//JButton sendBn = new JButton("����");
	/*public void init()
	{
		mainWin.setLayout(new BorderLayout());
		jta.setEditable(false);
		mainWin.add(new JScrollPane(jta), BorderLayout.CENTER);
		JPanel jp = new JPanel();
		jp.add(jtf);
		jp.add(sendBn);
		// ������Ϣ��Action,Action��ActionListener���ӽӿ�
		Action sendAction = new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				String content = jtf.getText();
				if (content.trim().length() > 0)
				{
					try
					{
						// ��content����д��Channel��
						clientChannel.write(ByteBuffer.wrap(content
							.trim().getBytes(UTF_8))).get();    //��
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
				// ��������
				jtf.setText("");
			}
		};
		sendBn.addActionListener(sendAction);
		// ��Ctrl+Enter����"send"����
		jtf.getInputMap().put(KeyStroke.getKeyStroke('\n'
			, java.awt.event.InputEvent.CTRL_MASK) , "send");
		// ��"send"��sendAction����
		jtf.getActionMap().put("send", sendAction);
		mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWin.add(jp , BorderLayout.SOUTH);
		mainWin.pack();
		mainWin.setVisible(true);
	}*/
	
	final ByteBuffer buff = ByteBuffer.allocate(1024);
	
	ExecutorService executor = Executors.newFixedThreadPool(1);
	
	AsynchronousChannelGroup channelGroup ;
	
	/*����Ա��½*/
	public void AdministroterLogin()throws Exception
	{
		channelGroup =AsynchronousChannelGroup.withThreadPool(executor);
		
		clientChannel = AsynchronousSocketChannel.open(channelGroup);
		
		clientChannel.connect(new InetSocketAddress("127.0.0.1", PORT)).get();
		
		SecondWindows.textArea.append("---����������ӳɹ�---\n");
		
		clientChannel.write(ByteBuffer.wrap((MyProtocol.LOGIN_MESSAGE+Admin+MyProtocol.PASSWORD_MASSAGE
												+AdminPassword+MyProtocol.MESSAGE_END).getBytes("utf-8"))).get();
		
		buff.clear();
		clientChannel.read(buff, null, new CompletionHandler<Integer,Object>() 
		{
			@Override
			public void completed(Integer result, Object attachment)
			{
				buff.flip();
				String content = StandardCharsets.UTF_8.decode(buff).toString();
				
				if(content.substring(0,5).equals(MyProtocol.LOGIN_SUCCESS_MESSAGE))
					SecondWindows.textArea.append("����Ա�˺ŵ�½�ɹ�\n");
				else
					/*if(content.substring(0, 5).equals(MyProtocol.SYSTEM_MESSAGE))
						SecondWindows.textArea.append("ϵͳ��Ϣ:"+content.substring(5)+"\n");
					else*/
				SecondWindows.textArea.append("����Ա�˺ŵ�½ʧ��\n������"+content);
				buff.clear();
				//clientChannel.read(buff , null , this);
			}
			@Override
			public void failed(Throwable ex, Object attachment)
			{
				System.out.println("��ȡ����ʧ��: " + ex);
			}
		});
	}
	
	/*ϵͳ��Ϣ����*/
	
	/*public void SystemSend()throws Exception
	{
		
		buff.clear();
		clientChannel.read(buff, null, new CompletionHandler<Integer,Object>() 
		{
			@Override
			public void completed(Integer result, Object attachment)
			{
				buff.flip();
				String content = StandardCharsets.UTF_8.decode(buff).toString();
				if(content.substring(0, 5).equals(MyProtocol.SYSTEM_MESSAGE))
					SecondWindows.textArea.append("ϵͳ��Ϣ:"+content.substring(5)+"\n");
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
	*/
	
	
}
