package com.swmaestro.rankserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Util {

	
	public static ArrayList<String> sortByValue(final HashMap<String, User> map){
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(map.keySet());
         
        Collections.sort(list,new Comparator(){
             
            public int compare(Object o1,Object o2){
                Object v1 = map.get(o1).score;
                Object v2 = map.get(o2).score;
                 
                return ((Comparable) v2).compareTo(v1);
            }
             
        });
//        Collections.reverse(list); // 주석시 오름차순
        return list;
    }
}
