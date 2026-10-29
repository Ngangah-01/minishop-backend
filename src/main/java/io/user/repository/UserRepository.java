package io.user.repository;

import io.lib.repository.BaseJpaRepository;
import io.user.entity.User;
import io.user.entity.UserStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface UserRepository<U extends User> extends BaseJpaRepository<U> {
    boolean existsByPhoneNumberAndEntityIdNot(String email, String entityId);

    boolean existsByEmailAndEntityIdNot(String email, String entityId);

    boolean existsByNationalIdNumberAndEntityIdNot(String nationalIdNumber, String entityId);

    default Specification<U> userStatusIs(UserStatus userStatus){
        return (root, cb, cq) -> cq.equal(root.get("userStatus"), userStatus);
    }

    default Specification<U> nameLike(String keyWord){
        return (root, cb, cq) -> cq.like(root.get("name"), "%"+keyWord+"%");
    }

    default Specification<U> phoneNumberIs(String phoneNumber){
        return (root, cb, cq) -> cq.equal(root.get("phoneNumber"),phoneNumber);
    }

    default Specification<U> phoneNumberLike(String keyWord){
        return (root, cb, cq) -> cq.like(root.get("phoneNumber"), "%"+keyWord+"%");
    }

    default Specification<U> emailIs(String email){
        return (root, cb, cq) -> cq.equal(root.get("email"), email);
    }

    default Specification<U> emailLike(String keyWord){
        return (root, cb, cq) -> cq.like(root.get("email"), "%"+keyWord+"%");
    }
}
