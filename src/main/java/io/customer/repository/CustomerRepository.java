package io.customer.repository;

import io.customer.entity.Customer;
import io.user.repository.UserRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends UserRepository<Customer> {
    default Specification<Customer> taxPinIs(String taxPin){
        return ((root, cq, cb) -> cb.equal(root.get("taxPin"), taxPin));
    }

    default Specification<Customer> nationalIdIs(String nationalId){
        return ((root, cq, cb) -> cb.equal(root.get("nationalId"), nationalId));
    }

    default Specification<Customer> nationalIdLike(String query){
        return ((root, cq, cb) -> cb.like(root.get("nationalId"), "%" + query + "%"));
    }
}

