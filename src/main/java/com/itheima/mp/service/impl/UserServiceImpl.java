package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.PageQuery;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public void deductBalance(Long id, Integer money) {
        // 1.查询用户
        User user = this.getById(id);//自己就是service可以直接调用 不用注入
        // 2.校验用户状态
        if(user==null || user.getStatus()==2){
            throw new RuntimeException("用户状态异常");
        }
        // 3.校验余额是否充足
        if(user.getBalance()<money){
            throw new RuntimeException("用户余额不足");
        }
        // 4.扣减余额
//        baseMapper.deductBalance(id,money);
        int remainbalance = user.getBalance()-money;
        lambdaUpdate()
                .set(User::getBalance,remainbalance)
                .set(remainbalance==0,User::getStatus,2)
                .eq(User::getId,id)
                .eq(User::getBalance,user.getBalance())// 乐观锁
                .update();
    }

    @Override
    public List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance) {
        return lambdaQuery()
                .like(name != null,User::getUsername,name)
                .eq(status != null,User::getStatus,status)
                .ge(minBalance != null,User::getBalance,minBalance)
                .le(maxBalance != null,User::getBalance,maxBalance)
                .list();
    }

    @Override
    public PageDTO<UserVO> queryUsersPage(UserQuery query) {
        String name = query.getName();
        Integer status = query.getStatus();
        // 1.构建分页条件
        Page<User> page = query.toMpPageDefaultSortByCreateTimeDesc();

        // 2.分页查询
        Page<User> p = lambdaQuery()
                .like(name != null,User::getUsername,name)
                .eq(status != null,User::getStatus,status)
                .page(page);

        // 3.封装VO结果
        return PageDTO.of(p,UserVO.class);
    }
}
