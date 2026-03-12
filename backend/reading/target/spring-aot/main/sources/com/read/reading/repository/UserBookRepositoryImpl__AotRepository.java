package com.read.reading.repository;

import com.read.reading.model.UserBook;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Long;
import java.lang.String;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link UserBookRepository}.
 */
@Generated
public class UserBookRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public UserBookRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link UserBookRepository#findById_CognitoUserIdAndId_BookId(java.lang.String,java.lang.Long)}.
   */
  public UserBook findById_CognitoUserIdAndId_BookId(String cognitoUserId, Long bookId) {
    String queryString = "SELECT u FROM UserBook u INNER JOIN u.id i WHERE i.cognitoUserId = :cognitoUserId AND i.bookId = :bookId";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("cognitoUserId", cognitoUserId);
    query.setParameter("bookId", bookId);

    return (UserBook) convertOne(query.getSingleResultOrNull(), false, UserBook.class);
  }

  /**
   * AOT generated implementation of {@link UserBookRepository#findById_CognitoUserIdAndStatus(java.lang.String,com.read.reading.model.UserBook$ReadStatus)}.
   */
  public List<UserBook> findById_CognitoUserIdAndStatus(String cognitoUserId,
      UserBook.ReadStatus status) {
    String queryString = "SELECT u FROM UserBook u INNER JOIN u.id i WHERE i.cognitoUserId = :cognitoUserId AND u.status = :status";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("cognitoUserId", cognitoUserId);
    query.setParameter("status", status);

    return (List<UserBook>) query.getResultList();
  }
}
