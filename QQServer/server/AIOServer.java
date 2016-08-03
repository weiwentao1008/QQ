package server;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.concurrent.*;

import static java.lang.System.*;

public class AIOServer
{
	static final int PORT = 30000;
	final static String UTF_8 = "utf-8";
	static ExecutorService executor;
	static AsynchronousChannelGroup channelGroup;
	static AsynchronousServerSocketChannel serverChannel;
	static Map<String,AsynchronousSocketChannel> channelMap= new HashMap<>();
	public static String getTime()
	{
		Calendar a=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy��MM��dd��HHʱmm��ss��");
		
		return sdf.format(a.getTime());
	}
	public void startListen() throws InterruptedException,Exception
	{
		executor = Executors.newFixedThreadPool(5);
		channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
		serverChannel= AsynchronousServerSocketChannel.open(channelGroup).bind(new InetSocketAddress(PORT));
		/*�����û�����״̬����*/
		new Thread()
		{
			public void run()
			{
				try {
				Statement st = SwingFrameStart.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
					ResultSet rs=st.executeQuery("SELECT ID,����״̬   FROM qq WHERE ����״̬ = '����'  OR ����״̬ = '����' AND Model ='RUN';");
					
				} catch (SQLException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}//������'����'
			}
		}.start();
		
		serverChannel.accept(null, new AcceptHandler(serverChannel));  
			Thread.sleep(5000);
	}
	public static void stopListen() throws IOException
	{
		serverChannel.close();
		channelGroup.shutdownNow();;
		executor.shutdown();
	}
	
	
	
}
class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Object>
{
	private AsynchronousServerSocketChannel serverChannel;
	public AcceptHandler(AsynchronousServerSocketChannel sc)
	{
		this.serverChannel = sc;
	}
	ByteBuffer buff = ByteBuffer.allocate(1024);
	
	/*ת��������Ϣ����*/
	private void ForwardInformation(String m) 
	{
		String ID;
		String friend_id;
		m=m.substring(5);
		int n=m.indexOf(MyProtocol.ID_MESSAGE);
		friend_id=m.substring(0,n);
		m=m.substring(n+5);
		n=m.indexOf(MyProtocol.MESSAGE_START);
		ID=m.substring(0,n);
		m=m.substring(n+5);
		n=m.indexOf(MyProtocol.MESSAGE_END);
		String message=m.substring(0,n);
		m=m.substring(n+5);
		AsynchronousSocketChannel send=AIOServer.channelMap.get(friend_id);
		if(send==null)
		{
			System.out.println(friend_id+"δ����");
			return ;
		}
		try {
			send.write(ByteBuffer.wrap((MyProtocol.PRIVATE_MESSAGE+MyProtocol.ID_MESSAGE+ID
										+MyProtocol.MESSAGE_START+message+MyProtocol.MESSAGE_END).getBytes(AIOServer.UTF_8))).get();
		} catch (UnsupportedEncodingException | InterruptedException
				| ExecutionException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	
	
	
	/*�������ϵͳ��Ϣ*/
	private void PrintSystemMessage(String message)
	{
		out.println("ps");
		Iterator<String> foreach=AIOServer.channelMap.keySet().iterator();
		while(foreach.hasNext())
		{
			String id=foreach.next();
			try
			{
				if(id.equals(SystemSendMessage.Admin))
					SecondWindows.textArea.append("ϵͳ��Ϣ:"+message+"\n");
				else
				AIOServer.channelMap.get(id).write(ByteBuffer.wrap((MyProtocol.SYSTEM_MESSAGE+message).getBytes(AIOServer.UTF_8))).get();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	/*��½��Ϣ������*/
	private void LoginServer(String message,AsynchronousSocketChannel c) 
	{
		out.println(message);
		int n=message.indexOf(MyProtocol.PASSWORD_MASSAGE);
		String ID=message.substring(0, n);
		message=message.substring(n+5);
		n=message.indexOf(MyProtocol.MESSAGE_END);
		String PassWord=message.substring(0,n);
		message=message.substring(n+5);
		Statement st;
		
		AsynchronousSocketChannel willRemoveChannel=AIOServer.channelMap.get(ID);
		System.out.println("willRemoveChannel:"+willRemoveChannel);
		if(willRemoveChannel!=null)
		{
			AIOServer.channelMap.remove(ID);
			try {
				willRemoveChannel.write(ByteBuffer.wrap((MyProtocol.USE_REPEAT_LOGIN_MESSAGE+MyProtocol.MESSAGE_END).getBytes(AIOServer.UTF_8))).get();
			} catch (UnsupportedEncodingException | InterruptedException
					| ExecutionException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			return ;
		}
				
				
				try {
					st = SwingFrameStart.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
				
				ResultSet rs=st.executeQuery("SELECT ID,Model,����,����, ����״̬   FROM qq WHERE ID = "+ID+" AND Model ='RUN';");//������'����'
				rs.first();
				out.println("�û�"+rs.getRow());
				if(rs.getRow()==0)
				{
					c.write(ByteBuffer.wrap((MyProtocol.LOGIN_ERROR_MESSAGE+0+MyProtocol.MESSAGE_END).getBytes(AIOServer.UTF_8))).get();
					return ;
				}
				
				if(rs.getString("����")==null)
					c.write(ByteBuffer.wrap((MyProtocol.LOGIN_ERROR_MESSAGE+0+MyProtocol.MESSAGE_END).getBytes(AIOServer.UTF_8))).get();
				else
					if(rs.getString("����").equals(PassWord))
					{
						out.println(ID+"   login ok");
						AIOServer.channelMap.put(ID, c);
						rs.updateString("����״̬","����");
						rs.updateRow();
						String time=AIOServer.getTime();
						SecondWindows.textArea.append(" ["+time+"]  "+rs.getString("����")+"( "+ID+" ) ������\r\n");
						c.write(ByteBuffer.wrap((MyProtocol.LOGIN_SUCCESS_MESSAGE+MyProtocol.MESSAGE_END).getBytes(AIOServer.UTF_8))).get();
					}
					else
						c.write(ByteBuffer.wrap((MyProtocol.LOGIN_ERROR_MESSAGE+1+MyProtocol.MESSAGE_END).getBytes(AIOServer.UTF_8))).get();
					rs.close();
						st.close();
					} 
				catch (SQLException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
	}
	/*ע���û���Ϣ����*/
	private void AddUser(String message,AsynchronousSocketChannel c)
	{
		message=message.substring(5);
		out.println(message);
		int n=message.indexOf(MyProtocol.ADDUSER_PASSWORD_MESSAGE);
		String name=message.substring(0,n);
		message=message.substring(n+5);
		out.println(message);
		n=message.indexOf(MyProtocol.ADDUSER_AGE_MESSAGE);
		String password=message.substring(0,n);
		message=message.substring(n+5);
		out.println(message);
		n=message.indexOf(MyProtocol.ADDUSER_SEX_MESSAGE);
		String age=message.substring(0,n);
		message=message.substring(n+5);
		out.println(message);
		n=message.indexOf(MyProtocol.ADDUSER_ADDRESS_MESSAGE);
		String sex=message.substring(0,n);
		message=message.substring(n+5);
		out.println(message);
		String address=message;
		
		Integer ID;
		
		int colnumber=0;
		try {
			Statement st=SwingFrameStart.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		
		ResultSet rs=st.executeQuery("SELECT * FROM `qq` WHERE  Model = 'STOP' ;");
		while(rs.next())
			colnumber++;
		if(colnumber==0)
		{
			rs=st.executeQuery("SELECT * FROM qq;");
			rs.moveToInsertRow();
		}
		else
		{
			rs.first();
			ID=rs.getInt(1);
			rs.deleteRow();
			rs.moveToInsertRow();
			rs.updateInt(1,ID);
		}
		
		
			rs.updateString("����",name);
		
		rs.updateString("����",password);
		rs.updateString("Model","RUN");
		rs.updateString("�Ա�",sex);
		rs.updateString("��ַ",address);
		rs.updateString("����",age);
		/*Ԥ��age��sex��picture��û�������ݿ�*/
		rs.insertRow();
		rs.last();
		ID=rs.getInt("ID");
		rs.close();
		String time=AIOServer.getTime();
		SecondWindows.textArea.append(" ["+time+"]  "+name+"( "+ID+" ) ע��ɹ�\r\n");
		c.write(ByteBuffer.wrap((MyProtocol.ADDUSER_SUCCESS_MESSAGE+ID).getBytes(AIOServer.UTF_8))).get();
		
		
		} catch (SQLException e) 
		{
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	
	/*���ɺ����б����Ϣ����*/
	private void FindFrient(String message,AsynchronousSocketChannel c)
	{
		int n=message.indexOf(MyProtocol.MESSAGE_END);
		String ID=message.substring(0,n);
		message=message.substring(n+5);
		
		Statement st_frient,st_message;
		try {
			st_frient = SwingFrameStart.conn.createStatement(ResultSet.CONCUR_READ_ONLY,ResultSet.TYPE_FORWARD_ONLY);
			st_message= SwingFrameStart.conn.createStatement(ResultSet.CONCUR_READ_ONLY,ResultSet.TYPE_FORWARD_ONLY);
			ResultSet rs_frient=st_frient.executeQuery("SELECT * FROM qq_frient WHERE ID="+ID+";");
			while(rs_frient.next())
			{
				ResultSet rs_message=st_message.executeQuery("SELECT  ID,����,�Ա�,��ַ,����,����״̬,ͷ��   FROM qq WHERE ID="+rs_frient.getString("ID_frient")+" AND Model='RUN' ;");
				rs_message.first();
				if(rs_message.getRow()==0)
					continue;
				c.write(ByteBuffer.wrap((MyProtocol.PERSON_MESSAGE+MyProtocol.RUN_MESSAGE+rs_message.getString("����״̬")+MyProtocol.PICTURE_MESSAGE+rs_message.getString("ͷ��")
											+MyProtocol.ID_MESSAGE+rs_message.getInt("ID")+MyProtocol.NAME_MESSAGE+rs_message.getString("����")
											+MyProtocol.AGE_MESSAGE+rs_message.getString("����")+MyProtocol.SEX_MESSAGE+rs_message.getString("�Ա�")
											+MyProtocol.ADDRESS_MESSAGE+rs_message.getString("��ַ")+MyProtocol.MESSAGE_END).getBytes(AIOServer.UTF_8))).get();
				rs_message.close();
			}
			st_message.close();
			rs_frient.close();
			st_frient.close();
			
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
	}
	
	/* ���ɸ�����Ϣ���� */
	private void MyPersonMessage(String message,AsynchronousSocketChannel c)
	{
		int n=message.indexOf(MyProtocol.MESSAGE_END);
		String ID=message.substring(0,n);
		message=message.substring(n+5);
		Statement st;
		try {
			st= SwingFrameStart.conn.createStatement(ResultSet.CONCUR_READ_ONLY,ResultSet.TYPE_FORWARD_ONLY);
			ResultSet rs=st.executeQuery("SELECT ID,����,�Ա�,��ַ,����,����״̬,ͷ��   FROM qq WHERE ID="+ID+";");
			rs.first();
				c.write(ByteBuffer.wrap((MyProtocol.MY_PERSON_MESSAGE+MyProtocol.PICTURE_MESSAGE+rs.getString("ͷ��")
										+MyProtocol.NAME_MESSAGE+rs.getString("����")+MyProtocol.AGE_MESSAGE+rs.getString("����")
										+MyProtocol.SEX_MESSAGE+rs.getString("�Ա�")+MyProtocol.ADDRESS_MESSAGE+rs.getString("��ַ")+MyProtocol.MESSAGE_END).getBytes(AIOServer.UTF_8))).get();
		} catch (UnsupportedEncodingException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	
	/*ת������״̬��Ϣ*/
	private void ForwardRunInformation(AsynchronousSocketChannel sc,String m) 
	{
		String ID;
		m=m.substring(5);
		int n=m.indexOf(MyProtocol.RUN_MESSAGE);
		ID=m.substring(0,n);
		m=m.substring(n+5);
		n=m.indexOf(MyProtocol.MESSAGE_END);
		String state=m.substring(0,n);
		m=m.substring(n+5);
		out.println(ID+" "+state);
		try {
			Statement st;
		st = SwingFrameStart.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		ResultSet rs=st.executeQuery("SELECT ID,����״̬  FROM qq WHERE ID="+ID+";");
		rs.first();
		rs.updateString("����״̬",state);
		rs.updateRow();
		rs.close();
		st.close();
		if(state.equals("����"))
			AIOServer.channelMap.remove(ID);
		else
			AIOServer.channelMap.put(ID, sc);
		if(!state.equals("����"))
			state="����";
		
		
			st = SwingFrameStart.conn.createStatement(ResultSet.CONCUR_READ_ONLY,ResultSet.TYPE_FORWARD_ONLY);
		
		rs=st.executeQuery("SELECT * FROM qq_frient WHERE ID="+ID+";");
		AsynchronousSocketChannel send;
		while(rs.next())
		{
			send=AIOServer.channelMap.get(rs.getString("ID_frient"));
			if(send==null)
			{
				System.out.println(rs.getString("ID_frient")+"δ����");
				continue;
			}
			try {
				send.write(ByteBuffer.wrap((MyProtocol.RUN_STATE_MESSAGE+MyProtocol.ID_MESSAGE+ID
											+MyProtocol.RUN_MESSAGE+state+MyProtocol.MESSAGE_END).getBytes(AIOServer.UTF_8))).get();
			} catch (UnsupportedEncodingException | InterruptedException
					| ExecutionException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		rs.close();
		st.close();
		
		} catch (SQLException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}	
	}
	/*�û�������Ϣ����*/
	private void ClosingClientUser(String m)
	{
		String state;
		String id;
		m=m.substring(5);
		int n=m.indexOf(MyProtocol.RUN_STATE_MESSAGE);
		id=m.substring(0,n);
		m=m.substring(n+5);
		n=m.indexOf(MyProtocol.MESSAGE_END);
		state=m.substring(0,n);
		m=m.substring(n+5);
		AIOServer.channelMap.remove(id);
		
		Statement st;
		try {
			st = SwingFrameStart.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet rs=st.executeQuery("SELECT ID,����,����״̬  FROM qq WHERE ID="+id+";");
			rs.first();
			String time=AIOServer.getTime();
			SecondWindows.textArea.append(" ["+time+"]  "+rs.getString("����")+"( "+id+" ) ������\r\n");
			rs.updateString("����״̬",state);
			rs.updateRow();
			rs.close();
			st.close();
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void completed(final AsynchronousSocketChannel sc, Object attachment)
	{
		serverChannel.accept(null , this);
		sc.read(buff , null, new CompletionHandler<Integer,Object>()  
		{
			@Override
			public void completed(Integer result, Object attachment)
			{
				buff.flip();
				String content = StandardCharsets.UTF_8.decode(buff).toString();
				String message=content.substring(5);
				out.println(content.substring(0, 5));
				switch(content.substring(0, 5))
				{
					
				case MyProtocol.SYSTEM_MESSAGE:PrintSystemMessage(message);break;
				case MyProtocol.LOGIN_MESSAGE:LoginServer(message,sc);break;
				case MyProtocol.MY_FRIENT_MESSAGE:FindFrient(message,sc);break;
				case MyProtocol.ADDUSER_MESSAGE:AddUser(message,sc);break;
				case MyProtocol.MY_PERSON_MESSAGE:MyPersonMessage(message,sc);break;
				case MyProtocol.PRIVATE_MESSAGE:ForwardInformation(message);break;
				case MyProtocol.RUN_STATE_MESSAGE:ForwardRunInformation(sc,message);break;
				case MyProtocol.CLOSING_MESSAGE:ClosingClientUser(message);break;
				}
					
				
				buff.clear();
				sc.read(buff , null , this);
			}
			@Override
			public void failed(Throwable ex, Object attachment)
			{
				System.out.println("��ȡ����ʧ��: " + ex);
				Object temp=null;
				for(Object c : AIOServer.channelMap.keySet())
				{
						if(AIOServer.channelMap.get(c).equals(sc))
							temp=c;
				}
				if(temp!=null)
				{
					//String message=MyProtocol.ID_MESSAGE+temp+MyProtocol.RUN_MESSAGE+"����"+MyProtocol.MESSAGE_END;
					//ForwardRunInformation(sc,message);
					AIOServer.channelMap.remove(temp);
				}
			}
		});
	}
	@Override
	public void failed(Throwable ex, Object attachment)
	{
		System.out.println("����ʧ��: " + ex);
	}
}