package org.niewie.personapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.niewie.personapi.util.NullOrNotBlank;

import javax.validation.constraints.*;
import javax.validation.groups.Default;

/**
 * @author aniewielska
 * @since 18/07/2018
 */
@Data
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
    private Optional<Integer> age;
    private Optional<String> favouriteColour;
    */
    @PositiveOrZero(groups = {Full.class, Default.class})
    private Integer age;
    private String favouriteColour;

    public interface Full {
    }

}
