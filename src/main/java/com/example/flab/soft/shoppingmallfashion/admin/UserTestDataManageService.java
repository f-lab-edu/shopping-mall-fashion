package com.example.flab.soft.shoppingmallfashion.admin;

import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserTestDataManageService {
    private final UserRepository userRepository;
    private final ExecutorService executorService;
    private final JdbcTemplate jdbcTemplate;

    public CreatedDataInfo createTestUsers(Integer count) {
        ConcurrentUtil.collect(IntStream.range(0, count)
                .mapToObj(i -> executorService.submit(() ->
                        userRepository.save(UserGenerator.generateUser(i))))
                .toList());

        return CreatedDataInfo.builder()
                .createdCount(userRepository.count())
                .firstElementId(userRepository.findFirstBy().getId())
                .build();
    }

    @Transactional
    public Long clearAll() {
        jdbcTemplate.execute("DELETE FROM users");
        userRepository.deleteAll();
        return userRepository.count();
    }
}
