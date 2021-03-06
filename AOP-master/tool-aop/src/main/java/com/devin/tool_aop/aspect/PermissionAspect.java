package com.devin.tool_aop.aspect;

import com.devin.apply_permission.ApplyPermission;
import com.devin.tool_aop.AopUtils;
import com.devin.tool_aop.LogUtils;
import com.devin.tool_aop.annotation.Permission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by whf on 17/3/8.
 */
@Aspect
public class PermissionAspect {

    public static final String TAG = "Aspect";

    @Pointcut("execution(@com.devin.tool_aop.annotation.Permission * *(..)) && @annotation(permission)")
    public void permission(Permission permission) {
    }

    @Around("permission(permission)") // 在连接点进行方法替换
    public void permission(final ProceedingJoinPoint joinPoint, Permission permission) throws Throwable {
        LogUtils.d(TAG, ">>>>>permission：" + permission.must());
        if (AopUtils.mApplication == null) {
            throw new Exception("AopUtils未初始化");
        }
        ApplyPermission.get(new ApplyPermission.CallBack() {
            @Override
            public void onGranted() {
                try {
                    joinPoint.proceed();// 执行原方法
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void onDenied() {
            }
        }).apply(AopUtils.mApplication, permission.value(), permission.must());
    }
}
