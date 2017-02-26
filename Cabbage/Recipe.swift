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
    var authour: String = ""
    var yield: String = ""
    
    var ingredients: Array<String> = []
    var steps: Array<String> = []
    
    //Random Recipe
    init(){
        let server = serverRequests()
        server.downloadRecipe(callBack: self)
    }
    
    //ID Recipe
    init(id: String) {
        
    }
    
    func randomRecipe(recipe: String) -> Void {
        //splits it into tables, recipe, ingredient, steps
        let tables = recipe.components(separatedBy: "¦")
        
        //splits it into columns
        var parts = tables[0].components(separatedBy: "|")
        
        self.id = parts[0]
        self.title = parts[1]
        self.prept = parts[2]
        self.cookt = parts[3]
        self.authour = parts[4]
        self.yield = parts[5]
        
        for ingSplit in tables[1].components(separatedBy: "|"){
            self.ingredients += [ingSplit]
        }
        
        for stepSplit in tables[2].components(separatedBy: "|"){
            self.steps += [stepSplit]
        }
        
        //tables[3] contains image
        
    }
}
