package io.userpermission.controller;

import io.lib.form.BaseFetchForm;
import io.lib.view.PagedEntityApiResponse;
import io.userpermission.model.UserPermission;
import io.userpermission.service.UserPermissionReadService;
import io.userpermission.view.UserPermissionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/permissions")
public class PermissionController {
    private UserPermissionReadService userPermissionReadService;

    @GetMapping("list")
    public PagedEntityApiResponse<UserPermissionView> listPermissions(
        @RequestParam(value = "query", required = false) String query,
        @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
        @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize
    ){
        var form = new BaseFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);

        var page = userPermissionReadService.listUserPermissions(form);
        var views = page.stream()
            .sorted(Comparator.comparing(UserPermission::getPermissionName))
            .map(UserPermissionView::new)
            .toList();
        return new PagedEntityApiResponse<>(page, views);
    }

    @Autowired
    public void setUserPermissionReadService(UserPermissionReadService service) {
        this.userPermissionReadService = service;
    }
}
