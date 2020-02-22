package com.lirui.blackdog;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatMsgViewAdapater extends BaseAdapter {
   private List<ChatMsgEntity> data;
   private Context context;
   private LayoutInflater mInflater;
   
   public ChatMsgViewAdapater(Context context,List<ChatMsgEntity> data){
	   this.context=context;
	   this.data=data;
	   mInflater=LayoutInflater.from(context);
   }
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get( position);
	}

	@Override
	public long getItemId(int  position) {
		return  position;
	}
	
	public int getItemViewType(int position){
		ChatMsgEntity entity =data.get(position);
		if (entity.getMsgTyp())
			return 0;
		else return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChatMsgEntity entity=data.get(position);
		boolean isComMsg= entity.getMsgTyp();
		if(isComMsg){convertView=mInflater.inflate(R.layout.item_l, null);}
		else{convertView=mInflater.inflate(R.layout.item_r, null);}
		ImageView iv=(ImageView) convertView.findViewById(R.id.pic);
		TextView tvuserid=(TextView) convertView.findViewById(R.id.atext);
		TextView tvmsg=(TextView) convertView.findViewById(R.id.btext);
		 iv.setImageResource(entity.getPic());
		 tvuserid.setText(entity.getUserid());
		 tvmsg.setText(entity.getMsg());
		
//		ChatMsgEntity entity=data.get(position);
//		boolean isComMsg= entity.getMsgTyp();
//		ViewHolder viewHolder= null;
//		
//		
//		if (convertView==null){
//			if(isComMsg){convertView=mInflater.inflate(R.layout.item_l, null);}
//			else{convertView=mInflater.inflate(R.layout.item_r, null);}
//			viewHolder=new ViewHolder();
//			viewHolder.iv=(ImageView) convertView.findViewById(R.id.pic);
//			viewHolder.tvuserid=(TextView) convertView.findViewById(R.id.atext);
//			viewHolder.tvmsg=(TextView) convertView.findViewById(R.id.btext);
//			viewHolder.isComMsg=isComMsg;
//			convertView.setTag(viewHolder);
//		}
//		else {
//			viewHolder=(ViewHolder) convertView.getTag();
//			viewHolder.iv.setImageResource(entity.getPic());
//			viewHolder.tvuserid.setText(entity.getUserid());
//			viewHolder.tvmsg.setText(entity.getMsg());
//			viewHolder.isComMsg=isComMsg;
//		}
		
		return convertView;
	}
  static class ViewHolder{
	  public ImageView iv;
	  public TextView tvuserid;
	  public TextView tvmsg;
	  public boolean isComMsg=true;
	  
  }
}
