package com.tui.proof.validation.strategy.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RandomObjectFiller {

    private static final Random random = new Random();

    public static <T> T createAndFill(Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = getRandomValueForField(field.getType(), field.getGenericType());
                field.set(instance, value);
            }
            return instance;
        } catch (Exception e) {
            return null;
        }
    }

    private static Object getRandomValueForField(Class<?> type, Type genericType) {
        if (type.isEnum()) {
            Object[] enumValues = type.getEnumConstants();
            return enumValues[random.nextInt(enumValues.length)];
        } else if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            return random.nextInt();
        } else if (type.equals(Long.TYPE) || type.equals(Long.class)) {
            return random.nextLong();
        } else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
            return random.nextDouble();
        } else if (type.equals(Float.TYPE) || type.equals(Float.class)) {
            return random.nextFloat();
        } else if (type.equals(String.class)) {
            return UUID.randomUUID().toString();
        } else if (type.equals(BigInteger.class)) {
            return BigInteger.valueOf(random.nextInt());
        } else if (type.equals(List.class)) {
            Type actualTypeArgument = ((ParameterizedType) genericType).getActualTypeArguments()[0];
            return Collections.singletonList(getRandomValueForField(actualTypeArgument.getClass(), null));
        }
        return createAndFill(type);
    }
}
