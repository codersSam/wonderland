package com.senhua.my.wonderland.web.dao;

import com.senhua.my.wonderland.web.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by Administrator on 2019/9/17.
 */
@Repository
public interface RoleMapper extends Mapper<Role> {

    List<Role> findByUid(@Param("uid") Long uid);
}
