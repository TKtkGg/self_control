package com.tktkgg.selfcontrol.repository;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.tktkgg.selfcontrol.entity.Like;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    Optional<Like> findByUserIdAndTargetUserId(@Param("userId") UUID userId, @Param("targetUserId") UUID targetUserId);

    List<Like> findByUserId(@Param("userId") UUID userId);

    List<Like> findByTargetUserId(@Param("targetUserId") UUID targetUserId);
}
