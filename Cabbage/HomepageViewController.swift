//
//  HomepageViewController.swift
//  Cabbage
//
//  Created by Bartlomiej Morawiec on 03/02/2017.
//  Copyright © 2017 Self-Ignition. All rights reserved.
//

import UIKit

class HomepageViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, RecipeReady /*UITableViewDataSource,* UITableViewDelegate, UISearchBarDelegate, UISearchDisplayDelegate*/ {

    //Added RECIPEREADY to the class, Swift Protocol = interface
    //List of recipes to be passed to the ListView RB
    var recipeList : Array<Recipe> = []
    
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        populateList()
        // Do any additional setup after loading the view, typically from a nib.
        
        //Make 6 Recipe calls RB
        var i: Int = 0
        while(i <  6)
        {
            //Create the recipe, swift is complaing because i don't then mess w/ the var
            let r : Recipe = Recipe(RecipeReadyCallback: self)
            i += 1
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func onRequestComplete(recipe: String) -> Void {
       
        
        return cell
    }
    
    @IBAction func unwindToHomepage(segue: UIStoryboardSegue) {}
    
    var recipeList: Array<Recipe> = []

    func populateList(){
        //request from recipe class
        //add to array
        var i = 0
        while(i < 6){
            recipeList += [Recipe(callBack: self)]
            i += 1
        }
    }

}

