package io.internaluser.controller;

import io.internaluser.form.InternalUserFetchForm;
import io.internaluser.service.InternalUserReadService;
import io.internaluser.view.InternalUserView;
import io.lib.view.PagedEntityApiResponse;
import io.user.entity.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/internal-users")
public class InternalUserController {

    private InternalUserReadService internalUserReadService;

    @RequestMapping("list")
    public PagedEntityApiResponse<InternalUserView> listInternalUsers(
        @RequestParam(value = "query", required = false) String query,
        @RequestParam(value = "status", required = false) UserStatus status,
        @RequestParam(value = "departmentInfo", required = false) String departmentInfo,
        @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
        @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize,
        Authentication auth
    ){
        var form = new InternalUserFetchForm();
        form.setQuery(query);
        form.setUserStatus(status);
        form.setDepartmentInfo(departmentInfo);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = internalUserReadService.listUsers(form);
        var views = page.stream()
                .map(InternalUserView::new)
                .toList();
        return new PagedEntityApiResponse<>(page, views);
    }

    @Autowired
    public void setInternalUserReadService(InternalUserReadService internalUserReadService) {
        this.internalUserReadService = internalUserReadService;
    }
}
