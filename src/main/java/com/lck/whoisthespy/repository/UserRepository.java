package com.lck.whoisthespy.repository;

import com.lck.whoisthespy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
