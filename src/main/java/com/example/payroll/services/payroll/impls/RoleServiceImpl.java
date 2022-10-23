package com.example.payroll.services.payroll.impls;

import com.example.payroll.dto.RoleModel;
import com.example.payroll.model.payroll.Roles;
import com.example.payroll.repository.payroll.RoleRepository;
import com.example.payroll.services.payroll.RoleService;
import com.example.payroll.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    public RoleModel addNewRoll(RoleModel roleModel) throws Exception{
        Roles roles = new Roles();
        Util.copyProperty(roleModel, roles);
        roles.setActiveStatus("ACTIVE");
        roles = roleRepository.save(roles);
        Util.copyProperty(roles, roleModel);
        return roleModel;
    }
}
