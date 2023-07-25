package org.zerock.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.BoardImageDTO;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.zerock.b01.domain.QBoard.board;
import static org.zerock.b01.domain.QReply.reply;

// 인터페이스 구현 클래스
public class BoardSearchImpl implements BoardSearch {

    private final JPAQueryFactory queryFactory;

    public BoardSearchImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {
        QueryResults<Board> results = queryFactory.select(board)
                .from(board)
                .where(board.title.contains("1"))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        List<Board> content = results.getResults();
        long count = results.getTotal();

        return new PageImpl<>(content, pageable, count);
    }

    /**
     * 제목, 내용, 작성자에 키워드가 존재하고 bno가 0보다 큰 데이터를 조회
     */
    @Override
    public Page<Board> search(String[] types, String keyword, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        if ((types != null && types.length > 0) && keyword != null) {

            for (String type : types) {
                switch (type) {
                    case "t":
                        builder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        builder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        builder.or(board.writer.contains(keyword));
                        break;
                }
            }
        }
        List<Board> content = queryFactory.selectFrom(board)
                .where(
                        builder,
                        board.bno.gt(0L))
                .orderBy(board.bno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory
                .select(Wildcard.count) // select count(*)
                .from(board)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }

    /**
     * 게시글 목록 화면에 댓글의 숫자도 같이 출력
     */
    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if ((types != null && types.length > 0) && keyword != null) {

            for (String type : types) {
                switch (type) {
                    case "t":
                        builder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        builder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        builder.or(board.writer.contains(keyword));
                        break;
                }
            }
        }
        List<BoardListReplyCountDTO> content = queryFactory
                .select(Projections.bean(BoardListReplyCountDTO.class,
                        board.bno,
                        board.title,
                        board.writer,
                        board.regDate,
                        reply.count().as("replyCount")
                ))
                .from(board)
                .where(
                        builder,
                        board.bno.gt(0L))
                .leftJoin(reply).on(reply.board.eq(board))
                .groupBy(board)
                .orderBy(board.bno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory
                .select(board.countDistinct())
                .from(board)
                .leftJoin(reply).on(reply.board.eq(board))
                .where(builder,
                        board.bno.gt(0L))
                .fetchOne();

        return new PageImpl<>(content,pageable, totalCount);
    }

    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if ((types != null && types.length > 0) && keyword != null) {

            for (String type : types) {
                switch (type) {
                    case "t":
                        builder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        builder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        builder.or(board.writer.contains(keyword));
                        break;
                }
            }
        }
        List<Tuple> tupleList = queryFactory.select(board, reply.countDistinct())
                .from(board)
                .leftJoin(reply).on(reply.board.eq(board))
                .where(builder)
                .groupBy(board)
                .orderBy(board.bno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
            Board board1 = (Board) tuple.get(board);
            Long replyCount = tuple.get(1, Long.class);

            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .replyCount(replyCount)
                    .build();

            //BoardImage를 BoardImageDTO 처리할 부분
            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
                    .map(boardImage -> BoardImageDTO.builder()
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .ord(boardImage.getOrd())
                            .build()).collect(Collectors.toList());

            dto.setBoardImages(imageDTOS);  //처리된 BoardImageDTO들을 추가

            return dto;
        }).collect(Collectors.toList());

        Long totalCount = queryFactory
                .select(board.countDistinct())
                .from(board)
                .leftJoin(reply).on(reply.board.eq(board))
                .where(builder,
                        board.bno.gt(0L))
                .fetchOne();

        return new PageImpl<>(dtoList, pageable, totalCount);
    }

}
