package org.zerock.b01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample Title..")
                .content("Sample Content")
                .writer("user00")
                .build();

        Long bno = boardService.register(boardDTO);

        log.info("bno : " + bno);

    }

    @Test
    public void testModify() {
        //변경에 필요한 데이터만 -> @builder
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("Updated..101")
                .content("Updated content 101")
                .build();

        boardService.modify(boardDTO);
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        log.info(responseDTO);
    }

    //게시글 등록 (첨부파일도 같이 등록)
    @Test
    public void testRegisterWithImages() {
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("File..Sample Title...")
                .content("Sample Content....")
                .writer("user00")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID() + "_aaa.jpg",
                        UUID.randomUUID() + "_bbb.jpg",
                        UUID.randomUUID() + "_bbb.jpg"

                )
        );

        Long bno = boardService.register(boardDTO);

        log.info("bno: " + bno);
    }

    //게시글 조회 (첨부파일도 같이 조회)
    @Test
    public void testReadAll() {
        Long bno = 102L;
        BoardDTO boardDTO = boardService.readOne(bno);
        log.info(boardDTO);

        for (String fileName : boardDTO.getFileNames()) {
            log.info(fileName);
        }
    }

    //게시물 수정 시 - 첨부파일 수정?
    @Test
    public void testModify1() {
        //변경에 필요한 데이터만 -> @builder
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(102L)
                .title("Updated..102")
                .content("Updated content 102")
                .build();

        //첨부파일 추가
        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID() + "_zzz.jpg"));
        boardService.modify(boardDTO);
    }

    //게시물 삭제 시 첨부파일도 같이 삭제됨 cascase -> ALL 이므로
    @Test
    public void testRemoveAll() {
        Long bno = 1L;
        boardService.remove(bno);
    }

    @Test
    public void testListAll() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);
        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();
        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno() + ":" + boardListAllDTO.getTitle());
            if (boardListAllDTO.getBoardImages() != null) {
                for (BoardImageDTO boardImage : boardListAllDTO.getBoardImages()) {
                    log.info(boardImage);
                }
            }
            log.info("-----------------------");
        });
    }

}