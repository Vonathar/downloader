package io.github.vonathar.testutils;

import java.lang.reflect.Field;
import lombok.SneakyThrows;

public class ReflectionUtils {

  @SneakyThrows
  public static <T> Object getField(T object, String fieldName) {
    Field field = object.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(object);
  }
}
