package org.niewie.personapi.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author aniewielska
 * @since 18/07/2018
 */
@Data
@Entity
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    private String personId;
    private String firstName;
    private String lastName;
    private Integer age;
    private String favouriteColour;

    public void update(Person person) {
        this.update(person, true);
    }

    public void update(Person person, boolean updateNull) {
        if (person.firstName != null || updateNull)
            this.firstName = person.firstName;
        if (person.lastName != null || updateNull)
            this.lastName = person.lastName;
        if (person.age != null || updateNull)
            this.age = person.age;
        if (person.favouriteColour != null || updateNull)
            this.favouriteColour = person.favouriteColour;
    }
}
