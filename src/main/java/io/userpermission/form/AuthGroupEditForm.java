package io.userpermission.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.Set;

@Getter
@Setter
public class AuthGroupEditForm {

    @NotBlank(message = "error.empty.name")
    protected String name;

    @NotBlank(message = "error.invalid.description")
    private String description;

    @NotEmpty(message = "error.empty.permissions")
    private Set<String> permissionIds;

    private String sessionUserId;

    public String getName() {
        return StringUtils.capitalize(
            name.trim().replaceAll(" +", " ").toLowerCase()
        );
    }
}
