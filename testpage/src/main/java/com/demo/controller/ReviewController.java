package com.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.demo.domain.MemberData;
import com.demo.domain.Review;
import com.demo.service.ReviewService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
public class ReviewController {

    @Autowired
    ReviewService reviewsv;
    
    private final String uploads = Paths.get("E:", "tui-editor", "upload").toString();
    
    //큰메뉴 캠핑장검색페이지로
    @GetMapping("/map")
    public String go_mapsearch() {
        return "Mapsearch/regionMapApi";
    }
    
    // 리뷰작성시 캠핑장찾기
    @GetMapping("/kakao-map")
    public String kakaoMap() {
        return "Community/search_kakaomap";
    }
    
    //리뷰 리스트
    @GetMapping(value = {"/review", "/review_list"})
    public String getReviewList(@RequestParam(value = "review_seq", defaultValue = "1") int review_seq,
                                @RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "6") int size, Model model) {
        Page<Review> pageList = reviewsv.getAllReview(review_seq, page, size);
        List<Review> reviewList = pageList.getContent();

        model.addAttribute("reviewList", reviewList);
        model.addAttribute("totalPages", pageList.getTotalPages());
        model.addAttribute("pageNumber", page);

        return "Community/ReviewList";
    }

    //리뷰쓰기
    @GetMapping("/reviewwrite")
    public String getReviewWriteView(HttpSession session) {
        return "Community/ReviewWrite";
    }

    
    //리뷰 글 전송
    @Transactional
    @PostMapping("/reviewwrite/submit")
    public String insertCom_Board(HttpSession session, Model model, @RequestBody Review review) {
        MemberData loginUser = (MemberData) session.getAttribute("loginUser");
        
        review.setMember_data(loginUser);
        reviewsv.insertReview(review);

        model.addAttribute("totalPages", session.getAttribute("totalPages"));
        model.addAttribute("pageNumber", session.getAttribute("pageNumber"));

        return "redirect:/review";
    }

    //이미지업로드
    @PostMapping("/tui-editor/image-upload")
    public String uploadEditorImage(@RequestParam final MultipartFile image) {
        if (image.isEmpty()) {
            return "";
        }

        String orgFilename = image.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);
        String saveFilename = uuid + "." + extension;
        String fileFullPath = Paths.get(uploads, saveFilename).toString();

        File dir = new File(uploads);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            File uploadFile = new File(fileFullPath);
            image.transferTo(uploadFile);
            return saveFilename;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    //이미지 출력
    @GetMapping(value = "/tui-editor/image-print", produces = {MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] printEditorImage(@RequestParam final String filename) {
        String fileFullPath = Paths.get(uploads, filename).toString();
        File uploadedFile = new File(fileFullPath);
        if (!uploadedFile.exists()) {
            throw new RuntimeException();
        }

        try {
            return Files.readAllBytes(uploadedFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    
}

