package org.niewie.personapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.niewie.personapi.model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of Person List - only to get "person" label
 *
 * @author aniewielska
 * @since 18/07/2018
 */
@Data
@Builder
public class PersonList {
    @JsonProperty("person")
    private List<PersonData> personList;
}
