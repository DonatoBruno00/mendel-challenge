package com.mendel.transactions.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerIntegrationTest {

    private static final String ELECTRONICS_TYPE = "electronics";
    private static final String UNKNOWN_TYPE = "unknown";

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

    @Test
    void shouldReturnTransactionIdsByType() throws Exception {
        String electronicsTransaction1 = """
                {
                    "amount": 5000,
                    "type": "electronics"
                }
                """;

        String electronicsTransaction2 = """
                {
                    "amount": 3000,
                    "type": "electronics"
                }
                """;

        mockMvc.perform(put("/transactions/600")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(electronicsTransaction1))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/transactions/601")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(electronicsTransaction2))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/transactions/types/" + ELECTRONICS_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions.length()").value(2));
    }

    @Test
    void shouldReturnEmptyListWhenNoTransactionsOfType() throws Exception {
        mockMvc.perform(get("/transactions/types/" + UNKNOWN_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions.length()").value(0));
    }

    @Test
    void shouldReturnTransactionSumWithNoChildren() throws Exception {
        String transaction = """
                {
                    "amount": 5000,
                    "type": "cars"
                }
                """;

        mockMvc.perform(put("/transactions/700")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transaction))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/transactions/sum/700"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value(5000));
    }

    @Test
    void shouldReturnTransitiveSumWithChildren() throws Exception {
        String parent = """
                {
                    "amount": 10000,
                    "type": "cars"
                }
                """;

        String child = """
                {
                    "amount": 5000,
                    "type": "shopping",
                    "parentId": 800
                }
                """;

        mockMvc.perform(put("/transactions/800")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parent))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/transactions/801")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(child))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/transactions/sum/800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value(15000));
    }

    @Test
    void shouldReturnZeroWhenTransactionNotFound() throws Exception {
        mockMvc.perform(get("/transactions/sum/9999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value(0.0));
    }
}
