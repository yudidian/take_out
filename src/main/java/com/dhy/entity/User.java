package com.dhy.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.io.Serializable;
/**
 * 用户信息
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //姓名
    private String name;


    //邮箱号
    private String phone;


    //性别 0 女 1 男
    private String sex;


    //身份证号
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String idNumber;


    //头像
    private String avatar;


    //状态 0:禁用，1:正常
    private Integer status;
}
