package com.mipt.mikhaildubov.annotations_reflections;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class Validator {

  private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
  private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

  public static ValidationResult validate(Object object) {
    ValidationResult result = new ValidationResult();

    if (object == null) {
      result.addError("Object cannot be null");
      return result;
    }

    Field[] fields = object.getClass().getDeclaredFields();

    for (Field field : fields) {
      field.setAccessible(true);

      try {
        Object fieldValue = field.get(object);

        if (field.isAnnotationPresent(NotNull.class)) {
          NotNull annotation = field.getAnnotation(NotNull.class);
          if (fieldValue == null) {
            result.addError(annotation.message());
          }
        }

        if (fieldValue != null) {

          if (field.isAnnotationPresent(Size.class) && fieldValue instanceof String) {
            Size annotation = field.getAnnotation(Size.class);
            String stringValue = (String) fieldValue;
            int length = stringValue.length();
            if (length < annotation.min() || length > annotation.max()) {
              result.addError(annotation.message());
            }
          }

          if (field.isAnnotationPresent(Range.class) && fieldValue instanceof Number) {
            Range annotation = field.getAnnotation(Range.class);
            Number numberValue = (Number) fieldValue;
            double value = numberValue.doubleValue();
            if (value < annotation.min() || value > annotation.max()) {
              result.addError(annotation.message());
            }
          }

          if (field.isAnnotationPresent(Email.class) && fieldValue instanceof String) {
            Email annotation = field.getAnnotation(Email.class);
            String emailValue = (String) fieldValue;
            if (!EMAIL_PATTERN.matcher(emailValue).matches()) {
              result.addError(annotation.message());
            }
          }
        }

      } catch (IllegalAccessException e) {
        result.addError("Cannot access field: " + field.getName());
      }
    }

    return result;
  }
}