//package com.demo.Recipe;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.stereotype.Service;
//
//@Service
//public class Recipe10000 {
//
//	public List<Recipe> getRecipes() throws IOException {
//		String url = "https://www.10000recipe.com/recipe/list.html?q=%EC%BA%A0%ED%95%91";
//		Document doc = Jsoup.connect(url).get();
//		
//		Elements recipeItems = doc.select("li.common_sp_list_li");
//		List<Recipe> recipes = new ArrayList<>();
//		
//		for (Element item : recipeItems) {
//			String title = item.select("div.common_sp_caption_tit").text().trim();
//			String imgUrl = item.select("div.common_sp_thumb img").attr("src");
//			
//			Recipe recipe = new Recipe(title, imgUrl);
//            recipes.add(recipe);
//		}
//		return recipes;
//
//	}
//	
//	public static class Recipe {
//        private String title;
//        private String imgUrl;
//
//        public Recipe(String title, String imgUrl) {
//            this.title = title;
//            this.imgUrl = imgUrl;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public String getImgUrl() {
//            return imgUrl;
//        }
//
//        public void setImgUrl(String imgUrl) {
//            this.imgUrl = imgUrl;
//        }
//
//        @Override
//        public String toString() {
//            return "Recipe{" +
//                    "title='" + title + '\'' +
//                    ", imgUrl='" + imgUrl + '\'' +
//                    '}';
//        }
//    }
//}
