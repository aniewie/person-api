package org.niewie.personapi.service;

import org.niewie.personapi.dto.PersonData;
import org.niewie.personapi.dto.PersonList;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Handles CRUD for Person Resource
 * @author aniewielska
 * @since 18/07/2018
 */
public interface PersonService {

    PersonData getPerson(String personId);

    @PreAuthorize("hasRole('ADMIN')")
    PersonData createPerson(PersonData person);

    @PreAuthorize("hasRole('ADMIN')")
    PersonData updatePerson(String personId, PersonData person);

    PersonData patchPerson(String personId, PersonData person);

    @PreAuthorize("hasRole('ADMIN')")
    PersonData deletePerson(String personId);

    PersonList getPersonList();
}
