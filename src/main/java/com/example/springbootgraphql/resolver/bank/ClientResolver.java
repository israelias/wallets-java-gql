package com.example.springbootgraphql.resolver.bank;

import com.example.springbootgraphql.domain.bank.BankAccount;
import com.example.springbootgraphql.domain.bank.Client;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientResolver implements GraphQLResolver<BankAccount> {

    public Client client(BankAccount bankAccount) {
        log.info("Requesting client data for bank account id {}", bankAccount.getId());

        return Client.builder().id(UUID.randomUUID()).firstName("Elias").lastName("Wrubel").build();
    }
}
