package io.customer.service;


import io.customer.entity.Customer;
import io.customer.repository.CustomerRepository;
import io.user.form.FetchUserForm;
import io.user.service.BaseUserReadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class CustomerReadService extends BaseUserReadService<Customer, CustomerRepository> {

    public Page<Customer> list(FetchUserForm form){
        var spec = repository.notDeleted()
                .and(repository.createAtBetween(form.getStartDate(), form.getEndDate()));

        if(form.getUserStatus() != null){
            spec = spec.and(repository.userStatusIs(form.getUserStatus()));
        }

        if(StringUtils.isNotBlank(form.getQuery())){
            spec = spec.and(
                repository.nameLike(form.getQuery())
                .or(repository.phoneNumberLike(form.getQuery()))
                .or(repository.emailLike(form.getQuery()))
                .or(repository.nationalIdLike(form.getQuery()))
            );
        }

        return repository.findAll(spec, repository.defaultPageable(form));
    }

    public Customer findByNationalId(String nationalId){
        var spec = repository.notDeleted()
                .and(repository.nationalIdIs(nationalId));
        return repository.findOne(spec).orElse(null);
    }

    public boolean existsByNationalId(String nationalId){
        var spec = repository.notDeleted()
                .and(repository.nationalIdIs(nationalId));
        return repository.exists(spec);
    }
}
