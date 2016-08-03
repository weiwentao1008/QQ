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
			temp="注册账号";
		if(Model==2)
			temp="删除账号";
		if(Model==3)
			temp="修改账号";
		JOptionPane.showMessageDialog(null,temp+":"+ID, "成功", JOptionPane.INFORMATION_MESSAGE);
		Calendar a=Calendar.getInstance();
		String time=""+a.get(Calendar.YEAR)+"年"+(a.get(Calendar.MONTH)+1)+"月"+a.get(Calendar.DAY_OF_MONTH)+"日"+a.get(Calendar.HOUR_OF_DAY)+"时"+a.get(Calendar.MINUTE)+"分";
		SecondWindows.HistoryText.append("["+time+"]"+temp+":"+na+"("+ID+")"+temp+"\r\n");
	}
}
