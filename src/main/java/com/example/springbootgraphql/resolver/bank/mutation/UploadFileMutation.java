package com.example.springbootgraphql.resolver.bank.mutation;

import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>The {@code UploadFileMutation} class is a Spring component that implements the {@link GraphQLMutationResolver} interface, enabling it to handle GraphQL mutations for file uploads.
 * This class is annotated with {@link Slf4j} for logging and {@link Component} to indicate that it is a Spring-managed bean.
 * <p>Overall, the {@code UploadFileMutation} class provides a way to handle file uploads
 *
 * <p>Notes
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

  /**
   * <p>The methHod {@code uploadFile} handles the file upload mutation. It takes a {@link DataFetchingEnvironment} parameter,
   * which provides the context for the file upload. The method logs the start of the file upload process.
   *
   * @param environment the {@link DataFetchingEnvironment} containing the file upload context
   * @return a randomly generated {@link UUID} representing the uploaded file
   */
  public UUID uploadFile(DataFetchingEnvironment environment) {
    log.info("Uploading file");

    DefaultGraphQLServletContext context = environment.getContext();

    /**
     * The {@link DefaultGraphQLServletContext} is retrieved from the {@link DataFetchingEnvironment} to access the uploaded file parts.
     * The method then iterates over each file part, logging the file name and size.
     *
     * <p>Alternative Option: Declare the file parts as a variable to buffer them to memory or
     * stream them somewhere, e.g., {@code List<Part> fileParts = context.getFileParts();}
     */
    context
        .getFileParts()
        .forEach(
            part ->
                log.info("Uploading: {} , size: {}", part.getSubmittedFileName(), part.getSize()));
    return UUID.randomUUID();
  }
}
