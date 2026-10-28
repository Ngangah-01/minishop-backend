package io.customer.controller.internal;

import io.customer.form.CustomerRegistrationForm;
import io.customer.form.CustomerUpdateForm;
import io.customer.service.CustomerEditService;
import io.customer.service.CustomerReadService;
import io.customer.validator.RequiredCustomerStatus;
import io.customer.view.CustomerView;
import io.lib.form.RemarksForm;
import io.lib.service.Message;
import io.lib.view.EntityApiResponse;
import io.lib.view.PagedEntityApiResponse;
import io.user.entity.UserStatus;
import io.user.form.FetchUserForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Locale;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@Validated
@RestController
@RequestMapping(INTERNAL_USER_BASE_URL + "/customers")
public class CustomerController {
    private CustomerEditService customerEditService;
    private CustomerReadService customerReadService;

    @PreAuthorize("hasAuthority('REGISTER_CUSTOMER_ACCOUNT')")
    @PostMapping("register")
    public EntityApiResponse<CustomerView> registerCustomerAccount(
        @RequestBody
        @Valid
        CustomerRegistrationForm form,
        Authentication auth,
        Locale locale
    ){
        form.setSessionUserId(auth.getName());
        var customer = customerEditService.register(form);
        return new EntityApiResponse<>(
            Message.get("user.registration.success", locale),
            new CustomerView(customer)
        );
    }

    @PreAuthorize("hasAuthority('EDIT_CUSTOMER_ACCOUNT')")
    @PostMapping("edit/{customerId}")
    public EntityApiResponse<CustomerView> edit(
            @PathVariable
            @RequiredCustomerStatus(statuses = {UserStatus.PENDING_APPROVAL})
            String customerId,
            @RequestBody
            @Valid CustomerUpdateForm form,
            Authentication auth,
            Locale locale){
        form.setSessionUserId(auth.getName());
        var customer = customerEditService.edit(customerId, form);

        return new EntityApiResponse<>(
            Message.get("user.edit.success", locale),
            new CustomerView(customer)
        );
    }

    @PreAuthorize("hasAuthority('ACTIVATE_CUSTOMER_ACCOUNT')")
    @PostMapping("activate/{customerId}")
    public EntityApiResponse<CustomerView> activate(
            @PathVariable
            @RequiredCustomerStatus(statuses = {UserStatus.PENDING_APPROVAL, UserStatus.SUSPENDED})
            String customerId,
            @RequestBody @Valid RemarksForm form,
            Authentication auth,
            Locale locale){
        form.setSessionUserId(auth.getName());
        var user = customerEditService.activate(customerId, form);
        return new EntityApiResponse<>(
            Message.get("user.activation.success", locale),
            new CustomerView(user)
        );
    }

    @PreAuthorize("hasAuthority('SUSPEND_CUSTOMER_ACCOUNT')")
    @PostMapping("suspend/{customerId}")
    public EntityApiResponse<CustomerView> suspend(
            @PathVariable
            @RequiredCustomerStatus(statuses = {UserStatus.ACTIVE, UserStatus.PENDING_APPROVAL})
            String customerId,
            @RequestBody @Valid RemarksForm form,
            Authentication auth,
            Locale locale){
        form.setSessionUserId(auth.getName());
        var user = customerEditService.suspend(customerId, form);
        return new EntityApiResponse<>(
            Message.get("user.suspension.success", locale),
            new CustomerView(user)
        );
    }

    @PreAuthorize("hasAuthority('VIEW_CUSTOMER_ACCOUNT')")
    @GetMapping("list")
    public PagedEntityApiResponse<CustomerView> list(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "userStatus", required = false) UserStatus userStatus,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize
    ){
        var fetchForm = new FetchUserForm();
        fetchForm.setQuery(query);
        fetchForm.setUserStatus(userStatus);
        fetchForm.setStartDate(startDate);
        fetchForm.setEndDate(endDate);
        fetchForm.setPageNum(pageNum);
        fetchForm.setPageSize(pageSize);
        var page = customerReadService.list(fetchForm);
        var views = page.map(CustomerView::new).toList();
        return new PagedEntityApiResponse<>(page, views);
    }

    @Autowired
    public void setCustomerEditService(CustomerEditService service) {
        this.customerEditService = service;
    }

    @Autowired
    public void setCustomerReadService(CustomerReadService service) {
        this.customerReadService = service;
    }
}
