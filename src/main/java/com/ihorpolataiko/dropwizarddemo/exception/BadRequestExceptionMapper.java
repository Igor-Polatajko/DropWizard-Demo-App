package com.ihorpolataiko.dropwizarddemo.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException e) {
        return Response.status(400)
                .entity(ExceptionData.builder()
                        .code(400)
                        .message(e.getMessage())
                        .build())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
