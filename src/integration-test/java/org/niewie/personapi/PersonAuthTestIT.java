package org.niewie.personapi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.niewie.personapi.model.Person;
import org.niewie.personapi.model.PersonRepository;
import org.niewie.personapi.security.filter.JwtAuthenticationFilter;
import org.niewie.personapi.security.jwt.TokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests of authorization
 * Use the same keystore, that in "production", which is not a good practice
 * But it is a demo, so we will leave it as it is
 *
 * Tests are Transactional, so setUp and test each time run in Transaction that is rolled back
 * after the test
 *
 * @author aniewielska
 * @since 20/07/2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc

@TestPropertySource(locations = {"classpath:application.properties"})
public class PersonAuthTestIT {

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private MockMvc mvc;

    @Autowired
    PersonRepository repository;

    private static final String HEADER_KEY = "Authorization";

    @Before
    public void setUp() {
        repository.save(Person.builder()
                .personId("PRS-123")
                .firstName("Joe")
                .lastName("Doe")
                .age(12).build());
        repository.save(Person.builder()
                .personId("PRS-124")
                .firstName("Mary")
                .lastName("Berry")
                .age(122).build());
    }

    @Test
    public void deletePerson_success() throws Exception {
        String token = tokenHandler.generateToken("admin", Arrays.asList("ROLE_ADMIN"));
        mvc.perform(delete("/person/PRS-123")
                .header(JwtAuthenticationFilter.AUTH_HEADER_KEY, token))
                .andExpect(status().isNoContent());
        mvc.perform(get("/person").
                header(HEADER_KEY, token)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.person", hasSize(1)));
    }

    @Test
    public void deletePerson_noPriv() throws Exception {
        String token = tokenHandler.generateToken("user", Arrays.asList("ROLE_USER"));
        mvc.perform(delete("/person/PRS-123")
                .header(JwtAuthenticationFilter.AUTH_HEADER_KEY, token))
                .andExpect(status().isForbidden());
        mvc.perform(get("/person").
                header(HEADER_KEY, token)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.person", hasSize(2)));
    }

    @Test
    public void deletePerson_noAuth() throws Exception {
        String token = tokenHandler.generateToken("user", Arrays.asList("ROLE_USER"));
        mvc.perform(delete("/person/PRS-123"))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/person").
                header(HEADER_KEY, token)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.person", hasSize(2)));
    }

    @Test
    public void deletePerson_notFound() throws Exception {
        String token = tokenHandler.generateToken("user", Arrays.asList("ROLE_ADMIN"));
        mvc.perform(delete("/person/PRS-XXX")
                .header(HEADER_KEY, token))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listPerson_noAuth() throws Exception {
        mvc.perform(get("/person")).andExpect(status().isUnauthorized());
    }

    @Test
    public void listPerson_success() throws Exception {
        String token = tokenHandler.generateToken("user", Arrays.asList("USER"));

        mvc.perform(get("/person").
                header(HEADER_KEY, token)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.person", hasSize(2)));
    }

    @Test
    public void createPerson_noAuth() throws Exception {
        mvc.perform(post("/person").
                content("{\n" +
                        "  \"first_name\": \"Joe\",\n" +
                        "  \"last_name\": \"Doe\",\n" +
                        "  \"favourite_colour\": \"blue\",\n" +
                        "  \"age\": 12\n" +
                        "}")).
                andExpect(status().isUnauthorized());
    }

    @Test
    public void createPerson_noPriv() throws Exception {
        String token = tokenHandler.generateToken("user", Arrays.asList("ROLE_USER"));
        mvc.perform(post("/person").
                header(JwtAuthenticationFilter.AUTH_HEADER_KEY, token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"first_name\": \"Joe\",\n" +
                        "  \"last_name\": \"Doe\",\n" +
                        "  \"favourite_colour\": \"blue\",\n" +
                        "  \"age\": 12\n" +
                        "}")).
                andExpect(status().isForbidden());
    }

    @Test
    public void createPerson_success() throws Exception {
        String token = tokenHandler.generateToken("admin", Arrays.asList("ROLE_ADMIN"));
        mvc.perform(post("/person")
                .header(JwtAuthenticationFilter.AUTH_HEADER_KEY, token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"first_name\": \"Joe\",\n" +
                        "  \"last_name\": \"Doe\",\n" +
                        "  \"favourite_colour\": \"blue\",\n" +
                        "  \"age\": 12\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.first_name", is("Joe")));
    }
}
