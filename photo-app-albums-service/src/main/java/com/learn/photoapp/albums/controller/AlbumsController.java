package com.learn.photoapp.albums.controller;

import com.learn.photoapp.albums.model.AlbumTO;
import com.learn.photoapp.albums.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users/{id}/albums")
public class AlbumsController {

  private AlbumService albumService;

  @Autowired
  public AlbumsController(AlbumService albumService) {
    this.albumService = albumService;
  }

  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public List<AlbumTO> getAlbums(@PathVariable String id) {
    return albumService.getAlbums(id);
  }

}
