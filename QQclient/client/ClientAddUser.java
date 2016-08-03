package client;
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.Box;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import javax.swing.JTextField;
import javax.swing.JPasswordField;


public class ClientAddUser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	 JTextField name;
	 JPasswordField password1;
	 JPasswordField password2;
	 JTextField agetext;
	 JTextField sextext;
	 JTextField addresstext;
	 private AsynchronousSocketChannel clientChannel;

	/**
	 * Create the dialog.
	 */
	public ClientAddUser(final AsynchronousSocketChannel clientChannel) {
		this.clientChannel=clientChannel;
		//super(jf);
		this.setVisible(true);
		super.setTitle("注册账号");
		setBounds(100, 100, 280, 510);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			Box verticalBox = Box.createVerticalBox();
			verticalBox.setBorder(new EmptyBorder(0, 10, 0, 0));
			contentPanel.add(verticalBox, BorderLayout.WEST);
			{
				Component verticalGlue = Box.createVerticalGlue();
				verticalBox.add(verticalGlue);
			}
			{
				JLabel Name = new JLabel("\u6635\u79F0*");
				Name.setAlignmentY(Component.BOTTOM_ALIGNMENT);
				Name.setAlignmentX(Component.RIGHT_ALIGNMENT);
				verticalBox.add(Name);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(20);
				verticalBox.add(verticalStrut);
			}
			{
				JLabel password1 = new JLabel("\u5BC6\u7801*");
				password1.setAlignmentY(Component.BOTTOM_ALIGNMENT);
				password1.setAlignmentX(Component.RIGHT_ALIGNMENT);
				verticalBox.add(password1);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(20);
				verticalBox.add(verticalStrut);
			}
			{
				JLabel password2 = new JLabel("\u786E\u8BA4\u5BC6\u7801*");
				password2.setAlignmentY(Component.BOTTOM_ALIGNMENT);
				password2.setAlignmentX(Component.RIGHT_ALIGNMENT);
				verticalBox.add(password2);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(20);
				verticalBox.add(verticalStrut);
			}
			{
				JLabel age = new JLabel("\u5E74\u9F84");
				age.setAlignmentY(Component.BOTTOM_ALIGNMENT);
				age.setAlignmentX(Component.RIGHT_ALIGNMENT);
				verticalBox.add(age);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(20);
				verticalBox.add(verticalStrut);
			}
			{
				JLabel sex = new JLabel("\u6027\u522B*");
				sex.setAlignmentY(Component.BOTTOM_ALIGNMENT);
				sex.setAlignmentX(Component.RIGHT_ALIGNMENT);
				verticalBox.add(sex);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(20);
				verticalBox.add(verticalStrut);
			}
			{
				JLabel address = new JLabel("\u5730\u5740");
				address.setAlignmentY(Component.BOTTOM_ALIGNMENT);
				address.setAlignmentX(Component.RIGHT_ALIGNMENT);
				verticalBox.add(address);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(30);
				verticalBox.add(verticalStrut);
			}
			{
				JLabel picture = new JLabel("\u5934\u50CF");
				picture.setAlignmentY(Component.BOTTOM_ALIGNMENT);
				picture.setAlignmentX(Component.RIGHT_ALIGNMENT);
				verticalBox.add(picture);
			}
			{
				Component verticalGlue = Box.createVerticalGlue();
				verticalBox.add(verticalGlue);
			}
		}
		{
			Box verticalBox = Box.createVerticalBox();
			verticalBox.setBorder(new EmptyBorder(0, 10, 0, 30));
			contentPanel.add(verticalBox, BorderLayout.CENTER);
			{
				Component verticalStrut = Box.createVerticalStrut(65);
				verticalBox.add(verticalStrut);
			}
			{
				name = new JTextField();
				name.setAlignmentY(Component.TOP_ALIGNMENT);
				verticalBox.add(name);
				name.setColumns(10);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(15);
				verticalBox.add(verticalStrut);
			}
			{
				password1 = new JPasswordField();
				password1.setAlignmentY(Component.TOP_ALIGNMENT);
				verticalBox.add(password1);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(15);
				verticalBox.add(verticalStrut);
			}
			{
				password2 = new JPasswordField();
				password2.setAlignmentY(Component.TOP_ALIGNMENT);
				verticalBox.add(password2);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(15);
				verticalBox.add(verticalStrut);
			}
			{
				agetext = new JTextField();
				agetext.setAlignmentY(Component.TOP_ALIGNMENT);
				verticalBox.add(agetext);
				agetext.setColumns(10);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(15);
				verticalBox.add(verticalStrut);
			}
			{
				sextext = new JTextField();
				sextext.setAlignmentY(Component.TOP_ALIGNMENT);
				verticalBox.add(sextext);
				sextext.setColumns(10);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(15);
				verticalBox.add(verticalStrut);
			}
			{
				addresstext = new JTextField();
				addresstext.setAlignmentY(Component.TOP_ALIGNMENT);
				verticalBox.add(addresstext);
				addresstext.setColumns(10);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(20);
				verticalBox.add(verticalStrut);
			}
			{
				JButton upPicture = new JButton("\u4E0A\u4F20\u56FE\u7247");
				upPicture.setAlignmentX(Component.CENTER_ALIGNMENT);
				verticalBox.add(upPicture);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(75);
				verticalBox.add(verticalStrut);
			}
		}
		{
			Box horizontalBox = Box.createHorizontalBox();
			contentPanel.add(horizontalBox, BorderLayout.SOUTH);
			{
				Component horizontalGlue = Box.createHorizontalGlue();
				horizontalBox.add(horizontalGlue);
			}
			{
				JButton okButton = new JButton("\u63D0\u4EA4");
				okButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
				okButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent a)
					{
						try {
							String na=name.getText().trim();
							String pass1=String.valueOf(password1.getPassword());
							String pass2=String.valueOf(password2.getPassword());
							String age=agetext.getText().trim();
							String sex=sextext.getText().trim();
							String address=addresstext.getText().trim();
							
							
							if(na.equals(""))
								JOptionPane.showMessageDialog(null,"昵称不能为空，请输入","错误",JOptionPane.ERROR_MESSAGE);
							if(agetext.getText().trim().equals(""))
								age="0";
							if(sextext.getText().trim().equals(""))
								JOptionPane.showMessageDialog(null,"性别不能为空，请输入","错误",JOptionPane.ERROR_MESSAGE);
							if(addresstext.getText().trim().equals(""))
								address="无";
							if(!pass1.equals(pass2))
									JOptionPane.showMessageDialog(null,"两次密码不相同，请重新输入","错误",JOptionPane.ERROR_MESSAGE);
								else
								clientChannel.write(ByteBuffer.wrap((MyProtocol.ADDUSER_MESSAGE+MyProtocol.ADDUSER_NAME_MESSAGE+na
										+MyProtocol.ADDUSER_PASSWORD_MESSAGE+pass1+MyProtocol.ADDUSER_AGE_MESSAGE+age+MyProtocol.ADDUSER_SEX_MESSAGE
										+sex+MyProtocol.ADDUSER_ADDRESS_MESSAGE+address).getBytes("utf-8"))).get();
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
										if(content.substring(0, 5).equals(MyProtocol.ADDUSER_SUCCESS_MESSAGE))
											{
											JOptionPane.showMessageDialog(null,"注册成功，账号  "+content.substring(5),"成功",JOptionPane.INFORMATION_MESSAGE);
											dispose();
											}
										else
											JOptionPane.showMessageDialog(null,"注册失败,请重试","失败",JOptionPane.ERROR_MESSAGE);
										
										buff.clear();
									}
									@Override
									public void failed(Throwable ex, Object attachment)
									{
										System.out.println("读取数据失败: " + ex);
									}
								});
							
					}
				});
				horizontalBox.add(okButton);
			}
			{
				Component horizontalStrut = Box.createHorizontalStrut(20);
				horizontalBox.add(horizontalStrut);
			}
			{
				JButton cancelButton = new JButton("\u53D6\u6D88");
				cancelButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent a)
					{
						dispose();
					}
				});
				cancelButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
				horizontalBox.add(cancelButton);
			}
			{
				Component horizontalGlue = Box.createHorizontalGlue();
				horizontalBox.add(horizontalGlue);
			}
		}
		SwingUtilities.updateComponentTreeUI(getContentPane());
	}

}
