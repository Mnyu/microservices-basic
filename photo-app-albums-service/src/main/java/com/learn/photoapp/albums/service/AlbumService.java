package com.learn.photoapp.albums.service;

import com.learn.photoapp.albums.model.AlbumTO;

import java.util.List;

public interface AlbumService {
  List<AlbumTO> getAlbums(String id);
}
