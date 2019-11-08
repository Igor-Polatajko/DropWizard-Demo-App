package com.ihorpolataiko.dropwizarddemo.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServerInternalErrorExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        return Response.status(500)
                .entity(ExceptionData.builder()
                        .code(500)
                        .message(e.getMessage())
                        .build())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
