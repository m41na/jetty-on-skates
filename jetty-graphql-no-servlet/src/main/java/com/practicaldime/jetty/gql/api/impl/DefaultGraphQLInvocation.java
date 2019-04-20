package com.practicaldime.jetty.gql.api.impl;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.practicaldime.jetty.gql.api.GraphQLInvocation;
import com.practicaldime.jetty.gql.api.GraphQLInvocationData;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;

@Component
public class DefaultGraphQLInvocation implements GraphQLInvocation {

    @Autowired
    private GraphQL graphQL;

    @Override
    public CompletableFuture<ExecutionResult> invoke(GraphQLInvocationData invocationData ) {
        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                .query(invocationData.getQuery())
                .operationName(invocationData.getOperationName())
                .variables(invocationData.getVariables())
                .build();
        return graphQL.executeAsync(executionInput);
    }
}
