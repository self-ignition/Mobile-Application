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
    let back: HomepageViewController

    //Random Recipe
    init(callBack: HomepageViewController){
        let server = serverRequests()
        back = callBack
        server.downloadRecipe(callBack: self)
        
    }
    
    //ID Recipe
    init(id: String, callBack: HomepageViewController) {
        back = callBack
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
    }
}
