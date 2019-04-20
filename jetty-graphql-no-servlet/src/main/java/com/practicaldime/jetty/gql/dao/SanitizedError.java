package com.practicaldime.jetty.gql.dao;

import graphql.ExceptionWhileDataFetching;
import graphql.execution.ExecutionPath;
import graphql.language.SourceLocation;

public class SanitizedError extends ExceptionWhileDataFetching {

	private static final long serialVersionUID = 1L;

	public SanitizedError(ExecutionPath path, Throwable exception, SourceLocation sourceLocation) {
		super(path, exception, sourceLocation);
		// TODO Auto-generated constructor stub
	}

//	public SanitizedError(ExceptionWhileDataFetching inner) {
//		super(inner.getException());
//	}
//
//	@Override
//	@JsonIgnore
//	public Throwable getException() {
//		return super.getException();
//	}
}
