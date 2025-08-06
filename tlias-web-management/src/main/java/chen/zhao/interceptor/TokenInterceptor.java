package chen.zhao.interceptor;

import chen.zhao.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component  //因为是spring容器管理的
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1.获取请求路径 看看是不是login
        String requestURI = request.getRequestURI(); // /employee/login

        // 这一部分也可以不写 可以在webfig里面直接exclude掉就行
        // 2.判断是不是登陆请求 如果路径中包含/login 说明是登录操作 放行
//        if (requestURI.contains("/login")) {
//            log.info("登录请求，放行");
//            return true;     //这样就不会再往下运行了
//        }
        // 3.获取请求头中的token
        String token = request.getHeader("token");

        // 4.判断token是否存在 如果不存在 说明用户没有登录 返回错误信息(401状态吗)
        if (token == null || token.isEmpty()) {
            log.info("令牌为空 响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        // 5.如果token存在 校验令牌 如果校验失败 返回错误信息
        try {
            JwtUtils.parseToken(token);
        } catch (Exception e) {
            log.info("令牌非法，响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 6.校验成功 放行
        log.info("令牌合法 通过");
        return true;
    }
}
