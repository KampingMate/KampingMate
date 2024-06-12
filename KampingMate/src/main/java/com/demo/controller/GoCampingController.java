package com.demo.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.dto.CampingItem;
import com.demo.dto.GoCampingSearchList;
import com.demo.persistence.GoCampingRepository;
import com.demo.service.GoCampingAPI;
import com.demo.service.RegionMapping;
import com.demo.service.SigunguService;

@Controller
public class GoCampingController {

    @Autowired
    private GoCampingAPI goCampingAPI;
    
    @Autowired
    private SigunguService sigunguService;
    
    @Autowired
    private GoCampingRepository gocampingRepo;

    @GetMapping("/campingSites")
    public String getCampingSites(@RequestParam(defaultValue = "1") int page, Model model) {
        try {
            GoCampingAPI.CampingApiResponse response = goCampingAPI.getCampingSites(page, 10);
            List<CampingItem> campingItems = response.getItems();
            int totalCount = response.getTotalCount();

            int itemsPerPage = 10;
            int startIndex = (page - 1) * itemsPerPage;
            int endIndex = Math.min(startIndex + itemsPerPage, totalCount);

            model.addAttribute("totalCount", totalCount);
            model.addAttribute("items", campingItems);
            model.addAttribute("currentPage", page);
            model.addAttribute("hasPrev", page > 1);
            model.addAttribute("hasNext", endIndex < totalCount);
            model.addAttribute("startPage", Math.max(1, page - 5));
            model.addAttribute("endPage", Math.min((totalCount + itemsPerPage - 1) / itemsPerPage, page + 5));

            return "campingSites";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to load camping sites.");
            return "error";
        }
    }
    
    @GetMapping("/go_detail")
    public String goDetailPopupView(Model model) {
        List<String> do_list = gocampingRepo.findAllDoNmList();
        List<String> faclt_div_list = gocampingRepo.findAllFacltDivList();
        List<String> lct_cl_list = gocampingRepo.findAllLctClList();
        List<String> induty_list = gocampingRepo.findAllIndutyList();
        List<String> sbrs_cl_list = gocampingRepo.findAllSbrsClList();
        
        // 중복 제거 및 null 값 제거
        Set<String> uniqueDoList = new TreeSet<>(do_list.stream()
                                          .filter(item -> item != null)
                                          .map(RegionMapping::mapDoName)
                                          .collect(Collectors.toSet()));
        Set<String> uniqueFacltDivList = faclt_div_list.stream()
                                                       .filter(item -> item != null)
                                                       .collect(Collectors.toSet());
        Set<String> uniqueLctClList = lct_cl_list.stream()
                                                 .filter(item -> item != null)
                                                 .flatMap((String item) -> Arrays.stream(item.split(",")))
                                                 .map(String::trim)
                                                 .collect(Collectors.toSet());
        Set<String> uniqueIndutyList = induty_list.stream()
                                                  .filter(item -> item != null)
                                                  .flatMap((String item) -> Arrays.stream(item.split(",")))
                                                  .map(String::trim)
                                                  .collect(Collectors.toSet());

        // 주요 시설 중복 제거 및 null 값 제거
        Set<String> uniqueSbrsClList = sbrs_cl_list.stream()
                                                   .filter(item -> item != null)
                                                   .flatMap((String item) -> Arrays.stream(item.split(",")))
                                                   .map(String::trim)
                                                   .collect(Collectors.toSet());
        
        List<String> uniqueBottomClList = Arrays.asList("잔디", "파쇄석", "데크", "자갈", "맨흙");
        
        model.addAttribute("do_list", uniqueDoList);
        model.addAttribute("faclt_div_list", uniqueFacltDivList);
        model.addAttribute("lct_cl_list", uniqueLctClList);
        model.addAttribute("induty_list", uniqueIndutyList);
        model.addAttribute("site_bottom_list", uniqueBottomClList);
        model.addAttribute("sbrs_cl_list", uniqueSbrsClList);
        
        return "detail_select";
    }
    
    @GetMapping("/getSearchList")
    public String getNormalSearchList(@RequestParam String doNm,
                                      @RequestParam String gungu,
                                      @RequestParam String faclt,
                                      @RequestParam String lct,
                                      @RequestParam String induty,
                                      @RequestParam String bottom,
                                      @RequestParam String sbrs,
                                      @RequestParam(defaultValue = "1") int page,
                                      Model model) {
        try {
            doNm = doNm != null ? URLDecoder.decode(doNm, StandardCharsets.UTF_8.toString()) : "";
            gungu = gungu != null ? URLDecoder.decode(gungu, StandardCharsets.UTF_8.toString()) : "";
            lct = lct != null ? URLDecoder.decode(lct, StandardCharsets.UTF_8.toString()) : "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 페이지 인덱스를 1보다 작지 않도록 보정
        page = Math.max(page, 1);
        Pageable pageable = PageRequest.of(page - 1, 10);

        System.out.println("doNm: " + doNm + ", gungu: " + gungu + ", faclt: " + faclt + ", lct: " + lct);

        Page<GoCampingSearchList> goCampingPage = gocampingRepo.getSearchList(doNm, gungu, lct, pageable);

        List<GoCampingSearchList> goCampingList = goCampingPage.getContent();
        int totalPages = goCampingPage.getTotalPages();
        long totalElements = goCampingPage.getTotalElements();

        model.addAttribute("goCampingList", goCampingList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);

        return "searchView";
    }
    
    @GetMapping("/get_sigungu")
    @ResponseBody
    public List<String> getSigungu(@RequestParam String siDo) {
        String mappedSiDo = RegionMapping.mapDoName(siDo);
        Map<String, List<String>> sigunguMap = sigunguService.getSigunguData();
        return sigunguMap.getOrDefault(mappedSiDo, List.of());
    }
    
    @GetMapping("/regionMapping")
    @ResponseBody
    public Map<String, String> getRegionMapping() {
        return RegionMapping.DO_NAME_MAPPING;
    }
    
    
}
