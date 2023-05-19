package com.dhy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Employee implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long id;

  private String avatar;

  private String username;

  private String name;

  private String permission;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  private String phone;

  private String sex;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String idNumber;

  private Integer status;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;

  @TableField(fill = FieldFill.INSERT)
  private Long createUser;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Long updateUser;

}
