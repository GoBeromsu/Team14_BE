package com.kakaotech.team14backend.inner.post.usecase;

import com.kakaotech.team14backend.inner.post.repository.PostLikeCountRepository;
import com.kakaotech.team14backend.inner.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class SchedulePostPopularityUsecase {

  private final PostRepository postRepository;
  private final PostLikeCountRepository postLikeRepository;

  @Scheduled(initialDelayString = "${schedules.initialDelay}",fixedDelayString = "${schedules.fixedDelay}")
  public void execute() {
    postRepository.findAll().stream().forEach(post -> post.updatePopularity(post.getPostLikeCount().getLikeCount(),post.measurePostAge()));
  }

}
