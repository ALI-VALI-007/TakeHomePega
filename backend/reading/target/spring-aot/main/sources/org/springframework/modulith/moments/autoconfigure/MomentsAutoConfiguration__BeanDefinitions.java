package org.springframework.modulith.moments.autoconfigure;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.moments.support.Moments;
import org.springframework.modulith.moments.support.MomentsProperties;

/**
 * Bean definitions for {@link MomentsAutoConfiguration}.
 */
@Generated
public class MomentsAutoConfiguration__BeanDefinitions {
  /**
   * Get the bean definition for 'momentsAutoConfiguration'.
   */
  public static BeanDefinition getMomentsAutoConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MomentsAutoConfiguration.class);
    beanDefinition.setInstanceSupplier(MomentsAutoConfiguration::new);
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'moments'.
   */
  private static BeanInstanceSupplier<Moments> getMomentsInstanceSupplier() {
    return BeanInstanceSupplier.<Moments>forFactoryMethod(MomentsAutoConfiguration.class, "moments", ObjectProvider.class, ApplicationEventPublisher.class, MomentsProperties.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("org.springframework.modulith.moments.autoconfigure.MomentsAutoConfiguration", MomentsAutoConfiguration.class).moments(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'moments'.
   */
  public static BeanDefinition getMomentsBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(Moments.class);
    beanDefinition.setFactoryBeanName("org.springframework.modulith.moments.autoconfigure.MomentsAutoConfiguration");
    beanDefinition.setInstanceSupplier(getMomentsInstanceSupplier());
    return beanDefinition;
  }
}
