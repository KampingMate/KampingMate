package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.service.GoCampingAPI;
import com.demo.service.GoCampingAPI.CampingApiResponse;

@Controller
public class GoCampingController {

    private static final int PAGE_SIZE = 10; // 한 페이지에 보여줄 항목 수
    private static final int PAGE_BLOCK = 5; // 페이징 블록 크기

    @GetMapping("/campingSites")
    public String getCampingSites(@RequestParam(defaultValue = "1") int page, Model model) {
        try {
            CampingApiResponse response = GoCampingAPI.getCampingSites(page, PAGE_SIZE);
            model.addAttribute("totalCount", response.getTotalCount());
            model.addAttribute("items", response.getItems());
            model.addAttribute("currentPage", page);
            int totalPages = (int) Math.ceil((double) response.getTotalCount() / PAGE_SIZE);
            model.addAttribute("totalPages", totalPages);

            // 시작 페이지와 끝 페이지 계산
            int startPage = ((page - 1) / PAGE_BLOCK) * PAGE_BLOCK + 1;
            int endPage = Math.min(startPage + PAGE_BLOCK - 1, totalPages);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("hasPrev", startPage > 1);
            model.addAttribute("hasNext", endPage < totalPages);

        } catch (Exception e) {
            model.addAttribute("error", "Failed to load camping sites");
            e.printStackTrace();
        }
        return "fragments/campingSites";
    }
}
