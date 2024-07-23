package com.itheima.mp.service;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.po.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IUserServiceTest {
    @Autowired
    private IUserService userService;

    @Test
    void testSaveUser(){
        User user = new User();
        user.setId(5L);
        user.setUsername("LiLei");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
//        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setInfo(UserInfo.of(24,"英文老师","female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userService.save(user);
    }

    @Test
    void testQuery(){
        List<User> users = userService.listByIds(List.of(1L, 2L, 3L));
        users.forEach(System.out::println);
    }

    private User buildUser(int i){
        User user = new User();
        user.setUsername("user_"+i);
        user.setPassword("123");
        user.setPhone(""+(18688990011L +i));
        user.setBalance(200);
//        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setInfo(UserInfo.of(24,"英文老师","female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return user;
    }

    @Test
    void testSaveBatch() {
        // 准备10万条数据
        List<User> list = new ArrayList<>(1000);
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            list.add(buildUser(i));
            // 每1000条批量插入一次
            if (i % 1000 == 0) {
                userService.saveBatch(list);
                list.clear();
            }
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - b));
    }

    @Test
    void testPageQuery() {
        int pageNum = 1,pageSize = 2;
        // 1.分页查询，new Page()的两个参数分别是：页码、每页大小
        Page<User> page = Page.of(pageNum,pageSize);
//        Page<User> p = userService.page(new Page<>(2, 2));
        //排序条件
        page.addOrder(new OrderItem("balance",true));
        page.addOrder(new OrderItem("id",true));
        Page<User> p = userService.page(page);
        // 2.总条数
        System.out.println("total = " + p.getTotal());
        // 3.总页数
        System.out.println("pages = " + p.getPages());
        // 4.数据
        List<User> records = p.getRecords();
        records.forEach(System.out::println);
    }
}