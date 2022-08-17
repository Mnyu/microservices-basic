package com.learn.photoapp.albums.service;

import com.learn.photoapp.albums.model.AlbumTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumServiceImpl implements AlbumService {

  @Override
  public List<AlbumTO> getAlbums(String id) {
    List<AlbumTO> albumTOs = new ArrayList<>();
    albumTOs.add(AlbumTO.builder()
        .albumId("albumId1")
        .userId(id)
        .name("Album 1")
        .description("Album Description 1")
        .build());
    albumTOs.add(AlbumTO.builder()
        .albumId("albumId1")
        .userId(id)
        .name("Album 2")
        .description("Album Description 2")
        .build());
    return albumTOs;
  }

}
