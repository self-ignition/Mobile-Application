//
//  HomepageViewController.swift
//  Cabbage
//
//  Created by Bartlomiej Morawiec on 03/02/2017.
//  Copyright © 2017 Self-Ignition. All rights reserved.
//

import UIKit

class HomepageViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UISearchResultsUpdating, UISearchBarDelegate, RecipeReady {
    
    //Added RECIPEREADY to the class, Swift Protocol = interface
    //List of recipes to be passed to the ListView RB
    var recipeList: Array<Recipe> = []
    let numRecipes: Int = 10
    
    @IBOutlet weak var tableView: UITableView!
    
    //Variables used for search requests. BM
    var searchController: UISearchController!
    /*var dataArray = [String]()
    var filteredArray = [String]()*/
    var showSearchResults = false
    
                var filteredArray: Array<Recipe> = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        //Make 10 Recipe calls RB
        var i: Int = 0
        while(i < numRecipes)
        {
            //Create the recipe, swift is complaing because i don't then mess w/ the var
            let r : Recipe = Recipe(RecipeReadyCallback: self)
            i += 1
        }
        
        configureSearchController()
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
        print("Recipe Name: " + recipe.getTitle() + " Number of recipes in this list = " + String(recipeList.count))
        
        //FORCE THE LISTVIEW TO UPDATE RB
        UpdateListView()
    }
    
    func UpdateListView()
    {
        //Take the background thread and execute on main thread, Not working at the moment...
        DispatchQueue.main.sync {
            self.tableView.reloadData()
        }
    }
    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        
        return recipeList.count
    }
    
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "RecipeCell", for: indexPath) as! RandomMealTableViewCell
        
        cell.recipeTitle.text = recipeList[indexPath.row].getTitle()
        cell.recipeImage.image = recipeList[indexPath.row].image
        
        return cell
    }
    
    //Searching properties. BM
    func configureSearchController() {
        searchController = UISearchController(searchResultsController: nil)
        searchController.searchResultsUpdater = self
        searchController.dimsBackgroundDuringPresentation = false
        searchController.searchBar.placeholder = "Search"
        searchController.searchBar.delegate = self
        searchController.searchBar.sizeToFit()
        
        // Place the search bar view to the tableview headerview.
        tableView.tableHeaderView = searchController.searchBar
    }
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        showSearchResults = true
        tableView.reloadData()
    }
    
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        showSearchResults = false
        tableView.reloadData()
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        if !showSearchResults {
            showSearchResults = true
            tableView.reloadData()
        }
        
        searchController.searchBar.resignFirstResponder()
    }
    
    func updateSearchResults(for searchController: UISearchController) {
        let searchString = searchController.searchBar.text
        
        filteredArray = recipeList.filter({ (title) -> Bool in
            
            return ((title.getTitle().range(of: searchString!, options: NSString.CompareOptions.caseInsensitive) != nil))
        })
        
        tableView.reloadData()
    }
    
    @IBAction func unwindToHomepage(segue: UIStoryboardSegue) {}
    
}
