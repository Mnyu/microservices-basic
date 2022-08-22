package com.learn.photoapp.users.feign;

import com.learn.photoapp.users.model.AlbumTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

// This interface AlbumsServiceClient will be implemented by the framework, but we need to
// annotate it properly to specify the details of the request and the response.

@FeignClient(name = "albums-ws")
public interface AlbumsServiceClient {

  @GetMapping("/users/{userId}/albums")
  @CircuitBreaker(name = "albums-ws-cb", fallbackMethod = "getAlbumsFallback")
  List<AlbumTO> getAlbums(@PathVariable("userId") String userId);

  default List<AlbumTO> getAlbumsFallback(String userId, Throwable exception) {
    System.out.println("Param : " + userId);
    System.out.println("Exception took place : " + exception.getMessage());
    return new ArrayList<>();
  }

}
