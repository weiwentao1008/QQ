package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.Font;

public class SystemMessage extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public SystemMessage(String message) {
		this.setSize(350,200);
		this.setTitle("系统消息");
		this.setResizable(false);
		this.setIconImage(Toolkit.getDefaultToolkit().createImage(".\\QQbig2.png"));
		double x=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double y=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setLocation((int)x-350-5, (int)y-200-45);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("楷体", Font.BOLD, 30));
		textArea.setEditable(false);
		textArea.setText(message);
		contentPanel.add(textArea, BorderLayout.CENTER);
		this.setVisible(true);
		new Thread()
		{
			public void run()
			{
				try {
					sleep(10000);
					dispose();
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}.start();
		
	}

}
