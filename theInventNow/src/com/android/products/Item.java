package com.android.products;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable{
	//variables to fit schema
	long _id;
	String _UPC;
	String _name;
	int _amount;
	String _username;
	String _description;
	int _delFlag;
	
	public Item(){
		//empty
	}
	
	public Item(long id, String upc, String name, int amount, String username, String description, int delFlag){
		this._id = id;
		this._UPC = upc;
		this._name = name;
		this._amount = amount;
		this._username = username;
		this._description = description;
		this._delFlag = delFlag;
	}
	
	public Item(Parcel in){
		String[] data = new String[7];
		in.readStringArray(data);
		this._id = Integer.parseInt(data[0]);
		this._UPC = data[1];
		this._name = data[2];
		this._amount = Integer.parseInt(data[3]);
		this._username = data[4];
		this._description = data[5];
		this._delFlag = Integer.parseInt(data[6]);
	}
	
	//id getter and setter
	public long getID(){
		return this._id;
	}
	public void setID(long id){
		this._id = id;
	}
	//upc getter and setter
	public String getUPC(){
		return this._UPC;
	}
	public void setUPC(String upc){
		this._UPC = upc;
	}
	
	//name getter and setter
	public String getName(){
		return this._name;
	}
	public void setName(String name){
		this._name = name;
	}
	
	//amount getter and setter
	public int getAmount(){
		return this._amount;
	}
	public void setAmount(int amount){
		this._amount = amount;
	}
	//user getter and setter
	public String getUsername(){
		return this._username;
	}
	public void setUsername(String username){
		this._username = username;
	}
	
	//description getter and setter
	public String getDescription(){
		return this._description;
	}
	public void setDescription(String description){
		this._description = description;
	}
	
	//delFlag getter and setter
	public int getDelFlag(){
		return this._delFlag;
	}
	public void setDelFlag(int flag){
		this._delFlag = flag;
	}
	
	
	@Override
	public String toString(){
		return _UPC;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] { String.valueOf(this._id), this._UPC, this._name, 
				String.valueOf(this._amount), this._username, this._description, String.valueOf(this._delFlag)});
	}
	
	 public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
         public Item createFromParcel(Parcel in) {
             return new Item(in); 
         }

         public Item[] newArray(int size) {
             return new Item[size];
         }
     };
 }


