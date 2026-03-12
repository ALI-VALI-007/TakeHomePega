package com.read.reading.service;

import com.read.reading.repository.BookRepository;
import com.read.reading.repository.UserBookRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ReadingListService}.
 */
@Generated
public class ReadingListService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'readingListService'.
   */
  private static BeanInstanceSupplier<ReadingListService> getReadingListServiceInstanceSupplier() {
    return BeanInstanceSupplier.<ReadingListService>forConstructor(BookRepository.class, UserBookRepository.class)
            .withGenerator((registeredBean, args) -> new ReadingListService(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'readingListService'.
   */
  public static BeanDefinition getReadingListServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ReadingListService.class);
    beanDefinition.setInstanceSupplier(getReadingListServiceInstanceSupplier());
    return beanDefinition;
  }
}
