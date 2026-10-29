package io.activitylog.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateActivityLogForm {
    @NotBlank(message = "error.empty.id")
    private String owningEntityId;

    private String sessionUserId;
    private String action;
    private String remarks;
}
