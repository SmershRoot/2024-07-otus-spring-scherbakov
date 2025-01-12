package ru.otus.hw.services;

import org.springframework.security.acls.model.MutableAclService;
import org.springframework.stereotype.Service;

@Service
public class AclServiceBookService extends AclServiceWrapperServiceImpl{

    public AclServiceBookService(MutableAclService mutableAclService) {
        super(mutableAclService);
    }
}
