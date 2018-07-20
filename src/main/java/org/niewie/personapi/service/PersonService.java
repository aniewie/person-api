package org.niewie.personapi.service;

import org.niewie.personapi.dto.PersonData;
import org.niewie.personapi.dto.PersonList;

/**
 * Handles CRUD for Person Resource
 * @author aniewielska
 * @since 18/07/2018
 */
public interface PersonService {

    PersonData getPerson(String personId);

    PersonData createPerson(PersonData person);

    PersonData updatePerson(String personId, PersonData person);

    PersonData patchPerson(String personId, PersonData person);

    PersonData deletePerson(String personId);

    PersonList getPersonList();
}
