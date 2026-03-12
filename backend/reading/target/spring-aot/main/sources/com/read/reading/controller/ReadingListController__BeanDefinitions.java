package com.read.reading.controller;

import com.read.reading.service.ReadingListService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ReadingListController}.
 */
@Generated
public class ReadingListController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'readingListController'.
   */
  private static BeanInstanceSupplier<ReadingListController> getReadingListControllerInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<ReadingListController>forConstructor(ReadingListService.class)
            .withGenerator((registeredBean, args) -> new ReadingListController(args.get(0)));
  }

  /**
   * Get the bean definition for 'readingListController'.
   */
  public static BeanDefinition getReadingListControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ReadingListController.class);
    beanDefinition.setInstanceSupplier(getReadingListControllerInstanceSupplier());
    return beanDefinition;
  }
}
