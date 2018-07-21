package org.niewie.personapi.controller.person;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.niewie.personapi.controller.person.PersonController;
import org.niewie.personapi.dto.PersonData;
import org.niewie.personapi.exception.PersonNotFoundException;
import org.niewie.personapi.service.PersonService;
import org.niewie.personapi.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author aniewielska
 * @since 20/07/2018
 */
@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private IdGenerator idGenerator;

    @Test
    public void getPersonList() {
    }

    @Test
    @WithMockUser
    public void getPerson_success() throws Exception {
        PersonData person = PersonData.builder().
                personId("p1").
                firstName("Joe").
                lastName("Doe").
                build();
        given(this.personService.getPerson("p1")).willReturn(person);
        this.mvc.perform(get("/person/p1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name", is("Joe")))
                .andExpect(jsonPath("$.last_name", is("Doe")))
                .andExpect(jsonPath("$.id", is("p1")));
    }

    @Test
    @WithMockUser
    public void getPerson_notFound() throws Exception {
        given(this.personService.getPerson("p1")).willThrow(new PersonNotFoundException("p1"));
        this.mvc.perform(get("/person/p1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void createPerson_success() throws Exception {
        PersonData personIn = PersonData.builder().
                firstName("Joe").
                lastName("Doe").
                favouriteColour("blue").
                age(12).
                build();
        PersonData personOut = PersonData.builder().
                personId("p1").
                firstName("Joe").
                lastName("Doe").
                favouriteColour("blue").
                age(12).
                build();
        given(this.personService.createPerson(personIn)).willReturn(personOut);
        given(this.idGenerator.getNext()).willReturn("p1");
        this.mvc.perform(post("/person")
                .with(csrf()) //this is ugly, but the least verbose way to ignore security
                .content("{\n" +
                        "  \"first_name\": \"Joe\",\n" +
                        "  \"last_name\": \"Doe\",\n" +
                        "  \"favourite_colour\": \"blue\",\n" +
                        "  \"age\": 12\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.first_name", is("Joe")))
                .andExpect(jsonPath("$.last_name", is("Doe")))
                .andExpect(jsonPath("$.favourite_colour", is("blue")))
                .andExpect(jsonPath("$.id", is("p1")));
    }

    @Test
    @WithMockUser
    public void createPerson_wrongAge() throws Exception {

        this.mvc.perform(post("/person")
                .with(csrf())
                .content("{\n" +
                        "  \"first_name\": \"Joe\",\n" +
                        "  \"last_name\": \"Doe\",\n" +
                        "  \"favourite_colour\": \"blue\",\n" +
                        "  \"age\": -12\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]", containsString("age")));
    }

    @Test
    @WithMockUser
    public void createPerson_noFirstName() throws Exception {

        this.mvc.perform(post("/person")
                .with(csrf())
                .content("{\n" +
                        "  \"last_name\": \"Doe\",\n" +
                        "  \"favourite_colour\": \"blue\",\n" +
                        "  \"age\": 12\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]", containsString("firstName")));
    }

    @Test
    @WithMockUser
    public void createPerson_firstNameNull() throws Exception {

        this.mvc.perform(post("/person")
                .with(csrf())
                .content("{\n" +
                        "  \"first_name\": null,\n" +
                        "  \"last_name\": \"Doe\",\n" +
                        "  \"favourite_colour\": \"blue\",\n" +
                        "  \"age\": 12\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]", containsString("firstName")));
    }


    @Test
    @WithMockUser
    public void updatePerson_success() throws Exception {
        PersonData personIn = PersonData.builder().
                firstName("Joe").
                lastName("Doe").
                favouriteColour("blue").
                age(12).
                build();
        PersonData personOut = PersonData.builder().
                personId("p1").
                firstName("Joe*").
                lastName("Doe*").
                favouriteColour("blue*").
                age(12).
                build();
        given(this.personService.updatePerson("p1", personIn)).willReturn(personOut);
        this.mvc.perform(put("/person/p1")
                .with(csrf())
                .content("{\n" +
                        "  \"first_name\": \"Joe\",\n" +
                        "  \"last_name\": \"Doe\",\n" +
                        "  \"favourite_colour\": \"blue\",\n" +
                        "  \"age\": 12\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name", is("Joe*")))
                .andExpect(jsonPath("$.last_name", is("Doe*")))
                .andExpect(jsonPath("$.favourite_colour", is("blue*")))
                .andExpect(jsonPath("$.id", is("p1")));
    }

    @Test
    @WithMockUser
    public void updatePerson_success_noAge() throws Exception {
        PersonData personIn = PersonData.builder().
                firstName("Joe").
                lastName("Doe").
                favouriteColour("blue").
                build();
        PersonData personOut = PersonData.builder().
                personId("p1").
                firstName("Joe*").
                lastName("Doe*").
                favouriteColour("blue*").
                build();
        given(this.personService.updatePerson("p1", personIn)).willReturn(personOut);
        this.mvc.perform(put("/person/p1")
                .with(csrf())
                .content("{\n" +
                        "  \"first_name\": \"Joe\",\n" +
                        "  \"last_name\": \"Doe\",\n" +
                        "  \"favourite_colour\": \"blue\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name", is("Joe*")))
                .andExpect(jsonPath("$.last_name", is("Doe*")))
                .andExpect(jsonPath("$.age", is(nullValue())))
                .andExpect(jsonPath("$.id", is("p1")));
    }

    @Test
    @WithMockUser
    public void updatePerson_notBlankCheck() throws Exception {
        this.mvc.perform(put("/person/p1")
                .with(csrf())
                .content("{\n" +
                        "  \"first_name\": \"Joe\",\n" +
                        "  \"favourite_colour\": \"blue\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]", containsString("lastName")));

    }

    @Test
    @WithMockUser
    public void patchPerson_success() throws Exception {
        PersonData personIn = PersonData.builder().
                firstName("Joe").
                lastName("Doe").
                favouriteColour("blue").
                build();
        PersonData personOut = PersonData.builder().
                personId("p1").
                firstName("Joe*").
                lastName("Doe*").
                favouriteColour("blue*").
                build();
        given(this.personService.patchPerson("p1", personIn)).willReturn(personOut);
        this.mvc.perform(patch("/person/p1")
                .with(csrf())
                .content("{\n" +
                        "  \"first_name\": \"Joe\",\n" +
                        "  \"last_name\": \"Doe\",\n" +
                        "  \"favourite_colour\": \"blue\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name", is("Joe*")))
                .andExpect(jsonPath("$.last_name", is("Doe*")))
                .andExpect(jsonPath("$.age", is(nullValue())))
                .andExpect(jsonPath("$.id", is("p1")));
    }

    @Test
    @WithMockUser
    public void patchPerson_notBlankIgnored() throws Exception {
        PersonData personIn = PersonData.builder().
                firstName("Joe").
                favouriteColour("blue").
                build();
        PersonData personOut = PersonData.builder().
                personId("p1").
                firstName("Joe*").
                lastName("Doe*").
                favouriteColour("blue*").
                build();
        given(this.personService.patchPerson("p1", personIn)).willReturn(personOut);
        this.mvc.perform(patch("/person/p1")
                .with(csrf())
                .content("{\n" +
                        "  \"first_name\": \"Joe\",\n" +
                        "  \"favourite_colour\": \"blue\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name", is("Joe*")));

    }

    @Test
    @WithMockUser
    public void patchPerson_ageCheck() throws Exception {
        this.mvc.perform(patch("/person/p1")
                .with(csrf())
                .content("{\n" +
                        "  \"first_name\": \"Joe\",\n" +
                        "  \"age\": \"-100\",\n" +
                        "  \"favourite_colour\": \"blue\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]", containsString("age")));

    }

    @Test
    @WithMockUser
    public void deletePerson() throws Exception {
        PersonData personOut = PersonData.builder().
                personId("p1").
                build();
        given(this.personService.deletePerson("p1")).willReturn(personOut);
        this.mvc.perform(delete("/person/p1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

}