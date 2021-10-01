package com.example.springbootgraphql.resolver.bank.mutation;

import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UploadFileMutation implements GraphQLMutationResolver {

  public UUID uploadFile(DataFetchingEnvironment environment) {
    log.info("Uploading file");

    DefaultGraphQLServletContext context = environment.getContext();

    // Option to declare the file parts as a variable
    // to buffer them to memory or stream them somewhere
    //    List<Part> fileParts = context.getFileParts();

    context
        .getFileParts()
        .forEach(
            part ->
                log.info("Uploading: {} , size: {}", part.getSubmittedFileName(), part.getSize()));
    return UUID.randomUUID();
  }
}
