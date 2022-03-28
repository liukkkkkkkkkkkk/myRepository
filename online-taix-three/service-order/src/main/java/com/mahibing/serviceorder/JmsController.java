package com.mahibing.serviceorder;

import com.mahibing.serviceorder.entity.TblOrderEvent;
import com.mahibing.serviceorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 49178
 * @create 2022/3/25
 */
@RestController
public class JmsController {

    @Autowired
    private OrderService orderService;

   @RequestMapping("/createOrder")

    public String createOrder(){
       orderService.insert();
        return "success";
    }

}
