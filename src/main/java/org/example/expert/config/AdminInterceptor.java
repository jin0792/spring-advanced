package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String controllerClass = ((HandlerMethod) handler).getBeanType().getName();
        String methodName = ((HandlerMethod) handler).getMethod().getName();

        boolean isTargetMethod = (("org.example.expert.domain.comment.controller.CommentAdminController".equals(controllerClass) && "deleteComment".equals(methodName)) ||
                ("org.example.expert.domain.user.controller.UserAdminController".equals(controllerClass) && "changeUserRole".equals(methodName)));

        if (!isTargetMethod) {
            return true;
        }

        String receivedHash = request.getParameter("admin");

        if (receivedHash == null || !receivedHash.equals(request.getParameter("admin"))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            logger.warn("[BLOCKED] 인증실패: {}.{}", controllerClass, methodName);
            return false;
        }

        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        LocalDateTime requestTime = LocalDateTime.now();

        logger.info("[ADMIN API ACCESS] {}.{} / method={} / uri={} / time={} / verifiedBy=header", controllerClass, method, method, requestURI, requestTime);

        return true;
    }

}