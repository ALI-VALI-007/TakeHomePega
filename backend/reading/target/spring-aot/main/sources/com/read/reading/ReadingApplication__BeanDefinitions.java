package com.read.reading;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ReadingApplication}.
 */
@Generated
public class ReadingApplication__BeanDefinitions {
  /**
   * Get the bean definition for 'readingApplication'.
   */
  public static BeanDefinition getReadingApplicationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ReadingApplication.class);
    beanDefinition.setInstanceSupplier(ReadingApplication::new);
    return beanDefinition;
  }
}
