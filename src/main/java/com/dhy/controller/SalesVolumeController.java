package com.dhy.controller;

import com.dhy.common.R;
import com.dhy.entity.SalesVolume;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
@RequestMapping("/sales")
public class SalesVolumeController {
    @GetMapping("/{dishId}")
    private R<SalesVolume> getDishSales(@PathVariable Long dishId){
        return null;
    }
}
