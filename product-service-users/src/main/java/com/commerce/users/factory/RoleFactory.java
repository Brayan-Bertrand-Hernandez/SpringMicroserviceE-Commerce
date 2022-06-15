	package com.commerce.users.factory;

import java.util.List;

import com.commerce.users.model.Role;

public interface RoleFactory {
	
	List<Role> findAllAuthorities();
	
}
