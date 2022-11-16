package com.crowx.common.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Bean 属性拷贝增强工具类
 * <br/>
 * @author 谢子豪
 * @date 2021-01-11 11:14 上午
 */
public class BeanUtilsBuffer extends BeanUtils {

    /**
     * Copy List 集合
     *
     * @param sourceList 原始集合
     * @param targetClass 目标集合泛型类
     * @return 目标集合
     */
    public static <T> List<T> copyList(Collection<?> sourceList, Class<T> targetClass) {
        return copyListWithProperties(sourceList, targetClass);
    }

    /**
     * Copy List 集合
     *
     * @param sourceList 原始集合
     * @param targetCreate 目标集合泛型类对象创建器
     * @return 目标集合
     */
    public static <T> List<T> copyList(Collection<?> sourceList, Supplier<T> targetCreate) {
        return copyList(sourceList, targetCreate, true);
    }

    /**
     * Copy List 集合
     *
     * @param sourceList 原始集合
     * @param targetCreate 目标集合泛型类对象创建器
     * @param copyNullValue 是否 copy null 值
     * @return 目标集合
     */
    public static <T> List<T> copyList(Collection<?> sourceList, Supplier<T> targetCreate, boolean copyNullValue) {
        return copyListWithProperties(sourceList, targetCreate, copyNullValue);
    }

    /**
     * Copy 对象
     *
     * @param source 原对象
     * @param targetClass 目标对象 Class
     * @param <T> 目标对象类型
     * @return 目标对象
     * @throws IllegalAccessException 反射异常
     * @throws InstantiationException 反射异常
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass) throws IllegalAccessException, InstantiationException {
        T target = targetClass.newInstance();
        copyProperties(source, target);
        return target;
    }

    /**
     * Copy 对象
     *
     * @param source 原对象
     * @param targetCreate 目标对象 创建器
     * @param <T> 目标对象类型
     * @return 目标对象
     */
    public static <T> T copyProperties(Object source, Supplier<T> targetCreate) {
        T target = targetCreate.get();
        copyProperties(source, target);
        return target;
    }

    /**
     * Copy List 集合
     *
     * @param sourceList 原始集合
     * @param targetClass 目标集合泛型类
     * @param includeProperties 需要 copy 的属性名
     * @return 目标集合
     */
    public static <T> List<T> copyListWithProperties(Collection<?> sourceList, Class<T> targetClass, String... includeProperties) {
        return copyListWithProperties(sourceList, () -> {
            try {
                return targetClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }, includeProperties);
    }

    /**
     * Copy List 集合
     *
     * @param sourceList 原始集合
     * @param targetCreate 目标集合泛型类对象创建器
     * @param includeProperties 需要 copy 的属性名
     * @return 目标集合
     */
    public static <T> List<T> copyListWithProperties(Collection<?> sourceList, Supplier<T> targetCreate, String... includeProperties) {
        return copyListWithProperties(sourceList, targetCreate, true, includeProperties);
    }

    /**
     * Copy List 集合
     *
     * @param sourceList 原始集合
     * @param targetCreate 目标集合泛型类对象创建器
     * @param copyNullValue 是否 copy null 值
     * @param includeProperties 需要 copy 的属性名
     * @return 目标集合
     */
    public static <T> List<T> copyListWithProperties(Collection<?> sourceList, Supplier<T> targetCreate, boolean copyNullValue, String... includeProperties) {
        return sourceList.stream().map(source -> {
            T target = targetCreate.get();
            if (includeProperties.length > 0) {
                copyPropertiesInclude(source, target, copyNullValue, includeProperties);
            } else {
                copyProperties(source, target, copyNullValue);
            }
            return target;
        }).collect(Collectors.toList());
    }

    /**
     * copy properties
     * @param source source
     * @param target target
     * @param includeProperties array of property names to include
     */
    public static void copyPropertiesInclude(Object source, Object target, String... includeProperties) {
        copyPropertiesInclude(source, target, true, includeProperties);
    }

    /**
     * copy properties
     * @param source source
     * @param target target
     * @param copyNullValue 是否 copy null 值
     * @param includeProperties array of property names to include
     */
    public static void copyPropertiesInclude(Object source, Object target, boolean copyNullValue, String... includeProperties) {
        copyProperties(source, target, null, includeProperties, null, copyNullValue);
    }

    /**
     * copy properties
     * @param source source
     * @param target target
     * @param copyNullValue 是否 copy null 值
     */
    public static void copyProperties(Object source, Object target, boolean copyNullValue) {
        copyProperties(source, target, null, null, null, copyNullValue);
    }

    /**
     * Copy the property values of the given source bean into the given target bean.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * @param source the source bean
     * @param target the target bean
     * @param editable the class (or interface) to restrict property setting to
     * @param includeProperties array of property names to include
     * @param ignoreProperties array of property names to ignore
     * @param copyNullValue copy null value to target
     * @throws BeansException if the copying failed
     * @see BeanWrapper
     */
    private static void copyProperties(Object source, Object target,
                                       Class<?> editable,
                                       String[] includeProperties,
                                       String[] ignoreProperties,
                                       boolean copyNullValue) throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);

        List<String> includeList = (includeProperties != null ? Arrays.asList(includeProperties) : null);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (includeList == null || includeList.contains(targetPd.getName()))
                    && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                            ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            if (value != null || copyNullValue) {
                                writeMethod.invoke(target, value);
                            }
                        }
                        catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * map转java对象
     *
     * @param map map
     * @param beanClass 对象 class
     * @return 对象
     * @throws Exception 反射异常
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) throws Exception {
        if (map == null) {
            return null;
        }
        T obj = beanClass.newInstance();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            Method setter = property.getWriteMethod();
            if (setter != null) {
                setter.invoke(obj, map.get(property.getName()));
            }
        }
        return obj;
    }

    /**
     * java对象转map
     *
     * @param obj obj
     * @return map
     * @throws Exception 反射异常
     */
    public static Map<String, Object> objectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();

        try {
          if (obj == null) {
              return null;
          }
          BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
          PropertyDescriptor[] propertyDescriptors = beanInfo
                  .getPropertyDescriptors();
          for (PropertyDescriptor property : propertyDescriptors) {
              String key = property.getName();
              if (key.compareToIgnoreCase("class") == 0) {
                  continue;
              }
              Method getter = property.getReadMethod();
              Object value = getter != null ? getter.invoke(obj) : null;
              map.put(key, value);
          }
      }catch (Exception e){
          e.printStackTrace();
      }
        return map;
    }

}
