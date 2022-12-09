package com.dhy.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ChartVo implements Serializable {
    @ApiModelProperty(value = "管理员名称")
    private String managerName;

    @ApiModelProperty(value = "管理员ID")
    private String managerId;

    @ApiModelProperty(value = "管理员头像")
    private String managerAvatar;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    @ApiModelProperty(value = "聊天内容")
    private String message;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "发送标志")
    private Integer sendFlag;

    @ApiModelProperty(value = "已读标志")
    private Integer readFlag;
}
