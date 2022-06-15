package com.dhy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dhy.entity.User;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
