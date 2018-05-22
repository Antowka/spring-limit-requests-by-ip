package ru.antowka.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.antowka.annotation.RequestLimitByIp;
import ru.antowka.exception.RequestLimitException;
import ru.antowka.service.ClientService;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class ClientAspect {

    @Autowired
    private ClientService clientService;

    @Before("@annotation(ru.antowka.annotation.RequestLimitByIp)")
    public void requestLimit(JoinPoint joinPoint) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes())
                .getRequest();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        System.out.println("Request received from: " + request.getRemoteAddr());

        RequestLimitByIp methodAnnotation = method.getAnnotation(RequestLimitByIp.class);
        if (methodAnnotation != null && !clientService.isLimitForClientRequests(
                request.getRemoteAddr(), methodAnnotation.limit(), methodAnnotation.timeoutInSec())) {
            throw new RequestLimitException();
        }
    }
}
