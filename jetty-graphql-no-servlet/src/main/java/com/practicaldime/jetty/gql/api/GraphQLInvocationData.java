package com.practicaldime.jetty.gql.api;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import graphql.Assert;

public class GraphQLInvocationData {

	private final String query;
    private final String operationName;
    private final Map<String, Object> variables;
    private final Function<String, String> headers;

    public GraphQLInvocationData(String query, String operationName, Map<String, Object> variables, Function<String, String> headers) {
        this.query = Assert.assertNotNull(query, "query must be provided");
        this.operationName = operationName;
        this.variables = variables != null ? variables : Collections.emptyMap();
        this.headers = headers;
    }

    public String getQuery() {
        return query;
    }

    public String getOperationName() {
        return operationName;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

	public String getHeader(String header) {
		return headers.apply(header);
	}
}
