package com.tradingplatformByTrove.user.service;

import com.tradingplatformByTrove.common.exception.ResourceNotFoundException;
import com.tradingplatformByTrove.gamification.service.GamificationService;
import com.tradingplatformByTrove.portfolio.service.PortfolioService;
import com.tradingplatformByTrove.user.dto.CreateUserRequest;
import com.tradingplatformByTrove.user.dto.UserResponse;
import com.tradingplatformByTrove.user.mapper.UserMapper;
import com.tradingplatformByTrove.user.model.User;
import com.tradingplatformByTrove.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PortfolioService portfolioService;
    private final GamificationService gamificationService;

    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }

        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);

        // Provision initial portfolio
        portfolioService.createPortfolioForUser(savedUser.getUserId());

        // Recalculate rank leaderboard
        gamificationService.recalculateRanks();

        return userMapper.toResponse(userRepository.findById(savedUser.getUserId()).orElse(savedUser));
    }

    public UserResponse getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return userMapper.toResponse(user);
    }
};