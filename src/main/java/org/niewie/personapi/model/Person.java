package org.niewie.personapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author aniewielska
 * @since 18/07/2018
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
