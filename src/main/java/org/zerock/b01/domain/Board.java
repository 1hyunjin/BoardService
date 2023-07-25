package org.zerock.b01.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"bno", "title", "content","writer"})
public class Board extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false) //column 길이와 null 허용여부
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    @OneToMany(mappedBy = "board")
    private List<Reply> replies = new ArrayList<>();

    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }

    //CascadeType.ALL : 상위 엔티티의 모든 상태 변경이 하위 엔티티에 적용
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)
    private Set<BoardImage> imageSet = new HashSet<>();

    public void addImage(String uuid, String fileName) {
        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(boardImage);
    }

    public void clearImages() {
        //첨부 파일들을 모두 삭제하므로 BoardImage 객체의 Board 참조를 null로 변경
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));
        this.imageSet.clear();

    }


}
