package com.senhua.my.wonderland.web.common;

import java.util.Random;

/**
 * Created by wly on 2018/4/22.
 */
public class RandStringUtils {
	public static String getCode(){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			sb = sb.append( getRandomString());
		}
		return sb.toString();
	}
	
	public static int getRandomString(){
		Random r = new Random();
		int num = r.nextInt(9);
		return num;
	}
	
	public static String getPhones(String[] arr){
		String str = "";
		if (arr!=null&&arr.length >0) {
			for (int i = 0; i < arr.length; i++) {
				str = str + arr[i] + ",";
			}
		}
		return str.substring(0,str.length()-1);
	}



}
