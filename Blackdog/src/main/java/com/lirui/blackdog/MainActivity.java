package com.lirui.blackdog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		 stoptag=true ;
		 stoptag1=true ;
		super.onResume();
	}
	private EditText remoteipText;
	private EditText idText;
	private EditText passwordText;
	private EditText nameText;
	private TextView outputText;
	private Button setipButton;
	private Button registerButton;
	private Button deleteButton;
	private Button loginButton;
	private Button floginButton;
	private Button sentButton;
	private Handler inHandler;
	private Handler outHandler;
	private Handler outsentHandler;
	private Handler outipHandler;
	public String s;
	static boolean stoptag=true ;
	static boolean stoptag1=true ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		remoteipText = (EditText) findViewById(R.id.remoteipText);
		idText = (EditText) findViewById(R.id.idText);
		passwordText = (EditText) findViewById(R.id.passwordText);
		nameText = (EditText) findViewById(R.id.nameText);
		outputText = (TextView) findViewById(R.id.outputText);
		loginButton = (Button) findViewById(R.id.loginButton);
		floginButton = (Button) findViewById(R.id.floginButton);
		sentButton = (Button) findViewById(R.id.sentButton);
		registerButton = (Button) findViewById(R.id.registerButton);
		deleteButton = (Button) findViewById(R.id.deleteButton);
		setipButton = (Button) findViewById(R.id.setipButton);
		ButtonListener buttonListener = new ButtonListener();
		loginButton.setOnClickListener(buttonListener);
		floginButton.setOnClickListener(buttonListener);
		sentButton.setOnClickListener(buttonListener);
		registerButton.setOnClickListener(buttonListener);
		deleteButton.setOnClickListener(buttonListener);
		setipButton.setOnClickListener(buttonListener);
		inHandler = new inHandler();
		ClientThread c = new ClientThread();
		c.start();
		System.out.println("onCreate stoptag=="+stoptag);
		System.out.println("onCreate stoptag1=="+stoptag1);
		IntentFilter intentfilter = new IntentFilter("BC_Two");
		broadcastreciever broadcastreciever = new broadcastreciever();
		registerReceiver(broadcastreciever, intentfilter);

	}

	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.setipButton: {
				Message ipmsg = outipHandler.obtainMessage();
				ipmsg.obj = (String) remoteipText.getText().toString();
				outipHandler.sendMessage(ipmsg);
				break;
			}
			case R.id.registerButton: {
				Message sentmsg = outHandler.obtainMessage();
				sentmsg.obj = "s" + "/" + (String) idText.getText().toString()
						+ "/" + (String) nameText.getText().toString()
						+ "/" + (String) passwordText.getText().toString();
				outHandler.sendMessage(sentmsg);
				break;
			}
			case R.id.deleteButton: {
				Message sentmsg = outHandler.obtainMessage();
				sentmsg.obj = "d" + "/" + (String) idText.getText().toString()
						+ "/" + (String) nameText.getText().toString()
						+ "/" + (String) passwordText.getText().toString();
				outHandler.sendMessage(sentmsg);
				break;
			}
			case R.id.loginButton: {
				Message sentmsg = outHandler.obtainMessage();
				sentmsg.obj = "l" + "/" + (String) idText.getText().toString()
						+ "/" + (String) nameText.getText().toString()
						+ "/" + (String) passwordText.getText().toString();
				outHandler.sendMessage(sentmsg);
				break;
			}
			case R.id.floginButton: {
				Intent fintent = new Intent();
				fintent.setClass(MainActivity.this, ChActivity.class);
				fintent.putExtra("com.lirui.blackdog.intent.string",
						(String) idText.getText().toString());
				startActivity(fintent);
				break;
			}
			case R.id.sentButton: {
				Intent wintent = new Intent();
				wintent.setClass(MainActivity.this, ChActivity.class);
				wintent.putExtra("com.lirui.blackdog.intent.string",
						(String) idText.getText().toString());
				startActivity(wintent);
				break;
			}
			}
		}
	}

	class inHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			s = (String) msg.obj;
			String user_s[] = s.split("/");
			outputText.setText(user_s[0]);

			// 数据库存储
			SQLiteDatabase clientdba = openOrCreateDatabase("clientdba.db",
					MODE_PRIVATE, null);
			clientdba.execSQL("create table if not exists clienttb(_id integer,userid text not null,username text not null,ip text not null,msg text not null)");
			clientdba.execSQL("create table if not exists chattb(_id integer,userid text not null,username text not null,msg text not null)");//+++
			clientdba.delete("clienttb", "_id>=?", new String[] { "0" });
			Cursor ct = clientdba.query("chattb", null, "_id>=?",
					new String[] { "0" }, null, null, "_id");
			if (ct.getCount()==0) {
				ContentValues values1 = new ContentValues();
				values1.clear();
				values1.put("userid", "1001");
				values1.put("_id", 1);
				values1.put("username", "习近平");
				values1.put("msg", "....");
				clientdba.insert("chattb", null, values1);
			}
			
			
			
			for (int i = 1; i < user_s.length; i++) {
				String user_ss[] = user_s[i].split("#");
				Cursor cc = clientdba.query("clienttb", null, "userid=?",
						new String[] { user_ss[0] }, null, null, "_id");
				ContentValues values = new ContentValues();
				values.clear();
				values.put("userid", user_ss[0]);
				System.out.println("user_ss[0]==" + user_ss[0]);
				System.out.println("user_ss[1]==" + user_ss[1]);
				values.put("_id", i - 1);
				values.put("username", user_ss[1]);
				values.put("ip", user_ss[2]);
				values.put("msg", "");
				System.out.println("cc==" + cc.getCount());
				if (cc.getCount() == 0) {
					System.out.println("if");
					clientdba.insert("clienttb", null, values);
				} else {
					clientdba.update("clienttb", values, "userid=?",
							new String[] { user_ss[0] });
				}
				cc.close();
			}
			Cursor cs = clientdba.query("clienttb", null, "_id>=?",
					new String[] { "0" }, null, null, "_id");

			if (cs != null) {
				String[] columns = cs.getColumnNames();
				while (cs.moveToNext()) {
					for (String columnName : columns) {
						Log.i("info",
								cs.getString(cs.getColumnIndex(columnName)));
					}
				}
				cs.close();
				clientdba.close();
			}

			if (user_s[0].equals("success")) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ChActivity.class);
				intent.putExtra("com.lirui.blackdog.intent.string",
						(String) idText.getText().toString());
				startActivity(intent);
			}
		}
	}

	class ClientThread extends Thread {
		public String IP = "192.168.1.120";
		public String t;
		public Socket client;

		public void run() {
			System.out.println("ClientThread启动");

			Looper.prepare();
			outipHandler = new Handler() {
				boolean b = true;

				@Override
				public void handleMessage(Message msg) {

					IP = (String) msg.obj;
					System.out.println("Client线程传入IP：" + IP);
					if (b) {
						b = false;
						try {
							client = new Socket(IP, 8025);// 新建一个socket
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};

			outHandler = new Handler() {
				public void handleMessage(Message msg) {
					t = (String) msg.obj;
					System.out.println("Client线程传入字符串：" + t);
					System.out.println("outhandler 运行");
					try {
						String senttext = t;
						// Integer.valueOf(DK).intValue()
						// client = new Socket(IP, 8025);// 新建一个socket
						// 从Socket获取一个输出对象，以便把EditText输入的数据发给客户端
						PrintStream ps = new PrintStream(
								client.getOutputStream());
						ps.println(ChatProtocol.USER_ROUND + senttext
								+ ChatProtocol.USER_ROUND);
						// PrintWriter socketoutput = new PrintWriter(
						// client.getOutputStream(), true);
						//
						// socketoutput.println(senttext);// 发送给服务器
						// socketoutput.flush();// 清空缓存
						System.out.println("client socket 建立连接");
						// 从客户端的socket获取一个输入的对象，以便接收来自服务器信息
						BufferedReader socketinput = new BufferedReader(
								new InputStreamReader(client.getInputStream()));
						String recievtext = socketinput.readLine();// 把接收到的内容读取出来
						// 把接收到的内容通过inrecievHandler发送给主线程
						Message cmsg = inHandler.obtainMessage();
						cmsg.obj = recievtext;
						System.out.println(recievtext);
						inHandler.sendMessage(cmsg);
						if (recievtext.split("/")[0].equals("success")) {

							new Thread(new ClientSentThread(ps), "客户端--发送线程")
									.start();

							String line = null;
							while (stoptag&&((line = socketinput.readLine()) != null)) {
//								msg.obj = line;
//								inHandler.sendMessage(msg);
								Intent intent = new Intent();
								intent.putExtra("msg", line);
								intent.setAction("BC_One");
								sendOrderedBroadcast(intent, null);
								System.out.println("whlie循环接收到消息——>"+line+"stoptag =  "+stoptag);
							}
							System.out.println("while循环退出stoptag="+stoptag);
							if (socketinput!=null){
								socketinput.close();
							}
							if(ps!=null){
								ps.close();
								System.out.println("while循环ps.close()");
							}
							if (client!=null){
								client.close();
								System.out.println("while循环client.close()");
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};

			// System.out.println("Client线程销毁");
			Looper.loop();

		}

	}

	class ClientSentThread implements Runnable {
		// private Socket s;
		PrintStream ps = null;

		public ClientSentThread(PrintStream ps) throws IOException {
			this.ps = ps;
		}

		public void run() {
			Looper.prepare();
			outsentHandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					String t = (String) msg.obj;
					ps.println(t);
					System.out.println(Thread.currentThread().getName()
							+ "-ID-" + Thread.currentThread().getId()
							+ " outclientHandler 运行");
					System.out.println(Thread.currentThread().getName()
							+ "-ID-" + Thread.currentThread().getId()
							+ "发送完信息------->" + t);
					if (t.equals(ChatProtocol.PRIVATE_ROUND + (String) idText.getText().toString()
							+ ChatProtocol.SPLIT_SIGN
							+ "kill_while"
							+ ChatProtocol.PRIVATE_ROUND)) {
						stoptag1=false;
					}
					System.out.println("t----->"+t+stoptag1);
					System.out.println("t----->"+ChatProtocol.PRIVATE_ROUND + (String) idText.getText().toString()
							+ ChatProtocol.SPLIT_SIGN
							+ "kill_while"
							+ ChatProtocol.PRIVATE_ROUND);
				}
			};
			Looper.loop();

		}
	}

	// 广播接收器
	public class broadcastreciever extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			String s = intent.getStringExtra("msg");
			System.out.println("接收到广播Two：" + s);
			Message sentMsg = outsentHandler.obtainMessage();
			sentMsg.obj = s;
			outsentHandler.sendMessage(sentMsg);
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		stoptag=false;
		Message sentMsg = outsentHandler.obtainMessage();
		sentMsg.obj = ChatProtocol.PRIVATE_ROUND + (String) idText.getText().toString()
				+ ChatProtocol.SPLIT_SIGN
				+ "kill_while"
				+ ChatProtocol.PRIVATE_ROUND;
		outsentHandler.sendMessage(sentMsg);
		while (stoptag1) {}
		System.out.println("onDestroy执行stoptag="+stoptag);
		System.out.println("onDestroy执行stoptag1="+stoptag1);
		super.onDestroy();
	}
}
