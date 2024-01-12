package dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import dto.ChatMsgDTO;

@Repository("livechatdao")
@Mapper
public interface LiveChatDAO {

	/**
	 * 과거 채팅방 메시지 조회
	 * @param chatIdx
	 * @return 과거 채팅방 메시지 리스트
	 */
	List<ChatMsgDTO> selectAllChatMsg(int chatIdx);

}
