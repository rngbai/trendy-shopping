package com.hmall.api.intercerptor;

import com.hmall.common.utils.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author lixiaobai
 * @date 2023/11/18
 */

public class UserInfoRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        //获取登录用户
        Long user = UserContext.getUser();
        System.out.println(user);
        if (user != null) {
            template.header("user-info", user.toString());
        }
        // 获取登录用户
//        System.out.println("我被调用了");
//        Long userId = UserContext.getUser();
//        System.out.println("userId========>"+userId);
//        if(userId == null) {
//            // 如果为空则直接跳过
//            return;
//        }
//        // 如果不为空则放入请求头中，传递给下游微服务
//        template.header("user-info", userId.toString());
//    }
    }
}
