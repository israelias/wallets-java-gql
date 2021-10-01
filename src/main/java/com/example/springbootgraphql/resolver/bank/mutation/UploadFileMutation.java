package com.example.springbootgraphql.resolver.bank.mutation;

import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * UploadFileMutation
 *
 * <p>Currently logs a stream of the uploaded data for development purposes. Can be tested on
 * Postman using form-data with the following body as a `POST` request to {@url
 * "localhost:8080/graphql"}:
 *
 * <dl>
 *   <dt>operations (as text key)
 *   <dd>{@code "query": "mutation { uploadFile }"; "variables": {}} (as value)
 *   <dt>file (as text key)
 *   <dd>Upload-File.txt (file value)
 * </dl>
 */
@Slf4j
@Component
public class UploadFileMutation implements GraphQLMutationResolver {

  public UUID uploadFile(DataFetchingEnvironment environment) {
    log.info("Uploading file");

    DefaultGraphQLServletContext context = environment.getContext();

    /**
     * Alternative Option is to declare the file parts as a variable to buffer them to memory or
     * stream them somewhere i.e. {@code List<Part> fileParts = context.getFileParts();}
     */
    context
        .getFileParts()
        .forEach(
            part ->
                log.info("Uploading: {} , size: {}", part.getSubmittedFileName(), part.getSize()));
    return UUID.randomUUID();
  }
}
