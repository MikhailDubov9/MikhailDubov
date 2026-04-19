package com.mipt.mikhaildubov.todo.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Processor for logging bean initialization stages.
 */
@Component
public class TaskLifecycleProcessor implements BeanPostProcessor {
  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof TaskService || beanName.toLowerCase().contains("repository")) {
      System.out.println("BeanPostProcessor: Before initialization of " + beanName);
    }
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof TaskService || beanName.toLowerCase().contains("repository")) {
      System.out.println("BeanPostProcessor: After initialization of " + beanName);
    }
    return bean;
  }
}