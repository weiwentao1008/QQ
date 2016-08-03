package server;
import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.Box;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;


public class AddUser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	 JTextField name;
	 JPasswordField password1;
	 JPasswordField password2;
	 JTextField agetext;
	 JTextField sextext;
	 JTextField textField_2;
	 JComboBox comboBox = new JComboBox();
	/**
	 * Create the dialog.
	 */
	public AddUser(String Title,final Connection con) {
		
		//super(jf);
		
		super.setTitle(Title);
		setBounds(100, 100, 300, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			Box verticalBox = Box.createVerticalBox();
			verticalBox.setBorder(new EmptyBorder(0, 10, 0, 0));
			contentPanel.add(verticalBox, BorderLayout.WEST);
			{
				Component verticalStrut = Box.createVerticalStrut(65);
				verticalBox.add(verticalStrut);
			}
			{
				JLabel Name = new JLabel("\u6635\u79F0*");
				Name.setAlignmentX(Component.RIGHT_ALIGNMENT);
				verticalBox.add(Name);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(15);
				verticalBox.add(verticalStrut);
			}
			{
				JLabel password1 = new JLabel("\u5BC6\u7801*");
				password1.setAlignmentX(Component.RIGHT_ALIGNMENT);
				verticalBox.add(password1);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(10);
				verticalBox.add(verticalStrut);
			}
			{
				JLabel password2 = new JLabel("\u786E\u8BA4\u5BC6\u7801*");
				password2.setAlignmentX(Component.RIGHT_ALIGNMENT);
				verticalBox.add(password2);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(15);
				verticalBox.add(verticalStrut);
			}
			{
				JLabel age = new JLabel("\u5E74\u9F84");
				age.setAlignmentX(Component.RIGHT_ALIGNMENT);
				verticalBox.add(age);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(15);
				verticalBox.add(verticalStrut);
			}
			{
				JLabel sex = new JLabel("\u6027\u522B");
				sex.setAlignmentX(Component.RIGHT_ALIGNMENT);
				verticalBox.add(sex);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(15);
				verticalBox.add(verticalStrut);
			}
			{
				JLabel address = new JLabel("\u5730\u5740");
				address.setAlignmentX(Component.RIGHT_ALIGNMENT);
				verticalBox.add(address);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(40);
				verticalBox.add(verticalStrut);
			}
			{
				JLabel picture = new JLabel("\u5934\u50CF");
				picture.setAlignmentX(Component.RIGHT_ALIGNMENT);
				verticalBox.add(picture);
			}
		}
		{
			Box verticalBox = Box.createVerticalBox();
			verticalBox.setBorder(new EmptyBorder(0, 10, 0, 30));
			contentPanel.add(verticalBox, BorderLayout.CENTER);
			{
				Component verticalStrut = Box.createVerticalStrut(60);
				verticalBox.add(verticalStrut);
			}
			{
				name = new JTextField();
				name.setAlignmentY(Component.TOP_ALIGNMENT);
				verticalBox.add(name);
				name.setColumns(10);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(10);
				verticalBox.add(verticalStrut);
			}
			{
				password1 = new JPasswordField();
				password1.setAlignmentY(Component.TOP_ALIGNMENT);
				verticalBox.add(password1);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(10);
				verticalBox.add(verticalStrut);
			}
			{
				password2 = new JPasswordField();
				password2.setAlignmentY(Component.TOP_ALIGNMENT);
				verticalBox.add(password2);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(10);
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
				textField_2 = new JTextField();
				textField_2.setAlignmentY(Component.TOP_ALIGNMENT);
				verticalBox.add(textField_2);
				textField_2.setColumns(10);
			}
			{
				Component verticalStrut = Box.createVerticalStrut(15);
				verticalBox.add(verticalStrut);
			}
			{
				{
					Box horizontalBox = Box.createHorizontalBox();
					verticalBox.add(horizontalBox);
					{
						Component horizontalStrut = Box.createHorizontalStrut(40);
						horizontalBox.add(horizontalStrut);
					}
					
					for(int i=0;i<15;i++)
						comboBox.addItem(new ImageIcon(".\\head\\"+i+".gif"));
					horizontalBox.add(comboBox);
					{
						Component horizontalStrut = Box.createHorizontalStrut(40);
						horizontalBox.add(horizontalStrut);
					}
				}
			}
			{
				Component verticalStrut = Box.createVerticalStrut(60);
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
						try
						{
							
							int colnumber=0;
							Statement st=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
							
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
								Integer ID=rs.getInt(1);
								rs.deleteRow();
								rs.moveToInsertRow();
								rs.updateInt(1,ID);
							}
							if(comboBox.getSelectedIndex()>0)
								rs.updateInt("头像",comboBox.getSelectedIndex());
							else
							{
								JOptionPane.showMessageDialog(null,"请选择头像","错误",JOptionPane.ERROR_MESSAGE);
								return ;
							}
							String na=name.getText();
							rs.updateString("姓名",na);
							String pass1=String.valueOf(password1.getPassword());
							String pass2=String.valueOf(password2.getPassword());
							if(pass1.equals(pass2))
							{
								rs.updateString("密码",pass1);
								pass1=null;
								pass2=null;
							}
							
							rs.updateString("Model","RUN");
							/*预留age，sex，picture等没加入数据库*/
							rs.insertRow();
							rs.last();
							CreateLog.InsertLog(na,(Integer) rs.getObject(1),CreateLog.ADD);
							dispose();
							rs.close();
							
						}
						catch(Exception e)
						{
							e.printStackTrace(System.out);
							JOptionPane.showMessageDialog(null,"数据库读取失败","错误",JOptionPane.ERROR_MESSAGE);
						}
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
		{
			
		}
	}

}
