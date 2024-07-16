package com.example.flab.soft.shoppingmallfashion.store.jpa;

import static org.assertj.core.api.Assertions.*;

import com.example.flab.soft.shoppingmallfashion.store.repository.Crew;
import com.example.flab.soft.shoppingmallfashion.store.repository.CrewRepository;
import com.example.flab.soft.shoppingmallfashion.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CrewJpaTest {
    @Autowired
    private CrewRepository crewRepository;
    @Autowired
    private UserRepository userRepository;
    @Value("${authorization.store-manager.id}")
    private Long storeManagerId;

    @Test
    @DisplayName("직원과 역할을 joinfetch하여 가져온다")
    void findCrewWithRolesByUserId() {
        Optional<Crew> crewWithRolesByUserId = crewRepository.findCrewWithRolesByUserId(storeManagerId);
        assertThat(crewWithRolesByUserId.isEmpty()).isFalse();
        assertThat(crewWithRolesByUserId.get().getCrewRoles()).isNotEmpty();
    }
}
