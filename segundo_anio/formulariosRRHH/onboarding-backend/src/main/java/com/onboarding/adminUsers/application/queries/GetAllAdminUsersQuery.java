package com.onboarding.adminUsers.application.queries;

import com.onboarding.adminUsers.application.dto.AdminUsersDto;
import com.onboarding.shared.cqrs.Query;

import java.util.List;

public class GetAllAdminUsersQuery implements Query<List<AdminUsersDto.Response>> {
}
