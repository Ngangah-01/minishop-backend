package io.activitylog.form;

import io.lib.form.BaseDatedFetchForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchActivityLogForm extends BaseDatedFetchForm {
   private String owningEntityId;
}
