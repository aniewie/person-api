package org.niewie.personapi.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author aniewielska
 * @since 18/07/2018
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    /**
     * Find by external ID of a resource
     */
    Optional<Person> findByPersonId(String personId);

}
