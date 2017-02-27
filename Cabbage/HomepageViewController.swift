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

    
    //When the recipe is finished downloading RB
    func onRecipeReady(recipe: Recipe) {
        //Add the completed Recipe to the list RB
        recipeList += [recipe]

        //DEBUG PLEASE REMOVE
        print("Recipe Name: " + recipe.getTitle())
        
        //FORCE THE LISTVIEW TO UPDATE RB
        UpdateListView()
    }
    
    func UpdateListView()
    {
        //Take the background thread and execute on main thread, Not working at the moment...
        DispatchQueue.main.async { [unowned self] in
            self.tableView.reloadData()
        }
    }
    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return recipeList.count
    }
    
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = UITableViewCell(style: UITableViewCellStyle.default, reuseIdentifier: "RecipeCell") as! RandomMealTableViewCell
        cell.recipeTitle.text = "bollocks" //recipeList[indexPath.row].getTitle()
        
        return cell
    }
    
    @IBAction func unwindToHomepage(segue: UIStoryboardSegue) {}
    
}

