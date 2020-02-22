package com.lirui.blackdog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class InterChatActivity extends Activity {
	private TextView receivText;
	private TextView enterText;
	private Button sentButton;
	private Handler inHandler;
	private Handler outHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interchat);
		receivText = (TextView) findViewById(R.id.receivText);
		enterText = (TextView) findViewById(R.id.enterText);
		sentButton=(Button)findViewById(R.id.sentButton);
		ButtonListener buttonListener = new ButtonListener();
		sentButton.setOnClickListener(buttonListener);
		inHandler = new inHandler();
		Intent intent = getIntent();
		String tempString = intent
				.getStringExtra("com.lirui.blackdog.intent.string");
		receivText.setText( tempString);
		int k=1;
		new Thread(new MainThread(tempString),"MainThread"+k++).start();
		
		
		
		
	}
	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Message sentMsg = outHandler.obtainMessage();
			sentMsg.obj = (String) enterText.getText().toString();
			outHandler.sendMessage(sentMsg);
			enterText.setText("");
			 
		}}
	class inHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {

			String s = (String) msg.obj;
			System.out.println("inHandler---->" +s);
	        receivText.setText(s);
		}
	}
	
	class MainThread  extends Thread{
        int i=1;
        int j=1;
		Socket s;
		String ip="111.59.184.58";
       public MainThread(String ip){
			this.ip=ip;
		}
		@Override
		public void run() {
			try {
				System.out.println(Thread.currentThread().getName()+"-ID-"+Thread.currentThread().getId()+"����-------->");
				s = new Socket(ip,8026);
				System.out.println("MainThread socket ��������");
				new Thread(new ClientRecievThread(s),"�ͻ���--�����߳�"+i++).start();
				new Thread(new ClientSentThread(s),"�ͻ���--�����߳�"+j++).start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	class ClientRecievThread implements Runnable 
	{
		private Socket s;
		BufferedReader br=null;
		public ClientRecievThread(Socket s)
		throws IOException
		{
		this.s=s;
		br= new BufferedReader(new InputStreamReader(s.getInputStream()));
		}
		public void run(){
			
			try{
				String content=null;
				while((content=br.readLine())!=null)
				{
					System.out.println(Thread.currentThread().getName()+"-ID-"+Thread.currentThread().getId()+"------���յ���Ϣ---->" +content);
					Message msg = inHandler.obtainMessage();
					msg.obj = content;
					inHandler.sendMessage(msg);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			}
	}
	
	class ClientSentThread implements Runnable 
	{
		private Socket s;
		PrintWriter pw=null;
		public ClientSentThread (Socket s)
		throws IOException
		{
		this.s=s;
		pw=new PrintWriter(
				s.getOutputStream(), true);;
		}
		 
		public void run()
		{
			Looper.prepare();
			outHandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					String t = (String) msg.obj;
					System.out.println(Thread.currentThread().getName()+"-ID-"+Thread.currentThread().getId()+" outclientHandler ����");
//					while (t!=null)
//					{	
						pw.println(t);// ���͸�������
						pw.flush();// ��ջ���
						System.out.println(Thread.currentThread().getName()+"-ID-"+Thread.currentThread().getId()+"��������Ϣ------->"+t);
//					}
				}
			};
			Looper.loop();
			
			
			}
}	
	
	
	
	
	
	
	
}
