package org.springframework.modulith.moments.support;

import java.lang.Class;
import java.lang.String;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RegisteredBean;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.context.properties.ConstructorBound;
import org.springframework.boot.context.properties.bind.BindMethod;

/**
 * Bean definitions for {@link MomentsProperties}.
 */
@Generated
public class MomentsProperties__BeanDefinitions {
  /**
   * Get the bean instance for 'spring.modulith.moments-org.springframework.modulith.moments.support.MomentsProperties'.
   */
  private static MomentsProperties getMomentsPropertiesInstance(RegisteredBean registeredBean) {
    BeanFactory beanFactory = registeredBean.getBeanFactory();
    String beanName = registeredBean.getBeanName();
    Class<?> beanClass = registeredBean.getBeanClass();
    return (MomentsProperties) ConstructorBound.from(beanFactory, beanName, beanClass);
  }

  /**
   * Get the bean definition for 'momentsProperties'.
   */
  public static BeanDefinition getMomentsPropertiesBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MomentsProperties.class);
    beanDefinition.setAttribute("org.springframework.boot.context.properties.bind.BindMethod", BindMethod.VALUE_OBJECT);
    beanDefinition.setInstanceSupplier(InstanceSupplier.of(MomentsProperties__BeanDefinitions::getMomentsPropertiesInstance));
    return beanDefinition;
  }
}
