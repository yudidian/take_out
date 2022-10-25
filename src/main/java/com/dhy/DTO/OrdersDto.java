package com.dhy.DTO;

import com.dhy.entity.OrderDetail;
import com.dhy.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {
    private List<OrderDetail> orderDetailList;
}
