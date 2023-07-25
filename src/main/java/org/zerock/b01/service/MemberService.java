package org.zerock.b01.service;

import org.springframework.validation.Errors;
import org.zerock.b01.dto.MemberJoinDTO;

import java.util.HashMap;
import java.util.Map;

public interface MemberService {

    /**
     * 해당 아이디가 존재하는 경우 MemberRepository의 save()는 insert가 아닌 update로 실행
     * 같은 아이디가 존재하는 경우 예외를 발생
     */
    static class MidExistException extends Exception {
    }

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;


    String emailCheck(String memberEmail);

    String idCheck(String mid);

}
