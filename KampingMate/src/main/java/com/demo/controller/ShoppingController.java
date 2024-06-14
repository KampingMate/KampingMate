package com.demo.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.demo.dto.NShopping;
import com.demo.dto.NaverShoppingApi;
import com.demo.service.RecipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ShoppingController {
	
	@Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;
    
    @Autowired
    private RecipeService recipeService;

    @GetMapping("/recipes")
    public String getRecipes(Model model) {
        Map<String, Object> recipes = recipeService.getRecipes();
        model.addAttribute("recipes", recipes);
        return "guide/campfood";
    }
    
    @GetMapping("/detailrecipe")
    public String getDetailRecipes(@RequestParam("seq") int seq, Model model) {
        Map<String, Object> recipes = recipeService.getRecipes();
        List<Map<String, String>> filteredRecipes = filterRecipesBySeq(recipes, seq);
        if (filteredRecipes != null && !filteredRecipes.isEmpty()) {
            model.addAttribute("recipe", filteredRecipes.get(0));
        } else {
            model.addAttribute("recipe", null);
        }
        return "guide/fooddetail";
    }

    private List<Map<String, String>> filterRecipesBySeq(Map<String, Object> recipes, int seq) {
        if (recipes == null || !recipes.containsKey("COOKRCP01")) {
            return null;
        }
        Map<String, Object> cookRcp = (Map<String, Object>) recipes.get("COOKRCP01");
        List<Map<String, String>> allRecipes = (List<Map<String, String>>) cookRcp.get("row");
        return allRecipes.stream()
                .filter(recipe -> recipe.get("RCP_SEQ") != null && Integer.parseInt(recipe.get("RCP_SEQ")) == seq)
                .collect(Collectors.toList());
    }

    
    @GetMapping("/step")
   	public String gostep() {
   		 
   		return "guide/campingstep";
   	}
    
    
    @GetMapping("/shopping")
	public String goBookpage() {
		 
		return "Shopping/shoppingList";
	}
	
	//네이버 쇼핑 api	
    @GetMapping("/shoppingsearch")
    public String list(@RequestParam("searchKeyword") String searchKeyword, Model model) {
        URI uri = UriComponentsBuilder
            .fromUriString("https://openapi.naver.com")
            .path("/v1/search/shop.json")
            .queryParam("query", searchKeyword + "캠핑")
            .queryParam("display", 10)
            .queryParam("start", 1)
            .queryParam("sort", "sim")
            .encode()
            .build()
            .toUri();

        RequestEntity<Void> req = RequestEntity
            .get(uri)
            .header("X-Naver-Client-Id", clientId)
            .header("X-Naver-Client-Secret", clientSecret)
            .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.exchange(req, String.class);

        ObjectMapper om = new ObjectMapper();
        NaverShoppingApi resultVO = null;

        try {
            resultVO = om.readValue(resp.getBody(), NaverShoppingApi.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<NShopping> shopping = resultVO.getItems();
        model.addAttribute("shoppings", shopping);
        model.addAttribute("searchKeyword", searchKeyword);

        return "Shopping/shoppingResult :: #shopping_list";
    }
}


////네이버 쇼핑 트렌드 - 키워드	
//@GetMapping("/searchtrend")
//public String searchtrend(Model model, String text) {
//    URI uri = UriComponentsBuilder
//        .fromUriString("https://openapi.naver.com")
//        .path("/v1/datalab/shopping/category/keywords")
//        .queryParam("startDate", text )
//        .queryParam("endDate", text )
//        .queryParam("timeUnit", text )
//        .queryParam("category.name", text )
//        .queryParam("display", 10)
//        .queryParam("start", 1)
//        .queryParam("sort", "sim")
//        .encode()
//        .build()
//        .toUri();
//
//    RequestEntity<Void> req = RequestEntity
//        .get(uri)
//        .header("X-Naver-Client-Id", clientId)
//        .header("X-Naver-Client-Secret", clientSecret)
//        .build();
//
//    RestTemplate restTemplate = new RestTemplate();
//    ResponseEntity<String> resp = restTemplate.exchange(req, String.class);
//
//    ObjectMapper om = new ObjectMapper();
//    strend resultVO = null;
//
//    try {
//        resultVO = om.readValue(resp.getBody(), strend.class);
//    } catch (JsonMappingException e) {
//        e.printStackTrace();
//    } catch (JsonProcessingException e) {
//        e.printStackTrace();
//    }
//
//    List<strend> shopping = resultVO.getItems();
//    model.addAttribute("shoppings", shopping);
//    model.addAttribute("searchKeyword", searchKeyword);
//
//    return "Shopping/shoppingResult :: #shopping_list";
//}
//}
//
//
