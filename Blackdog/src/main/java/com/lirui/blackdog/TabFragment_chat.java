package com.lirui.blackdog;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.AdapterView;

public class TabFragment_chat<ParentActivity> extends Fragment
{
	private ListView listView;
	private List<Map<String, Object>> dataList;
	private SimpleAdapter simp_adapter;
	String useridString;
	String user;
	int j=1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		IntentFilter intentfilter=new IntentFilter("BC_One");
		broadcastreciever broadcastreciever=new broadcastreciever();
		getActivity().registerReceiver(broadcastreciever, intentfilter);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		View view= inflater.inflate(R.layout.fragment , container, false);    
		listView = (ListView)view.findViewById(R.id.lv);     
		
		dataList = new ArrayList<Map<String, Object>>();
//		dataList=((ChActivity) getActivity()).getData();
		useridString=((ChActivity) getActivity()).getUseridString();
		SQLiteDatabase clientdba = getActivity().openOrCreateDatabase("clientdba.db",
				android.content.Context.MODE_PRIVATE, null);
		Cursor cb = clientdba.query("clienttb", null, "userid=?",
				new String[] { useridString }, null, null, "_id");
		cb.moveToFirst();// 神奇...........
		if (cb.getCount() != 0) {
			String usernameString = cb.getString(cb.getColumnIndex("username"));
			user=useridString+"#"+usernameString;
		}
		cb.close();
		clientdba.close();
		simp_adapter = new SimpleAdapter(this.getActivity(),getData(), R.layout.item,
				new String[] { "pic", "atext", "btext" , "idtext"}, new int[] { R.id.pic,
						R.id.atext, R.id.btext , R.id.idtext});
		listView.setAdapter(simp_adapter);
		
		OnItemClickListener onItemClickListener=new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {

				Map<String, Object> map = (Map<String, Object>) listView
						.getItemAtPosition(position);
				String text = map.get("atext").toString();
				String text1 = map.get("idtext").toString();
				Toast toast = Toast.makeText(getActivity(), "与" + " [" + text + "] " + "开始聊天",
						Toast.LENGTH_SHORT);
				toast.show();
				Intent intent = new Intent();
				intent.setClass(getActivity(), ChatActivity.class);
				SQLiteDatabase clientdba = getActivity().openOrCreateDatabase("clientdba.db",
						android.content.Context.MODE_PRIVATE, null);
				Cursor cb = clientdba.query("clienttb", null, "userid=?",
						new String[] { text1 }, null, null, "_id");
				cb.moveToFirst();// 神奇...........
				if (cb.getCount() != 0) {
					String ip = cb.getString(cb.getColumnIndex("ip"));
					intent.putExtra("com.lirui.blackdog.intent.string", text1 + "#"+text1
							+ "/" + ip + "/" + user);
				} else {
					intent.putExtra("com.lirui.blackdog.intent.string", text1 + "#"+text1
							+ "/" + "127.0.0.1" + "/" + user);
				}

				cb.close();
				clientdba.close();
				startActivity(intent);
			}
			};
		listView.setOnItemClickListener(onItemClickListener);
		return view;     
	}
	private List<Map<String, Object>> getData() {
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("pic", R.drawable.touxiangfm);
//				map.put("atext", "1000");
//				map.put("idtext", "1000");
//				map.put("btext", "");
//				dataList.add(map);
		SQLiteDatabase clientdba =getActivity().openOrCreateDatabase("clientdba.db",android.content.Context.MODE_PRIVATE, null);
		Cursor cs = clientdba.query("chattb", null, "_id>=?",
				new String[] { "0" }, null, null, "_id");
		if (cs != null) {
			String[] columns = cs.getColumnNames();
			while (cs.moveToNext()) {

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("pic", R.drawable.touxiang);
				map.put("atext", cs.getString(cs.getColumnIndex(columns[2])));
				map.put("idtext", cs.getString(cs.getColumnIndex(columns[1])));
				map.put("btext", cs.getString(cs.getColumnIndex(columns[3])));
				dataList.add(map);
			}
		}
		cs.close();
	  	clientdba.close();
		return dataList;
	}
	
	
	public class broadcastreciever extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent intent) {
			String s=intent.getStringExtra("msg");
			System.out.println("接收到广播："+s);
			int g =0;
			String reciev_s[] = s.split(":");
			
			boolean lag=false;
			SQLiteDatabase clientdba =getActivity().openOrCreateDatabase("clientdba.db",android.content.Context.MODE_PRIVATE, null);//+++
			
			for (int j =0; j <listView.getCount(); j++) {
				Map<String, Object> map = (Map<String, Object>) listView
						.getItemAtPosition(j);
				String text = map.get("idtext").toString();
				if(reciev_s[0].equals(text)){
					lag=true;
					 dataList.get(j).put("btext",reciev_s[1]);
					 System.out.println("list中有账号：" +reciev_s[0]+"msg= "+ reciev_s[1]);
						clientdba.execSQL("create table if not exists chattb(_id integer,userid text not null,username text not null,msg text not null)");//+++
						ContentValues values = new ContentValues();
						values.clear();
						values.put("msg", reciev_s[1]);
						clientdba.update("chattb", values, "userid=?",
								new String[] { reciev_s[0] });
					break;
				};
			}
			if (lag==false) {
				clientdba.execSQL("create table if not exists chattb(_id integer,userid text not null,username text not null,msg text not null)");//+++
				clientdba.execSQL("create table if not exists clienttb(_id integer,userid text not null,username text not null,ip text not null,msg text not null)");
				Cursor cb = clientdba.query("clienttb", null, "userid=?",
						new String[] {reciev_s[0]}, null, null, "_id");

			    cb.moveToFirst();// 神奇...........
			if (cb.getCount() != 0) {
			String username= cb.getString(cb.getColumnIndex("username"));
			Map<String, Object> map = new HashMap<String, Object>();
		    map.put("pic", R.drawable.touxiang);
			map.put("atext", username);
			map.put("btext", reciev_s[1]);
			map.put("idtext", reciev_s[0]);
			System.out.println("list中没有账号：" +reciev_s[0]+"msg= "+ reciev_s[1]);
			dataList.add(map);
			
			
			
			ContentValues values = new ContentValues();
			values.clear();
			values.put("userid", reciev_s[0]);
			values.put("_id", listView.getCount()+1);
			values.put("username", username);
			values.put("msg", reciev_s[1]);
			clientdba.insert("chattb", null, values);
				
			}
			cb.close();
			clientdba.close();
			
		}

			
//			SQLiteDatabase clientdba = getActivity().openOrCreateDatabase("clientdba.db",
//					android.content.Context.MODE_PRIVATE, null);
//			Cursor cs = clientdba.query("clienttb", null, "userid=?",
//					new String[] { reciev_s[0] }, null, null, "_id");
//			if (cs != null) {
//			if(cs.moveToNext()){
//		     g =Integer.valueOf(cs.getString(cs.getColumnIndex("_id"))).intValue();
//		    System.out.println("listactivity查到"+reciev_s[0]+"_id= " + g);
//			}}
//			
//			cs.close();
//			clientdba.close();
//			 dataList.get(g).put("btext",reciev_s[1]);
			 
			 System.out.println("listactivity收到：" +reciev_s[0]+"msg= "+ reciev_s[1]);
			 
			    
			simp_adapter.notifyDataSetChanged();
		}
		
	}
	
}

