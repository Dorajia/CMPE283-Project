package com.EXSI;

import java.util.ArrayList;
import java.util.List;

public class test {
	public static void main(String[] args) throws Exception {
		String string= "/User/Dora/Desktop/vmdkfile.vmdk";
		System.out.println(string.substring(string.lastIndexOf("/") + 1));
		List<String> vmdklist = new ArrayList<String>();
		vmdklist.add(string);
		System.out.println(vmdklist.get(0));
	}
}
