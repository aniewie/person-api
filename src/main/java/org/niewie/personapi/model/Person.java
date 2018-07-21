package org.niewie.personapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author aniewielska
 * @since 18/07/2018
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PERSON")
@Entity
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    //@Column(name = "PERSON_ID")
    private String personId;
    //@Column(name = "FIRST_NAME")
    private String firstName;
    //@Column(name = "LAST_NAME")
    private String lastName;
    //@Column(name = "AGE")
    private Integer age;
    //@Column(name = "FAVOURITE_COLOUR")
    private String favouriteColour;

    /**
     * Updates "modifiable" properties (arbitrary list), permits setting nulls
     */

    public void update(Person person) {
        this.update(person, true);
    }

    /**
     * @param person - entity with updated values
     * @param updateNull - if true may update property values to null
     */
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
