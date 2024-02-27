package com.hmall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hmall.domain.po.User;
import com.hmall.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HutoolTest {

    @Autowired
    private UserMapper userMapper;
    @Test
    void name() {
        User jack = User.of(1L, "jack");
        User2 u2 = new User2();
        BeanUtil.copyProperties(jack, u2);
        System.out.println("user2 = " + u2);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    static class User {
        Long id;
        String name;
    }

    @Data
    static class User2 {
        String id;
        String name;
    }
    @Test
    public void testUser(){
//        Map map = new HashMap();
//        map.put("userName","Jack");
//        map.put("id",1L);
//        List list = userMapper.selectByMap(map);
        com.hmall.domain.po.User byId = userMapper.getById(1L);
        System.out.println(byId);
    }
}
