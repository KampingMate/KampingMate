//package com.demo.Recipe;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import com.demo.Recipe.Recipe10000.Recipe;
//
//import java.io.IOException;
//import java.util.List;
//
//@Controller
//public class RecipeController {
//
//    @Autowired
//    private Recipe10000 recipeService;
//
//    @GetMapping("/test3")
//    public String getRecipes(Model model) throws IOException {
//        List<Recipe> recipes = recipeService.getRecipes();
//        model.addAttribute("recipes", recipes);
//        return "NewFile3";
//    }
//}
