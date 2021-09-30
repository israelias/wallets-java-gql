package com.example.springbootgraphql.resolver.bank;

import com.example.springbootgraphql.domain.bank.BankAccount;
import com.example.springbootgraphql.domain.bank.Client;
import graphql.GraphQLException;
import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientResolver implements GraphQLResolver<BankAccount> {

    private final ExecutorService executorService =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public CompletableFuture<Client> client(BankAccount bankAccount) {
        log.info("Stop me debugging");

        return CompletableFuture.supplyAsync(
                () -> {
                    log.info("Requesting client data for bank account id {}", bankAccount.getId());
                    return Client.builder()
                            .id(UUID.randomUUID())
                            .firstName("Elias")
                            .lastName("Wrubel")
                            .build();
                },
                executorService);
    }
}
