package org.zerock.b01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.MemberJoinDTO;
import org.zerock.b01.service.MemberService;
import org.springframework.validation.Errors;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginGET(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "exception", required = false) String exception,
                           Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "/member/login";
    }
//    public void loginGET(String error, String logout) { //String error, String logout
////    public String loginGET(HttpServletRequest request, Model model){
//        log.info("login get...........");
//
//        if (logout != null) {
//            log.info("user logout....");
//        }
//    }

//    @PostMapping("/login")
//    public String  loginPOST() {
//        return
//        "redirect:/board/list";
//    }

    //회원 가입
    @GetMapping("/join")
    public void joinGET() {

        log.info("join get...");

    }

    @PostMapping("/join")
    public String joinPOST(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes) {
        log.info("join post...");
        log.info(memberJoinDTO);

        try {
            memberService.join(memberJoinDTO);
        } catch (MemberService.MidExistException e) {
            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/member/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/member/login"; //회원 가입 후 로그인
    }

    @PostMapping(value = "/email-check" ,produces = "application/text; charset=utf8")
    @ResponseBody
    public String emailCheck(@RequestParam("memberEmail") String memberEmail) {
        System.out.println("memberEmail = " + memberEmail);
        String checkResult = memberService.emailCheck(memberEmail);
        return checkResult;
    }

    @PostMapping(value = "/mid-check" ,produces = "application/text; charset=utf8")
    @ResponseBody
    public String midCheck(@RequestParam("mid") String mid) {
        System.out.println("mid = " + mid);
        String checkResult = memberService.idCheck(mid);
        return checkResult;
    }
}
