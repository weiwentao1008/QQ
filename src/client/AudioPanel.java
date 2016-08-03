package client;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class AudioPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public AudioPanel() {
		setLayout(new BorderLayout());
		
		Box verticalBox = Box.createVerticalBox();
		add(verticalBox, BorderLayout.NORTH);
		
		JLabel IconImage = new JLabel("");
		IconImage.setIcon(new ImageIcon(".\\image\\audioWait.gif"));
		verticalBox.add(IconImage);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalBox.add(verticalStrut);
		
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		verticalBox.add(horizontalBox);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(55);
		horizontalBox.add(horizontalStrut_1);
		
		JButton accept = new JButton("\u63A5\u53D7");
		horizontalBox.add(accept);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalBox.add(horizontalStrut);
		
		JButton no = new JButton("\u62D2\u7EDD");
		horizontalBox.add(no);

	}

}
