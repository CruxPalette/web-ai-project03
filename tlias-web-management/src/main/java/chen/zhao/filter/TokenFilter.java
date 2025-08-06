package chen.zhao.filter;

import chen.zhao.utils.CurrentHolder;
import chen.zhao.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
@Slf4j
@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 强转一下
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // 1.获取请求路径 看看是不是login
        String requestURI = request.getRequestURI(); // /employee/login
        // 2.判断是不是登陆请求 如果路径中包含/login 说明是登录操作 放行
        if (requestURI.contains("/login")) {
            log.info("登录请求，放行");
            filterChain.doFilter(request, response);
            return;     //这样就不会再往下运行了
        }
        // 3.获取请求头中的token
        String token = request.getHeader("token");

        // 4.判断token是否存在 如果不存在 说明用户没有登录 返回错误信息(401状态吗)
        if (token == null || token.isEmpty()) {
            log.info("令牌为空 响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        // 5.如果token存在 校验令牌 如果校验失败 返回错误信息
        try {
            Claims claims = JwtUtils.parseToken(token);
            Integer empId = Integer.valueOf(claims.get("id").toString());
            CurrentHolder.setCurrentId(empId);  // 存入
            log.info("当前登录员工ID: {}, 将其存入ThreadLocal", empId);
        } catch (Exception e) {
            log.info("令牌非法，响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 6.校验成功 放行
        log.info("令牌合法 通过");
        filterChain.doFilter(request, response);

        // 7.删除threadlocal中的数据 一定在最后删除
        CurrentHolder.remove();
    }
}
