package com.mendel.transactions.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateTransactionSuccessfully() throws Exception {
        String requestBody = """
                {
                    "amount": 5000,
                    "type": "cars"
                }
                """;

        mockMvc.perform(put("/transactions/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void shouldCreateTransactionWithParentId() throws Exception {
        String transactionWithoutParent = """
                {
                    "amount": 5000,
                    "type": "cars"
                }
                """;

        mockMvc.perform(put("/transactions/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionWithoutParent))
                .andExpect(status().isCreated());

        String transactionWithParent = """
                {
                    "amount": 10000,
                    "type": "shopping",
                    "parentId": 100
                }
                """;

        mockMvc.perform(put("/transactions/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionWithParent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void shouldReturnConflictWhenTransactionAlreadyExists() throws Exception {
        String requestBody = """
                {
                    "amount": 5000,
                    "type": "cars"
                }
                """;

        mockMvc.perform(put("/transactions/200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/transactions/200")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void shouldReturnBadRequestWhenAmountIsMissing() throws Exception {
        String requestBody = """
                {
                    "type": "cars"
                }
                """;

        mockMvc.perform(put("/transactions/300")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenTypeIsMissing() throws Exception {
        String requestBody = """
                {
                    "amount": 5000
                }
                """;

        mockMvc.perform(put("/transactions/400")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenAmountIsNegative() throws Exception {
        String requestBody = """
                {
                    "amount": -100,
                    "type": "cars"
                }
                """;

        mockMvc.perform(put("/transactions/500")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
