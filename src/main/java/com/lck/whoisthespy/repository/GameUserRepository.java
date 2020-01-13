package com.lck.whoisthespy.repository;

import com.lck.whoisthespy.entity.GameUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameUserRepository extends JpaRepository<GameUser,Integer> {
}
