package com.tradingplatformByTrove.user.mapper;

import com.tradingplatformByTrove.user.dto.CreateUserRequest;
import com.tradingplatformByTrove.user.dto.UserResponse;
import com.tradingplatformByTrove.user.model.User;
import org.springframework.stereotype.Component;

/**
 * Component mapping domain models to DTOs for User scope.
 */
@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request) {
        return User.builder()
                .username(request.getUsername())
                .gemCount(0)
                .totalTradesExecuted(0)
                .build();
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .gemCount(user.getGemCount())
                .totalTradesExecuted(user.getTotalTradesExecuted())
                .rank(user.getRank())
                .build();
    }
}
