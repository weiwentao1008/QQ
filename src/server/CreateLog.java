package server;
import java.util.Calendar;

import javax.swing.JOptionPane;


public class CreateLog {
	static int ADD=1;
	static int DELETE=2;
	static int CHANGE=3;
	public static void InsertLog(String na,Integer ID,int Model )
	{
		String temp=null;
		if(Model==1)
			temp="ע���˺�";
		if(Model==2)
			temp="ɾ���˺�";
		if(Model==3)
			temp="�޸��˺�";
		JOptionPane.showMessageDialog(null,temp+":"+ID, "�ɹ�", JOptionPane.INFORMATION_MESSAGE);
		Calendar a=Calendar.getInstance();
		String time=""+a.get(Calendar.YEAR)+"��"+(a.get(Calendar.MONTH)+1)+"��"+a.get(Calendar.DAY_OF_MONTH)+"��"+a.get(Calendar.HOUR_OF_DAY)+"ʱ"+a.get(Calendar.MINUTE)+"��";
		SecondWindows.HistoryText.append("["+time+"]"+temp+":"+na+"("+ID+")"+temp+"\r\n");
	}
}
