package com.lirui.blackdog;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.AdapterView;

public class TabFragment_l<ParentActivity> extends Fragment
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
		
//		IntentFilter intentfilter=new IntentFilter("BC_One");
//		broadcastreciever broadcastreciever=new broadcastreciever();
//		getActivity().registerReceiver(broadcastreciever, intentfilter);
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
		simp_adapter = new SimpleAdapter(this.getActivity(),getData(), R.layout.item_02,
				new String[] { "pic", "atext", "btext" }, new int[] { R.id.pic,
						R.id.atext, R.id.btext });
		listView.setAdapter(simp_adapter);
		
		OnScrollListener onScrollListener=new OnScrollListener(){

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				if (arg1 == android.widget.AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
					Map<String, Object> map = new HashMap<String, Object>();

					map.put("pic", R.drawable.touxiangf);
					map.put("atext", user.split("#")[1] + "—" + j);
					map.put("idtext", "...");
					j++;
					dataList.add(map);
				}
				simp_adapter.notifyDataSetChanged();
			}};
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
					intent.putExtra("com.lirui.blackdog.intent.string", text1 +"#"+text
							+ "/" + ip + "/" + user);
				} else {
					intent.putExtra("com.lirui.blackdog.intent.string",  text1 +"#"+text
				+ "/" + "127.0.0.1" + "/" + user);
				}

				cb.close();
				clientdba.close();
				startActivity(intent);
			}
			};
		listView.setOnItemClickListener(onItemClickListener);
		listView.setOnScrollListener(onScrollListener);
		return view;     
	}
	private List<Map<String, Object>> getData() {
		SQLiteDatabase clientdba =getActivity().openOrCreateDatabase("clientdba.db",android.content.Context.MODE_PRIVATE, null);
		Cursor cs = clientdba.query("clienttb", null, "_id>=?",
				new String[] { "0" }, null, null, "_id");
		if (cs != null) {
			String[] columns = cs.getColumnNames();
			while (cs.moveToNext()) {

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("pic", R.drawable.touxiang);
				map.put("atext", cs.getString(cs.getColumnIndex(columns[2])));
				map.put("idtext", cs.getString(cs.getColumnIndex(columns[1])));
				dataList.add(map);
			}
	  	    cs.close();
		  	clientdba.close();
	  	}
		return dataList;
	}
	
	
//	public class broadcastreciever extends BroadcastReceiver{
//
//		@Override
//		public void onReceive(Context arg0, Intent intent) {
//			String s=intent.getStringExtra("msg");
//			System.out.println("接收到广播："+s);
//			int g =0;
//			String reciev_s[] = s.split(":");
//
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
//			 System.out.println("listactivity收到：" +reciev_s[0]+"msg= "+ reciev_s[1]);
//			simp_adapter.notifyDataSetChanged();
//		}
//		
//	}
	
}

