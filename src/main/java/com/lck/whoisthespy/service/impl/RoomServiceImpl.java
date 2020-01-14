package com.lck.whoisthespy.service.impl;

import com.lck.whoisthespy.entity.Room;
import com.lck.whoisthespy.repository.RoomRepository;
import com.lck.whoisthespy.service.RoomService;
import com.lck.whoisthespy.util.CacheModel;
import com.lck.whoisthespy.util.ControllerUtil;
import com.lck.whoisthespy.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public ResponseVO getAllRoom() {
        Set<Map.Entry<String, Object>> set = CacheModel.entrySet();
        List<Room> roomList=new ArrayList<>();
        for (Map.Entry<String,Object> entry:
             set) {
            if(entry.getKey().contains("room")){
                roomList.add((Room) entry.getValue());
            }
        }
        return ControllerUtil.getDataResult(roomList);
    }
}
