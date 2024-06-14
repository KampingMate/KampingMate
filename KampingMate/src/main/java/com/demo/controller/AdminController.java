package com.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.domain.AdminQnaBoard;
import com.demo.domain.Book;
import com.demo.domain.MemberData;
import com.demo.domain.Notice;
import com.demo.domain.askBoard;
import com.demo.service.AdminService;
import com.demo.service.MemberService;
import com.demo.service.NoticeService;

import jakarta.persistence.EntityManager;


@Controller
@SessionAttributes("loginUser")
public class AdminController {

    @Autowired
    AdminService adminService;
    @Autowired
    NoticeService noticeService;
    @Autowired
    MemberService memberService;
    
    @Autowired
    private EntityManager entityManager;
    

    @GetMapping("/admin")
    public String mainView() {
        return "admin/admin_login";
    }

    @PostMapping("/admin/login")
    public String login(@RequestParam("id") String id,
                        @RequestParam("password") String password,
                        Model model) {
        // 실제 로그인 검증 로직을 사용하여 데이터베이스에서 사용자 정보를 확인합니다.
        MemberData loginUser = adminService.validateLogin(id, password);
        if (loginUser != null && loginUser.getUsercode() == 0) {
            model.addAttribute("error", "You are not authorized to login.");
            return "admin/admin_login";
        } else if (loginUser != null && loginUser.getUsercode() == 1) {
            model.addAttribute("loginUser", loginUser);
            return "redirect:/admin_main";
        } else {
            model.addAttribute("error", "Invalid id or password");
            return "admin/admin_login";
        }
    }

 // 로그아웃 처리
    @GetMapping("/admin_logout")
    public String logout(SessionStatus status) {
        status.setComplete(); // 세션 완료 상태로 설정
        return "admin/admin_login"; // 로그아웃 후 로그인 화면으로 리다이렉트
    }

    @GetMapping("/admin_main")
    public String mainView2(@ModelAttribute("loginUser") MemberData loginUser, Model model) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);

            List<AdminQnaBoard> qnaBoardList = adminService.getAllQnaBoardListMain();
            List<askBoard> askBoardList = adminService.getAllAskBoardListMain();

            model.addAttribute("askBoardList", askBoardList);
            model.addAttribute("qnaBoardList", qnaBoardList);

            return "admin/admin_main";
        } else {
            return "redirect:/admin";
        }
    }

    @GetMapping("/memberList.do")
    public String memberInfo(@ModelAttribute("loginUser") MemberData loginUser, Model model) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);

            List<MemberData> memberList = adminService.getAllMemberList();
            model.addAttribute("memberList", memberList);

            return "admin/boardPage/admin_userlist";
        } else {
            return "redirect:/admin";
        }
    }

    @GetMapping("/admin_QnA.do")
    public String QnABoard(@ModelAttribute("loginUser") MemberData loginUser, Model model, Pageable pageable) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);

            Page<AdminQnaBoard> qnaBoardPage = adminService.getAllQnaBoardList(PageRequest.of(pageable.getPageNumber(), 10));
            model.addAttribute("qnaBoardPage", qnaBoardPage);

            return "admin/boardPage/admin_qnaboard";
        } else {
            return "redirect:/admin";
        }
    }

    @GetMapping("/qnaRegister.do")
    public String qnaRegiView(@ModelAttribute("loginUser") MemberData loginUser) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);
            return "admin/boardForm/qnaForm";
        } else {
            return "redirect:/admin";
        }
    }

    @PostMapping("/qnaReg.do")
    public String qnaRegister(@ModelAttribute("loginUser") MemberData loginUser, AdminQnaBoard vo) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);
            adminService.insertAdminQnaBoard(vo);
            return "redirect:/admin_QnA.do";
        } else {
            return "redirect:/admin";
        }
    }

    @PostMapping("/qnaEdit.do")
    public String editQna(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("qnaSelect") int boardnum, RedirectAttributes redirectAttributes) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);
            AdminQnaBoard qna = adminService.getByQnaBoardnum(boardnum);
            redirectAttributes.addFlashAttribute("qna", qna);
            return "redirect:/qnaEditForm.do?boardnum=" + boardnum;
        } else {
            return "redirect:/admin";
        }
    }

    @GetMapping("/qnaEditForm.do")
    public String showEditQnaForm(@RequestParam("boardnum") int boardnum, Model model) {
        if (!model.containsAttribute("qna")) {
            AdminQnaBoard qna = adminService.getByQnaBoardnum(boardnum);
            model.addAttribute("qna", qna);
        }
        return "admin/boardForm/qnaEditForm";
    }

    @PostMapping("/qnaUpdate.do")
    public String qnaUpdate(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("qnaNum") int boardnum, AdminQnaBoard vo) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);
            vo.setQna_boardnum(boardnum);
            adminService.updateAdminQnaBoard(vo);
            return "redirect:/admin_QnA.do";
        } else {
            return "redirect:/admin";
        }
    }

    @PostMapping("/qnaDelete.do")
    public String deleteQna(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("qnaSelect") int boardnum) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);
            adminService.deleteAdminQnaBoard(boardnum);
            return "redirect:/admin_QnA.do";
        } else {
            return "redirect:/admin";
        }
    }

    @GetMapping("/admin_ask.do")
    public String AskBoard(@ModelAttribute("loginUser") MemberData loginUser, Pageable pageable, Model model) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);
            Page<askBoard> askBoardPage = adminService.getAllAskBoardListWait(PageRequest.of(pageable.getPageNumber(), 10));
            model.addAttribute("askBoardPage", askBoardPage);
            return "admin/boardPage/admin_askboard";
        } else {
            return "redirect:/admin";
        }
    }

    @PostMapping("/admin_ask.do")
    public String AskBoardFinish(@ModelAttribute("loginUser") MemberData loginUser, RedirectAttributes redirectAttributes, Pageable pageable, Model model) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);
            Page<askBoard> askBoardPage = adminService.getAllAskBoardListFinish(PageRequest.of(pageable.getPageNumber(), 10));
            model.addAttribute("askBoardPage", askBoardPage);
            redirectAttributes.addFlashAttribute("askBoardPage", askBoardPage);
            return "redirect:/admin_ask_board.do";
        } else {
            return "redirect:/admin";
        }
    }

    @GetMapping("/admin_ask_board.do")
    public String showFinishAsk(@ModelAttribute("loginUser") MemberData loginUser) {
        if (loginUser != null && loginUser.getId() != null) {
            return "admin/boardPage/admin_askboardEnd";
        } else {
            return "redirect:/admin";
        }
    }

    @GetMapping("/askDetail.do")
    public String askDetail(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("askNum") int askNum, Model model) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);
            askBoard ask = adminService.getByAskBoardnum(askNum);
            model.addAttribute("ask", ask);
            model.addAttribute("end_view", "view");
            return "admin/boardForm/askEditForm";
        } else {
            return "redirect:/admin";
        }
    }

    @GetMapping("/askEdit.do")
    public String askEdit(@ModelAttribute("loginUser") MemberData loginUser, @RequestParam("askNum") int askNum, Model model) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);
            askBoard ask = adminService.getByAskBoardnum(askNum);
            
            model.addAttribute("ask", ask);
            return "admin/boardForm/askEditForm";
        } else {
            return "redirect:/admin";
        }
    }

    @PostMapping("/askUpdate.do")
    public String askUpdate(@ModelAttribute("loginUser") MemberData loginUser, 
                            @ModelAttribute askBoard vo, 
                            @RequestParam("askNum") Long boardnum) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);

            askBoard ask = adminService.getByAskBoardnum(boardnum);
            if (ask != null) {
                vo.setRegdate(ask.getRegdate());
                vo.setInquiry_id(boardnum);
                vo.setEmail(ask.getEmail());
                vo.setName(ask.getName());
                vo.setStatus("답변 완료");
                vo.setMember_data(loginUser);

                adminService.updateAdminInquiry(vo);
                return "redirect:/admin_ask.do";
            }
        }
        return "redirect:/admin";
    }

    
    @GetMapping("/admin_notice.do")
    public String noticeForm(@ModelAttribute("loginUser") MemberData loginUser, Model model) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);
            return "admin/boardForm/noticeForm"; // 공지사항 작성 폼 페이지
        } else {
            return "redirect:/admin";
        }
    }

    @PostMapping("/noticeReg.do")
    public String noticeRegister(@ModelAttribute("loginUser") MemberData loginUser,
                                 Notice notice,
                                 RedirectAttributes redirectAttributes) throws IOException {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);

            // 사용자 ID를 사용하여 MemberData 객체를 데이터베이스에서 조회
            MemberData memberData = memberService.findById(loginUser.getId());
            if (memberData != null) {
                // Notice 엔티티에 조회된 MemberData 객체를 설정
                notice.setMember_data(memberData);

                // 파일 이름 리스트를 생성하여 Notice 객체에 추가
               

                // 공지 종류가 이벤트인 경우
                if ("event".equals(notice.getNotice_cate())) {
                    // 이벤트로 처리
                    adminService.insertNotice(notice);
                    return "redirect:/admin_eventlist.do"; // 이벤트 목록 페이지로 리디렉션
                } else {
                    // 공지로 처리
                    adminService.insertNotice(notice);
                    return "redirect:/admin_noticelist.do"; // 공지사항 목록 페이지로 리디렉션
                }
            } else {
                // MemberData가 조회되지 않은 경우 처리
                return "redirect:/admin";
            }
        } else {
            return "redirect:/admin";
        }
    }

    
    @GetMapping("/admin_noticelist.do")
    public String noticeList(@ModelAttribute("loginUser") MemberData loginUser, Model model) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);

            // 공지사항 목록을 가져오기
            List<Notice> noticeList = adminService.getAllNotices();

            model.addAttribute("notices", noticeList);
            return "admin/boardPage/admin_noticelist"; // 공지사항 목록 페이지
        } else {
            return "redirect:/admin";
        }
    }
    
    @GetMapping("/admin_noticeDetail")
    public String adminNoticeDetail(@RequestParam("notice_seq") int noticeSeq, Model model) {
        Notice notice = adminService.getBySeq(noticeSeq);
        model.addAttribute("notice", notice);
        return "admin/boardPage/admin_noticeDetail";
    }
    
    @PostMapping("/admin_updateNotice")
    public String adminUpdateNotice(@ModelAttribute("notice") Notice notice,
                                    RedirectAttributes redirectAttributes) {
        adminService.updateAdminNotice(notice);

        // Determine redirect path based on notice_cate
        if ("event".equals(notice.getNotice_cate())) {
            return "redirect:/admin_eventlist.do";
        } else if ("notice".equals(notice.getNotice_cate())) {
            return "redirect:/admin_noticelist.do";
        } else {
            // Handle other cases or defaults
            return "redirect:/admin_dashboard.do";
        }
    }



    @GetMapping("/admin_eventlist.do")
    public String eventList(@ModelAttribute("loginUser") MemberData loginUser, Model model) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);

            // 이벤트 목록을 가져오기
            List<Notice> eventList = adminService.getAllEvents();

            model.addAttribute("events", eventList);
            return "admin/boardPage/admin_eventlist"; // 이벤트 목록 페이지
        } else {
            return "redirect:/admin";
        }
    }

    @GetMapping("/admin_bookinglist.do")
    public String bookingList(@ModelAttribute("loginUser") MemberData loginUser, Model model) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);

            List<Book> bookingList = adminService.getAllBookings();
            model.addAttribute("bookings", bookingList);
            return "admin/boardPage/admin_bookinglist"; // 예약 목록 페이지
        } else {
            return "redirect:/admin";
        }
    }

    @PostMapping("/updateBookingCondition.do")
    public String updateBookingCondition(@ModelAttribute("loginUser") MemberData loginUser,
                                         @RequestParam("bookseq") int bookseq,
                                         @RequestParam("condition") int condition) {
        if (loginUser != null && loginUser.getId() != null) {
            adminService.adminCheck(loginUser);

            adminService.updateBookingCondition(bookseq, condition);
            return "redirect:/admin_bookinglist.do"; // 예약 목록 페이지로 리디렉션
        } else {
            return "redirect:/admin";
        }
    }









}
