package io.userpermission.form;

import io.lib.form.BaseFetchForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchUserPermissionForm extends BaseFetchForm {
    private String authGroupId;
}
