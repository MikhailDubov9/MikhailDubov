package com.mipt.mikhaildubov.collections;

import java.util.Objects;

public class Student {

  private final int id;
  private final String name;
  private final double grade;

  public Student(int id, String name, double grade) {
    this.id = id;
    this.name = name;
    this.grade = grade;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public double getGrade() {
    return grade;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Student student = (Student) obj;
    return id == student.id && Double.compare(grade, student.grade) == 0 && Objects.equals(name, student.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, grade);
  }
}
