package com.senhua.my.wonderland.web.dao;

import com.senhua.my.wonderland.web.entity.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * Created by Administrator on 2019/7/20.
 */
@Repository
public interface UserMapper extends Mapper<User> {
}
