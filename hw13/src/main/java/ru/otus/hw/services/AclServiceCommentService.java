package ru.otus.hw.services;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Book;

@Service
public class AclServiceCommentService extends AclServiceWrapperServiceImpl {

    public AclServiceCommentService(MutableAclService mutableAclService) {
        super(mutableAclService);
    }

    @Override
    public void addPermissionForCreate(Object object) {
        throw new UnsupportedOperationException("Use addPermissionForCreate(Object object, Book book)");
    }

    public void addPermissionForCreate(Object object, Book book) {
        super.addPermissionForCreate(object);

        MutableAcl acl = super.getAcl(book);
        var owner = acl.getOwner();
        super.addPermission(owner, object, BasePermission.READ, BasePermission.DELETE);
    }
}
