package org.niewie.personapi.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.niewie.personapi.dto.PersonData;
import org.niewie.personapi.dto.PersonList;
import org.niewie.personapi.exception.PersonNotFoundException;
import org.niewie.personapi.model.Person;
import org.niewie.personapi.model.PersonRepository;
import org.niewie.personapi.util.IdGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author aniewielska
 * @since 18/07/2018
 */
@Slf4j
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;
    private final ModelMapper mapper;
    private final IdGenerator idGenerator;

    public PersonServiceImpl(PersonRepository repository, ModelMapper mapper, IdGenerator idGenerator) {
        this.repository = repository;
        this.mapper = mapper;
        this.idGenerator = idGenerator;
    }

    @Override
    public PersonData getPerson(String personId) {
        Person personEntity = findPersonByPersonId(personId);
        return mapper.map(personEntity, PersonData.class);
    }

    @Override
    public PersonData createPerson(PersonData person) {
        Person personEntityIn = mapper.map(person, Person.class);
        personEntityIn.setPersonId(idGenerator.getNext());
        Person personEntityOut = repository.save(personEntityIn);
        return mapper.map(personEntityOut, PersonData.class);
    }

    @Override
    public PersonData updatePerson(String personId, PersonData person) {
        Person personEntityToUpdate = findPersonByPersonId(personId);
        personEntityToUpdate.update(mapper.map(person, Person.class));
        Person personEntityUpdated = repository.save(personEntityToUpdate);
        return mapper.map(personEntityUpdated, PersonData.class);
    }

    @Override
    public PersonData patchPerson(String personId, PersonData person) {
        Person personEntityToUpdate = findPersonByPersonId(personId);
        personEntityToUpdate.update(mapper.map(person, Person.class), false);
        Person personEntityUpdated = repository.save(personEntityToUpdate);
        return mapper.map(personEntityUpdated, PersonData.class);
    }

    @Override
    public PersonData deletePerson(String personId) {
        Person personEntityToDelete = findPersonByPersonId(personId);
        repository.delete(personEntityToDelete);
        return mapper.map(personEntityToDelete, PersonData.class);
    }

    @Override
    public PersonList getPersonList() {
        List<Person> personEntityList = repository.findAll();
        List<PersonData> personList = personEntityList.stream().
                map(personEntity -> mapper.map(personEntity, PersonData.class)).
                collect(Collectors.toList());
        return PersonList.builder().personList(personList).build();
    }

    private Person findPersonByPersonId(String personId) {
        return repository.findByPersonId(personId).orElseThrow(() -> new PersonNotFoundException(personId));
    }

}