package org.niewie.personapi.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.niewie.personapi.dto.PersonData;
import org.niewie.personapi.model.Person;
import org.niewie.personapi.model.PersonRepository;
import org.niewie.personapi.util.IdGenerator;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * @author aniewielska
 * @since 20/07/2018
 */
public class PersonServiceImplTest {


    private PersonRepository repository;

    private IdGenerator idGenerator;

    private PersonService service;

    @Before
    public void setUp() {
        repository = Mockito.mock(PersonRepository.class);
        idGenerator = Mockito.mock(IdGenerator.class);
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

