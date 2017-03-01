//
//  Recipe.swift
//  Cabbage
//
//  Created by Chris Roberts-Watts on 22/02/2017.
//  Copyright © 2017 Self-Ignition. All rights reserved.
//

import Foundation

class Recipe{
    
    var id: String = ""
    var title: String = ""
    var prept: String = ""
    var cookt: String = ""
    var author: String = ""
    var yield: String = ""
    
    var ingredients: Array<String> = []
    var steps: Array<String> = []
    
    var callback : RecipeReady
    
    //Getters RB
    func getId() -> String{
        return self.id
    }
    func getTitle() -> String{
        return self.title
    }
    func getPrepTime() -> String{
        return self.prept
    }
    func getCookTime() -> String{
        return self.cookt
    }
    func getAuthor() -> String{
        return self.author
    }
    func getYield() -> String{
        return self.yield
    }
    
    //Setters RB
    func setId(id: String) -> Void{
        self.id = id
    }
    func setTitle(title: String) -> Void{
        self.title = title
    }
    func setPrepTime(PrepTime: String) -> Void{
        self.prept = PrepTime
    }
    func setCookTime(CookTime: String) -> Void{
        self.cookt = CookTime
    }
    func setAuthor(Author: String) -> Void{
        self.author = Author
    }
    func setYield(Yield: String) -> Void{
        self.yield = Yield
    }
    
    
    //Random Recipe
    init(RecipeReadyCallback: RecipeReady){
        //Set the callback, so the recipe knows the way home before you send it out into the great wild yonder.
        //It also needs to be here because swift is a whiney bitch
        callback = RecipeReadyCallback
        
        //Once the recipe has a roadmap to prevent it getting lost through bumblefuckery,
        //Talk to the server. Get a new random recipe.
        let server = serverRequests()
        server.downloadRecipe(callBack: self)
        
    }
    
    //ID Recipe
    init(RecipeReadyCallback: RecipeReady, id: String) {
        callback = RecipeReadyCallback
    }
    
    
    
    func randomRecipe(recipe: String) -> Void {
        //splits it into tables, recipe, ingredient, steps
        let tables = recipe.components(separatedBy: "¦")
        
        //splits it into columns
        var parts = tables[0].components(separatedBy: "|")
        
        setId(id: parts[0])
        setTitle(title: parts[1])
        setPrepTime(PrepTime: parts[2])
        setCookTime(CookTime: parts[3])
        setAuthor(Author: parts[4])
        setYield(Yield: parts[5])
        
        for ingSplit in tables[1].components(separatedBy: "|"){
            self.ingredients += [ingSplit]
        }
        
        for stepSplit in tables[2].components(separatedBy: "|"){
            self.steps += [stepSplit]
        }
        
        //tables[3] contains image
        callback.onRecipeReady(recipe: self)
    }
}
