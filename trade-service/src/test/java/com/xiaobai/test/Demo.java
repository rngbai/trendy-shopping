package com.xiaobai.test;

import java.util.HashMap;

/**
 * @author lixiaobai
 * @date 2023/11/20
 */
public class Demo {
    public static void main(String[] args) {
        String[][] sect = {{"武当派","张三丰"}, {"全真教","王重阳"}, {"少林派","达摩祖师"}};
        System.out.println(sect[2][0]);
        HashMap<String, String> hashSect = new HashMap<>();
        hashSect.put("武当派","张三丰");
        hashSect.put("全真教","王重阳");
        hashSect.put("少林派","达摩祖师");
        System.out.println(hashSect);
    }
}
