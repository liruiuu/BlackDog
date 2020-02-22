package com.lirui.blackdog;

import android.R.string;

public class ChatMsgEntity {

	private int pic;
	private String userid;
	private String msg;
	private boolean isComMsg;
public int getPic(){
	return pic;
}
public void  setPic(int pic){
	this.pic = pic;
}
public String getUserid(){
	return userid;
}
public void  setUserid(String userid){
	this.userid = userid;
}
public String getMsg(){
	return msg;
}
public void  setMsg(String msg){
	this.msg = msg;
}
public boolean getMsgTyp(){
	return isComMsg;
}
   public void  setMsgTyp(boolean isComMsg){
	  isComMsg =isComMsg;
    }
	
	public ChatMsgEntity (){}
	public ChatMsgEntity (int pic,String userid,String msg,boolean isComMsg){
		this.pic=pic;
		this.userid=userid;
		this.msg=msg;
		this.isComMsg=isComMsg;
	}
	
}
