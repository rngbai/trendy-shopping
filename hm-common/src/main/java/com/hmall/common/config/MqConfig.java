package com.hmall.common.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Configuration;

/**
 * @author lixiaobai
 * @date 2023/11/24
 */
@Configuration
public class MqConfig {
    public MessageConverter jsJackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
