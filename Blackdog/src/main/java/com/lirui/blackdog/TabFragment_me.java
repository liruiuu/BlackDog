package com.lirui.blackdog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class TabFragment_me extends Fragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view= inflater.inflate(R.layout.fragment_me , container, false);   
		TextView tv = (TextView) view.findViewById(R.id.lv);
		return tv;
	}
}
