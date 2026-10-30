package io.orgbranch.controller;

import io.lib.form.BaseFetchForm;
import io.lib.service.Message;
import io.lib.view.EntityApiResponse;
import io.lib.view.PagedEntityApiResponse;
import io.orgbranch.form.OrgBranchEditForm;
import io.orgbranch.form.OrgBranchRegistrationForm;
import io.orgbranch.service.OrgBranchEditService;
import io.orgbranch.service.OrgBranchReadService;
import io.orgbranch.view.OrgBranchView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/org-branches")
public class OrgBranchController {
    private OrgBranchEditService orgBranchEditService;
    private OrgBranchReadService orgBranchReadService;

    @PreAuthorize("hasAuthority('REGISTER_ORG_BRANCH')")
    @PostMapping("register")
    public EntityApiResponse<OrgBranchView> registerBranch(
            @RequestBody @Valid OrgBranchRegistrationForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var orgBranch = orgBranchEditService.registerOrgBranch(form);
        return new EntityApiResponse<>(
                Message.get("branch.registration.success", locale),
                new OrgBranchView(orgBranch)
        );
    }

    @PreAuthorize("hasAuthority('EDIT_ORG_BRANCH')")
    @PostMapping("update/{orgBranchId}")
    public EntityApiResponse<OrgBranchView> update(
            @RequestBody @Valid OrgBranchEditForm form,
            @PathVariable String orgBranchId,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var orgBranch = orgBranchEditService.updateOrgBranch(form,orgBranchId);
        return new EntityApiResponse<>(
                Message.get("branch.edit.success", locale),
                new OrgBranchView(orgBranch)
        );
    }

    @PreAuthorize("hasAuthority('VIEW_ORG_BRANCH')")
    @GetMapping("list")
    public PagedEntityApiResponse<OrgBranchView> list(
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize,
            @RequestParam(value = "query", required = false) String query) {
        var form = new BaseFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        var page = orgBranchReadService.listOrgBranches(form);
        var views = page.stream().map(OrgBranchView::new).toList();
        return new PagedEntityApiResponse<>(page, views);
    }

    @Autowired
    public void setOrgBranchEditService(OrgBranchEditService orgBranchEditService) {
        this.orgBranchEditService = orgBranchEditService;
    }

    @Autowired
    public void setOrgBranchReadService(OrgBranchReadService orgBranchReadService) {
        this.orgBranchReadService = orgBranchReadService;
    }
}
