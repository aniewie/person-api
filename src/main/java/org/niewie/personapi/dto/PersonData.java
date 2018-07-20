package org.niewie.personapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.niewie.personapi.util.NullOrNotBlank;

import javax.validation.constraints.*;
import javax.validation.groups.Default;

/**
 * @author aniewielska
 * @since 18/07/2018
 *
 * Representation of Person REST Resource
 *
 * Validation Groups -> for partial updates (PATCH) to switch off checking Null
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonData {
    @ApiModelProperty(readOnly = true)
    @JsonProperty("id")
    private String personId;
    @NotBlank(groups = Full.class)
    @NullOrNotBlank(groups = Default.class)
    private String firstName;
    @NotBlank(groups = Full.class)
    @NullOrNotBlank(groups = Default.class)
    private String lastName;
    /*
    This was a nice attempt to distinguish null and not passed in payload value
    But seems incompatible with SpringFox and complicates ModelMapping
    private Optional<Integer> age;
    private Optional<String> favouriteColour;
    */
    @PositiveOrZero(groups = {Full.class, Default.class})
    private Integer age;
    private String favouriteColour;

    public interface Full {
    }

}
