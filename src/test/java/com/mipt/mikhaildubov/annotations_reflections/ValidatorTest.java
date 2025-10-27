package com.mipt.mikhaildubov.annotations_reflections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidatorTest {

  static class TestUser {

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Email(message = "Invalid email format")
    @NotNull(message = "Email cannot be null")
    private String email;

    @Range(min = 0, max = 150, message = "Age must be between 0 and 150")
    private Integer age;

    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

    public TestUser() {
    }

    public TestUser(String name, String email, Integer age, String password) {
      this.name = name;
      this.email = email;
      this.age = age;
      this.password = password;
    }
  }

  @Test
  void validateCorrectObject() {
    TestUser user = new TestUser("Mikhail", "mikhaildubov2007@gmail.com", 18, "password123");

    ValidationResult result = Validator.validate(user);

    assertTrue(result.isValid());
    assertTrue(result.getErrors().isEmpty());
  }

  @Test
  void validateObjectWithAllErrors() {
    TestUser user = new TestUser("M", "invalid-email", 200, "short");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertEquals(4, result.getErrors().size());
    assertTrue(result.getErrors().contains("Name must be between 2 and 50 characters"));
    assertTrue(result.getErrors().contains("Invalid email format"));
    assertTrue(result.getErrors().contains("Age must be between 0 and 150"));
    assertTrue(result.getErrors().contains("Password must be between 6 and 20 characters"));
  }

  @Test
  void validateNotNullAnnotation() {
    TestUser user = new TestUser(null, "mikhaildubov2007@gmail.com", 18, "password123");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("Name cannot be null"));
  }

  @Test
  void validateSizeAnnotation() {
    TestUser user = new TestUser("M", "mikhaildubov2007@gmail.com", 18, "password123password123");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("Name must be between 2 and 50 characters"));
    assertTrue(result.getErrors().contains("Password must be between 6 and 20 characters"));
  }

  @Test
  void validateRangeAnnotationUnderMinimum() {
    TestUser user = new TestUser("Mikhail", "mikhaildubov2007@gmail.com", -18, "password123");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("Age must be between 0 and 150"));
  }

  @Test
  void validateRangeAnnotationOverMaximum() {
    TestUser user = new TestUser("Mikhail", "mikhaildubov2007@gmail.com", 228, "password123");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("Age must be between 0 and 150"));
  }

  @Test
  void validateEmailAnnotation() {
    TestUser user = new TestUser("Mikhail", "invalid-email", 18, "password123");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("Invalid email format"));
  }

  @Test
  void validateNullObject() {
    ValidationResult result = Validator.validate(null);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().contains("Object cannot be null"));
  }

  @Test
  void validateSizeAnnotationMin() {
    TestUser user = new TestUser("Mi", "mikhaildubov2007@gmail.com", 18, "passwo");

    ValidationResult result = Validator.validate(user);

    assertTrue(result.isValid());
  }

  @Test
  void validateSizeAnnotationMax() {
    TestUser user = new TestUser("MikhailMikhailMikhailMikhailMikhailMikhailMikhailM",
        "mikhaildubov2007@gmail.com", 18, "password123password1");

    ValidationResult result = Validator.validate(user);

    assertTrue(result.isValid());
  }

  @Test
  void validateRangeAnnotationMin() {
    TestUser user = new TestUser("Mikhail", "mikhaildubov2007@gmail.com", 0, "password123");

    ValidationResult result1 = Validator.validate(user);

    assertTrue(result1.isValid());
  }

  @Test
  void validateRangeAnnotationMax() {
    TestUser user = new TestUser("Mikhail", "mikhaildubov2007@gmail.com", 150, "password123");

    ValidationResult result2 = Validator.validate(user);

    assertTrue(result2.isValid());
  }

  static class SimpleObject {

    private String noAnnotations;
    private int primitiveField;
  }

  @Test
  void validateObjectWithoutAnnotations() {
    SimpleObject obj = new SimpleObject();
    obj.noAnnotations = "test";

    ValidationResult result = Validator.validate(obj);

    assertTrue(result.isValid());
  }
}