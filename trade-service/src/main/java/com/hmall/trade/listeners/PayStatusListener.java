package com.hmall.trade.listeners;

import com.hmall.trade.domain.po.Order;
import com.hmall.trade.service.IOrderService;
import com.hmall.trade.service.impl.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author lixiaobai
 * @date 2023/11/24
 */
@Component
@RequiredArgsConstructor
public class PayStatusListener {
    private final IOrderService orderService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "mark.order.pay.queue",durable = "true"),
            exchange = @Exchange(name = "pay.topic",type = ExchangeTypes.TOPIC),
            key = "pay.success"
    ))
    public void listenPaySuccess(Long orderId){
//        orderService.markOrderPaySuccess(orderId);
        //1.查询订单
//        Order order = orderService.getById(orderId);
        //2.判断订单状态是否为未支付
//        if (order==null || order.getStatus()!=1){
//            //订单不存在 或者状态异常
//            return;
//        }
        //3.如果未支付，标记订单状态已支付
//        orderService.markOrderPaySuccess(orderId);
        //我们可以使用msyql自带的update上锁机制来优雅的解决这个问题
        orderService.lambdaUpdate()
                .set(Order::getStatus,2)
                .set(Order::getPayTime, LocalDateTime.now())
                .set(Order::getId,orderId)
                .set(Order::getStatus,1)
                .update();
    }
}
