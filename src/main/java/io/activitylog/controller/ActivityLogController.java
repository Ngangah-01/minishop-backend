package io.activitylog.controller;

import io.activitylog.form.FetchActivityLogForm;
import io.activitylog.service.ActivityLogReadService;
import io.activitylog.view.ActivityLogView;
import io.lib.view.PagedEntityApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;


@RestController
@RequestMapping({INTERNAL_USER_BASE_URL + "/activity-logs"})
public class ActivityLogController {
    private ActivityLogReadService activityLogReadService;

    @GetMapping(value = "list/{owningEntityId}")
    public PagedEntityApiResponse<ActivityLogView> list(
        @PathVariable String owningEntityId,
        @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
        @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize){

        var form = new FetchActivityLogForm();
        form.setOwningEntityId(owningEntityId);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);

        var page = activityLogReadService.listActivityLogs(form);

        var views = page.stream()
            .map(ActivityLogView::new)
            .toList();

        return new PagedEntityApiResponse<>(page, views);
    }

    @Autowired
    public void setActivityLogReadService(ActivityLogReadService activityLogReadService) {
        this.activityLogReadService = activityLogReadService;
    }
}

