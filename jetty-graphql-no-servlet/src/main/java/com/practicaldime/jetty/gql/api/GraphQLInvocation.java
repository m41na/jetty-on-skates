package com.practicaldime.jetty.gql.api;

import java.util.concurrent.CompletableFuture;

import graphql.ExecutionResult;

public interface GraphQLInvocation {

	CompletableFuture<ExecutionResult> invoke(GraphQLInvocationData invocationData);
}
