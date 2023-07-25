package org.zerock.b01.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.BoardImage;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;


@SpringBootTest
@Slf4j
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsert() {
        IntStream.rangeClosed(1,100).forEach(i -> {
            Board board = Board.builder()
                    .title("title..." + i)
                    .content("content..." + i)
                    .writer("user" + (i % 10))
                    .build();

            Board result = boardRepository.save(board);
            log.info("BNO:" + result.getBno());

        });
    }

    @Test
    public void testSelect() {
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        log.info(String.valueOf(board));

    }

    @Test
    public void testUpdate() {
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        board.change("update..title 100", "update content 100");

        boardRepository.save(board);
    }

    @Test
    public void testDelete() {
        Long bno = 100L;
        boardRepository.deleteById(bno);
    }

    @Test
    public void testPaging() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count: " + result.getTotalElements());
        log.info("total page: " + result.getTotalPages());
        log.info("page number: " + result.getNumber());
        log.info("page size: " + result.getSize());

        List<Board> todoList = result.getContent();

        todoList.forEach(board -> log.info(String.valueOf(board)));
    }

    @Test
    public void searchTest() {
        PageRequest pageable = PageRequest.of(1, 10, Sort.by("bno").descending());
//        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll() {
        String[] types = {"c","w", "t"};
        String keyword = "1";
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.search(types, keyword, pageRequest);

        for (Board board : result) {
            System.out.println("board = " + board);
        }

//        PageRequest pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
//        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
//
//        log.info(String.valueOf(result.getTotalPages()));
//        log.info(String.valueOf(result.getSize()));
//        log.info(String.valueOf(result.getNumber()));
//
//        //prev, next
//        log.info(String.valueOf(result.hasPrevious()) + " : " + result.hasNext());
//
//        result.getContent().forEach(board -> log.info(String.valueOf(board)));
    }

    @Test
    public void testSearchReplyCount() {
        String[] types = {"c","w", "t"};
        String keyword = "1";
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageRequest);


        log.info(String.valueOf(result.getTotalPages()));
        log.info(String.valueOf(result.getSize()));
        log.info(String.valueOf(result.getNumber()));
//
        //prev, next
        log.info(String.valueOf(result.hasPrevious()) + " : " + result.hasNext());

        result.getContent().forEach(board -> log.info(String.valueOf(board)));
    }

    @Test
    public void testInsertWithImages() {
        Board board = Board.builder()
                .title("Image Test")
                .content("첨부파일 테스트")
                .writer("tester")
                .build();

        for (int i = 0; i < 3; i++) {
            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
        }
        boardRepository.save(board);
    }

    @Test
    public void testReadWithImage() {
        //반드시 존재하는 bno로 확인
        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        log.info(String.valueOf(board));
        log.info("----------------");
        for (BoardImage boardImage : board.getImageSet()) {
            log.info(String.valueOf(boardImage));
        }
    }

    @Transactional
    @Commit
    @Test
    public void testModifyImages() {
        Optional<Board> result = boardRepository.findByIdWithImages(4L);
        Board board = result.orElseThrow();

        //기존 첨부파일들은 삭제
        board.clearImages();

        //새로운 첨부파일들
        for (int i = 0; i < 2; i++) {
            board.addImage(UUID.randomUUID().toString(), "updatefile" + i + ".jpg");
        }

        boardRepository.save(board);
    }


    @Transactional
    @Test
    @Commit
    public void testRemoveAll() {
        Long bno = 4L;

        replyRepository.deleteByBoard_Bno(bno);

        boardRepository.deleteById(bno);

    }

    @Test
    public void testInsertAll() {

        for (int i = 1; i <= 100; i++) {

            Board board = Board.builder()
                    .title("Title.." + i)
                    .content("Content.." + i)
                    .writer("Writer.." + i)
                    .build();

            for (int j = 0; j < 3; j++) {
                if (i % 5 == 0) {
                    continue;
                }
                board.addImage(UUID.randomUUID().toString(), i + "file" + j + ".jpg");
            }
            boardRepository.save(board);
        }
    }

    @Transactional
    @Test
    public void testSearchImageReplyCount() {


        String[] types = {"c","w", "t"};
        String keyword = null;
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "bno"));

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(types, keyword, pageable);

        log.info("--------------------");
        log.info(String.valueOf(result.getTotalElements()));

        result.getContent().forEach(boardListAllDTO -> log.info(String.valueOf(boardListAllDTO)));
    }







}