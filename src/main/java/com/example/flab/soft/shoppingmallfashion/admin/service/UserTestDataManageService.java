package com.example.flab.soft.shoppingmallfashion.admin.service;

import com.example.flab.soft.shoppingmallfashion.admin.dto.CreatedDataInfo;
import com.example.flab.soft.shoppingmallfashion.admin.util.UserGenerator;
import com.example.flab.soft.shoppingmallfashion.user.domain.User;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserTestDataManageService {
    private final UserRepository userRepository;
    private final AdminBatchService adminBatchService;
    private final JdbcTemplate jdbcTemplate;

    public CreatedDataInfo createTestUsers(Integer count) {
        List<User> users = IntStream.range(0, count)
                .mapToObj(UserGenerator::generateUser)
                .toList();
        adminBatchService.bulkInsertUsers(users);

        return CreatedDataInfo.builder()
                .createdCount(userRepository.count())
                .firstElementId(userRepository.findFirstBy().getId())
                .build();
    }

    @Transactional
    public void clearAll() {
        jdbcTemplate.execute("DELETE FROM users");
    }

    public Long count() {
        return userRepository.count();
    }
}
