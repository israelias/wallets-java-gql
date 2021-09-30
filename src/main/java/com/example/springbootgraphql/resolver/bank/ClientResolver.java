package com.example.springbootgraphql.resolver.bank;

import com.example.springbootgraphql.domain.bank.BankAccount;
import com.example.springbootgraphql.domain.bank.Client;
import graphql.GraphQLException;
import graphql.execution.DataFetcherResult;
import graphql.kickstart.execution.error.GenericGraphQLError;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientResolver implements GraphQLResolver<BankAccount> {

    public DataFetcherResult<Client> client(BankAccount bankAccount) {
        log.info("Requesting client data for bank account id {}", bankAccount.getId());


        //        throw new GraphQLException("Client unavailable");
        // throw new RuntimeException("Spring exception can't connect to database: (sql select *)");
        //        return
         return DataFetcherResult.<Client>newResult()
             .data(Client.builder().id(UUID.randomUUID()).firstName("Elias").lastName("Wrubel").build())
             .error(new GenericGraphQLError("Could not get sub-client id"))
             .build();
    }
}
