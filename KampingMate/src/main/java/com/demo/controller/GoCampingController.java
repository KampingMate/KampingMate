package com.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.GoCamping;
import com.demo.dto.CampingItem;
import com.demo.persistence.GoCampingRepository;
import com.demo.service.GoCampingAPI;
import com.demo.service.RegionMapping;
import com.demo.service.SigunguService;

import jakarta.servlet.http.HttpSession;

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
    public String getNormalSearchList(@RequestParam(name = "doNm", required = false) String doNmStr,
                                      @RequestParam(name = "gungu", required = false) String gunguStr,
                                      @RequestParam(name = "faclt", required = false) String facltStr,
                                      @RequestParam(name = "lct", required = false) String lctStr,
                                      @RequestParam(name = "induty", required = false) String indutyStr,
                                      @RequestParam(name = "bottom", required = false) String bottom,
                                      @RequestParam(name = "sbrs", required = false) String sbrsStr,
                                      @RequestParam(defaultValue = "0") int page,
                                      Model model) {

        List<GoCamping> combinedResults = new ArrayList<>();

        // 각 조건에 대해 개별 쿼리를 실행하고 결과를 합침
        if (doNmStr != null) {
            List<String> doNmList = Arrays.asList(doNmStr.split(","));
            for (String doNm : doNmList) {
                combinedResults.addAll(gocampingRepo.findByDoNm(doNm));
            }
        }

        if (gunguStr != null) {
            List<String> gunguList = Arrays.asList(gunguStr.split(","));
            for (String gungu : gunguList) {
                combinedResults.addAll(gocampingRepo.findBySigunguNm(gungu));
            }
        }

        if (facltStr != null) {
            List<String> facltList = Arrays.asList(facltStr.split(","));
            for (String faclt : facltList) {
                combinedResults.addAll(gocampingRepo.findByFaclt(faclt));
            }
        }

        if (lctStr != null) {
            List<String> lctList = Arrays.asList(lctStr.split(","));
            for (String lct : lctList) {
                combinedResults.addAll(gocampingRepo.findByLctCl(lct));
            }
        }

        if (indutyStr != null) {
            List<String> indutyList = Arrays.asList(indutyStr.split(","));
            for (String induty : indutyList) {
                combinedResults.addAll(gocampingRepo.findByInduty(induty));
            }
        }

        if (sbrsStr != null) {
            List<String> sbrsList = Arrays.asList(sbrsStr.split(","));
            for (String sbrs : sbrsList) {
                combinedResults.addAll(gocampingRepo.findBySbrsCl(sbrs));
            }
        }

        if (bottom != null && !bottom.isEmpty()) {
            combinedResults.addAll(gocampingRepo.findByBottom(bottom));
        }

        // 중복 제거
        Set<GoCamping> resultSet = new HashSet<>(combinedResults);

        // 페이지네이션
        List<GoCamping> goCampingList = new ArrayList<>(resultSet);
        int start = Math.min(page * 10, goCampingList.size());
        int end = Math.min((page + 1) * 10, goCampingList.size());

        List<GoCamping> paginatedList = goCampingList.subList(start, end);
        int totalPages = (int) Math.ceil((double) goCampingList.size() / 10);

        // 조회된 캠핑장 리스트를 모델에 추가합니다.
        model.addAttribute("goCampingList", paginatedList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "searchView"; // 뷰 페이지 이름을 반환합니다.
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
    
    @GetMapping("/detailView")
    public String goDetailView(@RequestParam("contentId") int contentId, Model model) {
        // contentId를 이용해 캠핑장 정보를 조회
        GoCamping campDetail = gocampingRepo.findById(contentId).orElse(null);

        // 캠핑장 정보를 모델에 추가
        model.addAttribute("campDetail", campDetail);

        return "detailView"; // detailView 템플릿을 반환
    }
    
    @PostMapping("/getRecommendList")
    public String getRecommendListView(HttpSession session, 
                                       @RequestParam(defaultValue = "0") int page,
                                       Model model) {
        // 세션에서 recommendations를 불러옴
        List<Integer> recommendations = (List<Integer>) session.getAttribute("recommendations");

        if (recommendations == null || recommendations.isEmpty()) {
            model.addAttribute("goCampingList", new ArrayList<>());
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 0);
            return "searchView";
        }

        // 추천 캠핑장 목록 조회
        List<GoCamping> recommendedCamps = gocampingRepo.findAllById(recommendations);

        // 페이지네이션
        int start = Math.min(page * 10, recommendedCamps.size());
        int end = Math.min((page + 1) * 10, recommendedCamps.size());

        List<GoCamping> paginatedList = recommendedCamps.subList(start, end);
        int totalPages = (int) Math.ceil((double) recommendedCamps.size() / 10);

        // 조회된 캠핑장 리스트를 모델에 추가
        model.addAttribute("goCampingList", paginatedList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "searchViewRecommend";
    }
    
}
