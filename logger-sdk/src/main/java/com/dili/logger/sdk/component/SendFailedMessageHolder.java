package com.dili.logger.sdk.component;

import java.util.HashSet;

/**
 * 失败消息保存
 */
public class SendFailedMessageHolder {
    private static HashSet set = new HashSet<String>();

    public static boolean add(Object e){
        return set.add(e);
    }

    public static void clear(){
        set.clear();
    }

    public static HashSet getAll(){
        return set;
    }

}
