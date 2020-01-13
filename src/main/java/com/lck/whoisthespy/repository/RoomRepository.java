package com.lck.whoisthespy.repository;

import com.lck.whoisthespy.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Integer> {
}
