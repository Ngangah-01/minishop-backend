package io.orgdepartment.controller;

import io.internaluser.view.OrgDepartmentView;
import io.lib.form.BaseFetchForm;
import io.lib.view.PagedEntityApiResponse;
import io.orgdepartment.service.OrgDepartmentReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL +   "/departments")
public class OrgDepartmentController {
    private OrgDepartmentReadService orgDepartmentReadService;

    @PreAuthorize("hasAuthority('VIEW_ORG_DEPARTMENT')")
    @GetMapping("list")
    public PagedEntityApiResponse<OrgDepartmentView> listDepartments(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize){
        var form = new BaseFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);

        var page = orgDepartmentReadService.listOrgDepartments(form);
        var views = page.stream().map(OrgDepartmentView::new).toList();
        return new PagedEntityApiResponse<>(page, views);
    }

    @Autowired
    public void setOrgDepartmentReadService(OrgDepartmentReadService service) {
        this.orgDepartmentReadService = service;
    }
}
