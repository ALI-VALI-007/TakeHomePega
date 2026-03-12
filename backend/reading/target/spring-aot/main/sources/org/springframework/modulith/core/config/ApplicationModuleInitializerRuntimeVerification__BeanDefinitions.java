package org.springframework.modulith.core.config;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ApplicationModuleInitializerRuntimeVerification}.
 */
@Generated
public class ApplicationModuleInitializerRuntimeVerification__BeanDefinitions {
  /**
   * Get the bean definition for 'applicationModuleInitializerRuntimeVerification'.
   */
  public static BeanDefinition getApplicationModuleInitializerRuntimeVerificationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ApplicationModuleInitializerRuntimeVerification.class);
    beanDefinition.setInstanceSupplier(ApplicationModuleInitializerRuntimeVerification::new);
    return beanDefinition;
  }
}
