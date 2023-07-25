package org.zerock.b01.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default //기본값 설정을 위한 어노테이션
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String type; //검색 종류: t, c, w, tc, tw, twc

    private String keyword;

    private String link;

    //type 이라는 문자열을 배열로 반환하는 기능
    public String[] getTypes() {
        if (type == null || type.isEmpty()) {
            return null;
        }
        return type.split("");
    }

    //paging 처리 위한 기능 - Pageable 타입을 반환하는 기능
    public Pageable getPageable(String... props) {
        return PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());
    }

    //검색 조건과 페이징 조건 등을 문자열로 구성하는 기능
    public String getLink() {
        if (link == null) {
            StringBuilder builder = new StringBuilder(); //문자 더하기
            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);

            if (type != null && type.length() > 0) {
                builder.append("&type=" + type);
            }

            if (keyword != null) {
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            link = builder.toString();
        }
        return link;
    }
}
