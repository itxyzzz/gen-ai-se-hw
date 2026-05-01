package com.setu.banking.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
abstract class ApiIntegrationTestSupport {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected TransactionRepository repository;

    @BeforeEach
    void clearRepository() {
        repository.clear();
    }
}
