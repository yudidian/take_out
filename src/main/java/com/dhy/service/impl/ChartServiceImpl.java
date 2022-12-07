package com.dhy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhy.entity.Chart;
import com.dhy.mapper.ChartMapper;
import com.dhy.service.ChartService;
import org.springframework.stereotype.Service;

@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart> implements ChartService {
}
