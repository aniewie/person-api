package org.niewie.personapi.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.niewie.personapi.dto.PersonData;
import org.niewie.personapi.model.Person;
import org.niewie.personapi.model.PersonRepository;
import org.niewie.personapi.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * @author aniewielska
 * @since 20/07/2018
 */
@RunWith(SpringRunner.class)
public class PersonServiceImplTest {

    @MockBean
    private PersonRepository repository;

    @MockBean
    private IdGenerator idGenerator;

    private PersonService service;

    @Before
    public void setUp() {
        service = new PersonServiceImpl(repository, idGenerator);
    }


    @Test
    public void getPerson() {
        Person entity = Person.builder().
                id(12L).
                personId("PRS-123").
                age(23).
                favouriteColour("blue").
                firstName("John").
                lastName("Doe").build();
        PersonData data = PersonData.builder().
                personId("PRS-123").
                age(23).
                favouriteColour("blue").
                firstName("John").
                lastName("Doe").build();
        given(repository.findByPersonId("PRS-123")).willReturn(Optional.of(entity));
        PersonData dataOut = service.getPerson("PRS-123");
        assertThat(dataOut, is(data));
    }

    @Test
    public void createPerson() {
        Person entityIn = Person.builder().
                personId("PRS-123").
                age(23).
                favouriteColour("blue").
                firstName("John").
                lastName("Doe").build();
        Person entityOut = Person.builder().
                id(12L).
                personId("PRS-123").
                age(23).
                favouriteColour("blue").
                firstName("John").
                lastName("Doe").build();
        PersonData dataIn = PersonData.builder().
                age(23).
                favouriteColour("blue").
                firstName("John").
                lastName("Doe").build();
        PersonData dataOut = PersonData.builder().
                personId("PRS-123").
                age(23).
                favouriteColour("blue").
                firstName("John").
                lastName("Doe").build();
        given(idGenerator.getNext()).willReturn("PRS-123");
        given(repository.save(entityIn)).willReturn(entityOut);
        PersonData result = service.createPerson(dataIn);
        assertThat(result, is(dataOut));

    }

    @Test
    public void updatePerson() {
        Person before = Person.builder().
                id(12L).
                personId("PRS-123").
                age(23).
                favouriteColour("blue").
                firstName("John").
                lastName("Doe").build();
        Person after = Person.builder().
                id(12L).
                personId("PRS-123").
                favouriteColour("green").
                lastName("Don").build();
        PersonData update = PersonData.builder().
                favouriteColour("green").
                firstName(null).
                lastName("Don").build();
        PersonData dataOut = PersonData.builder().
                personId("PRS-123").
                age(null).
                favouriteColour("green").
                firstName(null).
                lastName("Don").build();
        given(repository.findByPersonId("PRS-123")).willReturn(Optional.of(before));
        given(repository.save(after)).willReturn(after);
        PersonData result = service.updatePerson("PRS-123", update);
        assertThat(result, is(dataOut));
    }

    @Test
    public void patchPerson() {
        Person before = Person.builder().
                id(12L).
                personId("PRS-123").
                age(23).
                favouriteColour("blue").
                firstName("John").
                lastName("Doe").build();
        Person after = Person.builder().
                id(12L).
                personId("PRS-123").
                age(23).
                favouriteColour("green").
                firstName("John").
                lastName("Don").build();
        PersonData patch = PersonData.builder().
                favouriteColour("green").
                firstName(null).
                lastName("Don").build();
        PersonData dataOut = PersonData.builder().
                personId("PRS-123").
                age(23).
                favouriteColour("green").
                firstName("John").
                lastName("Don").build();
        given(repository.findByPersonId("PRS-123")).willReturn(Optional.of(before));
        given(repository.save(after)).willReturn(after);
        PersonData result = service.patchPerson("PRS-123", patch);
        assertThat(result, is(dataOut));
    }


}

