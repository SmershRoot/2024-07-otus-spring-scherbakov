package ru.otus.hw.services;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import ru.otus.hw.models.Role;
import ru.otus.hw.models.SystemUser;

public interface AclServiceWrapperService {

    void addPermission(SystemUser user, Object object, boolean sidIsOwner, Permission... permission);

    void addPermission(Role role, Object object, Permission... permission);

    void addPermission(Sid sid, Object object, boolean sidIsOwner, Permission ... permissions);

    void deleteAllPermission(Object object);

    void addPermissionForCreate(Object object);
}
