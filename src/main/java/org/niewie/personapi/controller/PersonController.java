package org.niewie.personapi.controller;

import org.niewie.personapi.dto.PersonData;
import org.niewie.personapi.dto.PersonList;
import org.niewie.personapi.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller for exposing REST methods
 * on Person resource
 *
 * @author aniewielska
 * @since 18/07/2018
 */
@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService service;


    public PersonController(PersonService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    PersonList getPersonList() {
        return service.getPersonList();
    }


    @RequestMapping(value = "/{personId}", method = RequestMethod.GET)
    PersonData getPerson(@PathVariable("personId") String personId) {
        return service.getPerson(personId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    PersonData createPerson(@RequestBody @Validated(PersonData.Full.class) PersonData data) {
        return service.createPerson(data);
    }

    @RequestMapping(value = "/{personId}", method = RequestMethod.PUT)
    PersonData updatePerson(@PathVariable("personId") String personId, @RequestBody @Validated(PersonData.Full.class) PersonData data) {
        return service.updatePerson(personId, data);
    }

    /**
     * Allows partial updates, but there is no way to revert null in this way
     * (Nulls are ignored)
     */
    @RequestMapping(value = "/{personId}", method = RequestMethod.PATCH)
    PersonData patchPerson(@PathVariable("personId") String personId, @RequestBody @Valid PersonData data) {
        return service.patchPerson(personId, data);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{personId}", method = RequestMethod.DELETE)
    void deletePerson(@PathVariable("personId") String personId) {
        service.deletePerson(personId);
    }


}
