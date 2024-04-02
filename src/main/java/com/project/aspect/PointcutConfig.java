package com.project.aspect;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
public class PointcutConfig {
    @Pointcut("execution(* com.project.controller.*.*(..))")
    public void apiLayerPointcut() {
    }

    @Pointcut("execution(* com.project.service.*.*(..))")
    public void serviceLayerPointcut() {
    }

    @Pointcut("@annotation(com.project.aspect.MeasureTime)")
    public void measureTimeAnnotationPointcut() {
    }
}
