package org.niewie.personapi;

import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.niewie.personapi.security.jwt.TokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests of authorization
 * Use the same keystore, that in "production", which is not a good practice
 * But it is a demo, so we will leave it as it is
 *
 * @author aniewielska
 * @since 20/07/2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:application.properties"})
public class PersonAuthTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private MockMvc mvc;

    private static final String HEADER_KEY = "Authorization";

    @Test
    public void getToken_unauthorized() {
        HttpStatus status = restTemplate.getForEntity("/token", String.class).getStatusCode();
        assertThat(status, is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void getToken_user() {
        String token = restTemplate.withBasicAuth("user", "password").
                getForEntity("/token", String.class).getBody();
        Claims claims = tokenHandler.verifyToken(token);
        assertThat(claims.getSubject(), is("user"));
    }

    @Test
    public void getToken_admin() {
        String token = restTemplate.withBasicAuth("admin", "password").
                getForEntity("/token", String.class).getBody();
        Claims claims = tokenHandler.verifyToken(token);
        assertThat(claims.getSubject(), is("admin"));
    }

    @Test
    public void getToken_wrongPassword() {
        HttpStatus status = restTemplate.withBasicAuth("admin", "wrong_password").
                getForEntity("/token", String.class).getStatusCode();
        assertThat(status, is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void getToken_noUser() {
        HttpStatus status = restTemplate.withBasicAuth("else", "password").
                getForEntity("/token", String.class).getStatusCode();
        assertThat(status, is(HttpStatus.UNAUTHORIZED));
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
                andExpect(status().isOk());
    }

}
