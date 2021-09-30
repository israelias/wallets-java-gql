package com.example.springbootgraphql.resolver.bank;

import com.example.springbootgraphql.domain.bank.Asset;
import com.example.springbootgraphql.domain.bank.BankAccount;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AssetResolver implements GraphQLResolver<BankAccount> {
    private static final ExecutorService executorService =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public CompletableFuture<List<Asset>> assets(BankAccount bankAccount) {
        return CompletableFuture.supplyAsync(
                () -> {
                    log.info("Getting assets for bank account id {}", bankAccount.getId());
                    return List.of();
                },
                executorService);
    }
}
