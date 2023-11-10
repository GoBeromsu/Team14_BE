package com.kakaotech.team14backend.post.application;

import com.kakaotech.team14backend.post.application.FindPostUsecase;
import com.kakaotech.team14backend.outer.post.dto.GetPostDTO;
import com.kakaotech.team14backend.outer.post.dto.GetPostResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetHomePostUsecase {

  private final FindPostUsecase findPostUsecase;

  public GetPostResponseDTO excute(GetPostDTO getPostDTO) {
    return findPostUsecase.execute(getPostDTO);
  }
}
