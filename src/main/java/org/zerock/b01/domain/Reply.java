package org.zerock.b01.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"rno", "replyText", "replyer"})
//@Table(name = "Reply", indexes = {
//        @Index(name = "idx_reply_board_bno", columnList = "board_bno")
//})
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_bno")
    private Board board;

    private String replyText;

    private String replyer;

    public void changeText(String replyText) {
        this.replyText = replyText;
    }


}
