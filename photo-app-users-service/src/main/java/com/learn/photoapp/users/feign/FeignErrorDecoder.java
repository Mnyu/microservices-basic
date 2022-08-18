package com.learn.photoapp.users.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// Handle Response Errors with Feign Error Decoder

public class FeignErrorDecoder implements ErrorDecoder {

  // This method is call for all errors that FeignClient receives.
  // Hence, business logic to handler errors is centralized here.
  @Override
  public Exception decode(String methodKey, Response response) {
    switch (response.status()) {
    case 400:
      // do something
      break;
    case 404: {
      if (methodKey.contains("getAlbums")) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Users albums are not found");
      }
      break;
    }
    default:
      return new Exception(response.reason());
    }
    return null;
  }
}
