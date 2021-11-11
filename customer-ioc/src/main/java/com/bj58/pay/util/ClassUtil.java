package com.bj58.pay.util;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yy
 * @version 1.0v
 * @description 类工具
 * @date 2021/11/9 17:27
 */
public class ClassUtil {

    public static final String FILE = "file";

    public static Set<Class<?>> getClassByPackageName(String packageName) {

        ClassLoader contextClassLoader = getClassLoader();

        URL url = contextClassLoader.getResource(packageName.replace(".", File.separator));
        Set<Class<?>> classSet = null;

        if (url.getProtocol().equals(FILE)) {
            classSet = new HashSet<>();
            searchClass(classSet, url.getPath(), packageName);
        }

        return classSet;

    }

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private static void searchClass(Set<Class<?>> classSet, String path, String packageName) {

        File file = new File(path);
        if (!file.isDirectory()) {
            return;
        }

        File[] files = file.listFiles(f -> {

            if (f.isDirectory()) {
                return true;
            }

            String absolutePath = f.getAbsolutePath();
            if (absolutePath.endsWith(".class")) {

                absolutePath = absolutePath.replace(File.separator, ".");
                String className = absolutePath.substring(absolutePath.indexOf(packageName));
                className = className.substring(0, className.lastIndexOf("."));
                try {
                    classSet.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

            return false;

        });

        if (files != null) {
            for (File f : files) {
                searchClass(classSet, f.getAbsolutePath(), packageName);
            }
        }

    }

    public static void main(String[] args) {
        Set<Class<?>> classByPackageName = getClassByPackageName("com.bj58.pay");
        classByPackageName.stream().forEach(System.out::println);
    }



}
