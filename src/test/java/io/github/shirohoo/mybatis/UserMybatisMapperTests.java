package io.github.shirohoo.mybatis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserMybatisMapperTests {
    @Autowired
    UserMybatisMapper sut;

    @BeforeEach
    void setUp() {
        sut.save("test_user", "pwd");
    }

    @Test
    void test_dynamic_query() {
        User effect = sut.findByUsernameAndPassword("test_user", null);

        assertThat(effect)
                .extracting(User::getId, User::getUsername, User::getPassword)
                .containsExactly(1L, "test_user", "pwd");
    }

    @AfterEach
    void tearDown() {
        sut.deleteById(1L);
    }
}
