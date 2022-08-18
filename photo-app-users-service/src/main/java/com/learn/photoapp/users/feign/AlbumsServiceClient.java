package com.learn.photoapp.users.feign;

import com.learn.photoapp.users.model.AlbumTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// This interface AlbumsServiceClient will be implemented by the framework, but we need to
// annotate it properly to specify the details of the request and the response.

@FeignClient(name = "albums-ws")
public interface AlbumsServiceClient {

  @GetMapping("/users/{userId}/albums")
  List<AlbumTO> getAlbums(@PathVariable("userId") String userId);

}
