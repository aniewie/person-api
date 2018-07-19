package org.niewie.personapi.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author aniewielska
 * @since 18/07/2018
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByPersonId(String personId);

}
