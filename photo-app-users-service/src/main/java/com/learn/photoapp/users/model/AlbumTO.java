package com.learn.photoapp.users.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumTO {
  private String albumId;
  private String userId;
  private String name;
  private String description;
}
