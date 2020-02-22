package com.lirui.blackdog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ChatActivity extends Activity {
	private TextView outputText;
	// private TextView recievText;
	private ListView listView;
	private EditText sentText;
	private Button sentButton;
	private Handler inserverHandler;
	private Handler outclientHandler;
	private Handler chatoutipHandler;
	private ChatMsgViewAdapater mAdapter;
	private List<ChatMsgEntity> dataList;
	// boolean stopThread = false;
	String IP;
	String useridString;
	String usernameString;
	String sendidString;
	String sendnameString;

	// String tag;
	// SeverThread s = new SeverThread();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		outputText = (TextView) findViewById(R.id.outputText);
		// recievText = (TextView) findViewById(R.id.recievText);
		listView = (ListView) findViewById(R.id.listView);
		sentText = (EditText) findViewById(R.id.sentText);
		sentButton = (Button) findViewById(R.id.sentButton);
		ButtonListener buttonListener = new ButtonListener();
		sentButton.setOnClickListener(buttonListener);
		Intent intent = getIntent();
		String tempString = intent
				.getStringExtra("com.lirui.blackdog.intent.string");
//		String recievString[] = tempString.split("/")[];
//		IP = recievString[1];
		System.out.println("12345678900:"+tempString);
		useridString =tempString.split("/")[2].split("#")[0];
		usernameString = tempString.split("/")[2].split("#")[1];
		sendidString =tempString.split("/")[0].split("#")[0];
		sendnameString =tempString.split("/")[0].split("#")[1];
		outputText.setText(sendnameString + ": "+tempString.split("/")[1]);

		IntentFilter intentfilter = new IntentFilter("BC_One");
		broadcastreciever broadcastreciever = new broadcastreciever();
		registerReceiver(broadcastreciever, intentfilter);

		dataList = new ArrayList<ChatMsgEntity>();
		mAdapter = new ChatMsgViewAdapater(this, getdata());
		listView.setAdapter(mAdapter);
	}

	private List<ChatMsgEntity> getdata() {

		return dataList;

	}

	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			String line = ChatProtocol.PRIVATE_ROUND +  sendidString
					+ ChatProtocol.SPLIT_SIGN
					+ (String) sentText.getText().toString()
					+ ChatProtocol.PRIVATE_ROUND;
			Intent intent = new Intent();
			intent.putExtra("msg", line);
			intent.setAction("BC_Two");
			sendOrderedBroadcast(intent, null);

			ChatMsgEntity cme = new ChatMsgEntity(R.drawable.touxiang, "："
					+ usernameString, (String) sentText.getText().toString(),
					false);
			dataList.add(cme);
			mAdapter.notifyDataSetChanged();
			sentText.setText("");
		}
	}


	// 广播接收器
	public class broadcastreciever extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			String s = intent.getStringExtra("msg");
			System.out.println("chat接收到广播：" + s);
			String temp[] = s.split(":");

			String s1 = temp[0] + ":";
			String s2 = temp[1];
			if (sendidString.equals(temp[0])) {
				ChatMsgEntity cme = new ChatMsgEntity(R.drawable.touxiang,sendnameString+"：", s2,
						true);
				dataList.add(cme);
				
				mAdapter.notifyDataSetChanged();
			}

		}

	}


	// @Override
	// protected void onDestroy() {
	// stopThread = true;
	// // s.interrupt();
	// Message ipMsg = chatoutipHandler.obtainMessage();
	// ipMsg.obj = "127.0.0.1";
	// chatoutipHandler.sendMessage(ipMsg);
	// Message sentMsg = outclientHandler.obtainMessage();
	// sentMsg.obj = "Serversocket，杀死你";
	// outclientHandler.sendMessage(sentMsg);
	// System.out.println("ChatActivity onDestroy" + stopThread);
	// super.onDestroy();
	// }
}
