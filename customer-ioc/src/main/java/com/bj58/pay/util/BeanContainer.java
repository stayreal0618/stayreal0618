package com.bj58.pay.util;

import com.bj58.pay.annotation.Autowire;
import com.bj58.pay.annotation.Controller;
import com.bj58.pay.annotation.Responsity;
import com.bj58.pay.annotation.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yy
 * @version 1.0v
 * @description bean 容器
 * @date 2021/11/10 19:11
 */
public class BeanContainer {


    private static ConcurrentHashMap<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    private static Boolean isInit = false;
    private static List<Class<? extends Annotation>> clazzAnnotations = Arrays.asList(Controller.class, Responsity.class, Service.class);
    private static List<Class<? extends Annotation>> injectAnnotations = Arrays.asList(Autowire.class);

    public static synchronized void init(String packageName) {

        if (isInit) {
            return;
        }

        Set<Class<?>> classSet = ClassUtil.getClassByPackageName(packageName);

        for (Class clazz : classSet) {

            for (Class<? extends Annotation> annotation : clazzAnnotations) {
                clazzAnnotations.stream().forEach(an -> {
                    if (clazz.isAnnotationPresent(an)) {
                        try {
                            beanMap.put(clazz, clazz.newInstance());
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        }

        isInit = true;

    }

    public static void injectBean() {

        Set<Class<?>> keys = beanMap.keySet();
        keys.stream().forEach(aClass -> {

            Field[] fields = aClass.getDeclaredFields();
            Arrays.stream(fields).forEach(field -> {
                injectAnnotations.stream().forEach(ann -> {
                    Arrays.stream(field.getAnnotations()).forEach(fieldAnn -> {

                    });
                });
            });

        });

    }

    public static Object getBeanByAnnotation(Class<? extends Annotation> clazz) {

        Enumeration<Class<?>> keys = beanMap.keys();

        while (keys.hasMoreElements()) {

            Class<?> aClass = keys.nextElement();
            if (aClass.isAnnotationPresent(clazz)) {
                return beanMap.get(aClass);
            }

        }

        return null;

    }

    public static void addBean(Class<?> clazz, Object obj) {
        beanMap.put(clazz, obj);
    }

    public static void removeBean(Class<?> clazz) {
        beanMap.remove(clazz);
    }

    public static Set<Object> getBeans() {
        return new HashSet<>(beanMap.values());
    }

    public static void main(String[] args) {
        BeanContainer.init("com.bj58.pay");
        BeanContainer.getBeans().stream().forEach(System.out::println);
    }

}
