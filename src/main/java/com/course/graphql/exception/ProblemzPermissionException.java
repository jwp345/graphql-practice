package com.course.graphql.exception;

import com.course.graphql.datasource.problemz.entity.Problemz;

public class ProblemzPermissionException extends RuntimeException{
    public ProblemzPermissionException() {super("You are not allowed to access this operation");}
}
