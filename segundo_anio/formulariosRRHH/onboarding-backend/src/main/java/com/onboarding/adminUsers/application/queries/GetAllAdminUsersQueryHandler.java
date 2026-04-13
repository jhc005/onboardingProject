package com.onboarding.adminUsers.application.queries;

import com.onboarding.adminUsers.application.dto.AdminUsersDto;
import com.onboarding.adminUsers.domain.outport.AdminUsersRepositoryPort;
import com.onboarding.adminUsers.mapper.AdminUsersMapper;
import com.onboarding.shared.cqrs.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllAdminUsersQueryHandler implements QueryHandler<GetAllAdminUsersQuery, List<AdminUsersDto.Response>> {

    private final AdminUsersRepositoryPort adminUsersPort;
    private final AdminUsersMapper mapper;

    @Override
    public List<AdminUsersDto.Response> handle(GetAllAdminUsersQuery query) {
        return adminUsersPort.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }
}
