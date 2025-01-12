package ru.otus.hw.services;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.stereotype.Service;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Book;

@Service
public class AclServiceCommentService extends AclServiceWrapperServiceImpl{

    private final BookMapper mapper;

    public AclServiceCommentService(MutableAclService mutableAclService, BookMapper mapper) {
        super(mutableAclService);
        this.mapper = mapper;
    }

    @Override
    public void addPermissionForCreate(Object object) {
        throw new UnsupportedOperationException("Use addPermissionForCreate(Object object, Book book)");
    }

    public void addPermissionForCreate(Object object, Book book) {
        super.addPermissionForCreate(object);

        var dtoBook = mapper.toBookDTO(book);
        MutableAcl acl = super.getAcl(dtoBook);
        var owner = acl.getOwner();
        super.addPermission(owner, object, false, BasePermission.READ, BasePermission.DELETE);
    }
}
