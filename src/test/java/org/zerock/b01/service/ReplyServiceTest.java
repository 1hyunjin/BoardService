package org.zerock.b01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Reply;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.ReplyDTO;
import org.zerock.b01.repository.ReplyRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ReplyServiceTest {

    @Autowired
    private ReplyService replyService;

    @Test
    public void testRegister(){

        ReplyDTO replyDTO = ReplyDTO.builder()
                .bno(99L)
                .replyText("ReplyDTO Text")
                .replyer("replyer")
                .build();

        log.info(replyService.register(replyDTO));
    }
}