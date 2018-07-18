package org.niewie.personapi.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author aniewielska
 * @since 18/07/2018
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
}
