package io.orgbranch.form;

import io.lib.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

@Getter
@Setter
public class OrgBranchEditForm extends SessionUserIdForm {
    @NotBlank(message = "error.invalid.name")
    private String name;

    private String createdById;

    public String getName() {
        return WordUtils.capitalize(StringUtils.trimToEmpty(name));
    }
}
