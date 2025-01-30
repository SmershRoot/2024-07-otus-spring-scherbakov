package ru.otus.hw.services;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Role;
import ru.otus.hw.models.SystemUser;

@Service
public class AclServiceWrapperServiceImpl implements AclServiceWrapperService {

    private static final String ROLE_USER = "ROLE_USER";

    private final MutableAclService mutableAclService;

    public AclServiceWrapperServiceImpl(MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
    }

    @Override
    public void addPermission(SystemUser user, Object object, Permission... permission) {
        Sid sidUser = new PrincipalSid(user.getUsername().toLowerCase());
        addPermission(sidUser, object, permission);
    }

    @Override
    public void addPermission(Role role, Object object, Permission... permission) {
        Sid sidRole = new GrantedAuthoritySid(role.getName().toUpperCase());
        addPermission(sidRole, object, permission);
    }

    @Override
    public void addPermission(Sid sid, Object object, Permission ... permissions) {
        MutableAcl acl = getAcl(object);
        for (Permission permission : permissions) {
            acl.insertAce(acl.getEntries().size(), permission, sid, true);
        }
        mutableAclService.updateAcl(acl);
    }

    public MutableAcl getAcl(Object object) {
        ObjectIdentity oid = new ObjectIdentityImpl(object);
        MutableAcl acl;
        try {
            acl = (MutableAcl) mutableAclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = mutableAclService.createAcl(oid);
        }
        return acl;
    }

    @Override
    public void deleteAllPermission(Object object) {
        ObjectIdentity oid = new ObjectIdentityImpl(object);
        mutableAclService.deleteAcl(oid, true);
    }

    @Override
    public void addPermissionForCreate(Object object) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Sid owner = new PrincipalSid(authentication);
        Sid userRole = new GrantedAuthoritySid(ROLE_USER);
        addPermission(owner, object, BasePermission.READ, BasePermission.WRITE, BasePermission.DELETE);
        addPermission(userRole, object, BasePermission.READ);
    }

}