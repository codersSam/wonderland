package com.senhua.my.wonderland.web.dao;

import com.senhua.my.wonderland.web.entity.UserInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * Created by Administrator on 2019/9/11.
 */
@Repository
public interface UserInfoMapper extends Mapper<UserInfo> {
}
