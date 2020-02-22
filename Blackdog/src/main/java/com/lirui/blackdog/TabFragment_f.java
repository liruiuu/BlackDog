package com.lirui.blackdog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class TabFragment_f<ParentActivity> extends Fragment
{
	private ListView listView;
	private List<Map<String, Object>> dataList;
	private SimpleAdapter simp_adapter;
	String useridString;
	int j=1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		View view= inflater.inflate(R.layout.fragment , container, false);    
		listView = (ListView)view.findViewById(R.id.lv);     
		
		dataList = new ArrayList<Map<String, Object>>();
		simp_adapter = new SimpleAdapter(this.getActivity(),getData(), R.layout.item_01,
				new String[] { "pic", "text"}, new int[] { R.id.pic,
						R.id.text});
		listView.setAdapter(simp_adapter);
		
		return view;     
	}
	private List<Map<String, Object>> getData() {

				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("pic", R.drawable.friendyard);
				map1.put("text", "ÅóÓÑÈ¦");
				dataList.add(map1);
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("pic", R.drawable.men_scan_icon);
				map2.put("text", "É¨Ò»É¨");
				dataList.add(map2);
				Map<String, Object> map3 = new HashMap<String, Object>();
				map3.put("pic", R.drawable.swift);
				map3.put("text", "Ò¡Ò»Ò¡");
				dataList.add(map3);
				Map<String, Object> map4 = new HashMap<String, Object>();
				map4.put("pic", R.drawable.ic_menu_allfriends);
				map4.put("text", "¸½½üµÄÈË");
				dataList.add(map4);
				Map<String, Object> map5 = new HashMap<String, Object>();
				map5.put("pic", "");
				map5.put("text", "");
				dataList.add(map5);
		return dataList;
	}
	
}