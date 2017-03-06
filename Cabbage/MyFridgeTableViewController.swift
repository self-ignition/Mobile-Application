//
//  MyFridgeTableViewController.swift
//  Cabbage
//
//  Created by Bartlomiej Morawiec on 25/02/2017.
//  Copyright © 2017 Self-Ignition. All rights reserved.
//

import UIKit

class MyFridgeTableViewController: UITableViewController {
    
    @IBOutlet var ingredientsTable: UITableView!
    @IBOutlet weak var addIngredientButton: UIBarButtonItem!
    
    let filePath : String = "MyFridge"
    var ingredientsList : Array<String> = []
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //ingredientsList = ["milk", "chicken", "flour"] //Debug init of ingredients
        
        //writeFile(file: filePath, list: ingredientsList)
        ingredientsList = [] // Debug clear list, to see if they are read from file
        
        ingredientsList = readFile(file: filePath)
        
        SetButtonOnClick()
        updateList()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return ingredientsList.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ingredientRow", for: indexPath) as! MyFridgeTableViewCell

        cell.ingredientNameLabel.text = ingredientsList[indexPath.row]

        return cell
    }
    
    func updateList() -> Void {
        ingredientsTable.reloadData()
    }
    
    func readFile(file: String) -> Array<String> {
        var lines: Array<String> = []
        
        if let dir = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first {
            
            let path = dir.appendingPathComponent(file)
            var text = ""
            
            //reading
            do {
                text = try String(contentsOf: path, encoding: String.Encoding.utf8)
            }
            catch {/* error handling here */}
            
            lines =  text.components(separatedBy: ",")
        }
        return lines
    }
    
    func writeFile(file: String, list: Array<String>) -> Void {
        if let dir = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first {
    
            let path = dir.appendingPathComponent(file)
            var text: String = ""
            
            for s in list {
                text += s + ","
            }
            
            //writing
            do {
                try text.write(to: path, atomically: false, encoding: String.Encoding.utf8)
            }
            catch {/* error handling here */}
        }
    }
    
    func SetButtonOnClick(){
        //Set action for the button when clicked
        addIngredientButton.target = self
        addIngredientButton.action = #selector(PopupDialog)
    }
    
    func PopupDialog(){
        //1. Create Dialog
        let alert = UIAlertController(title: "Add to my fridge", message: "Enter your ingredient", preferredStyle: .alert)
        
        //2. Add the text field. You can configure it however you need.
        alert.addTextField { (textField) in
            textField.text = "Apple"
        }
        
        // 3. Grab the value from the text field, and print it when the user clicks OK.
        let okAction = UIAlertAction(title: "Add", style: UIAlertActionStyle.default)
        {
            (result : UIAlertAction) -> Void in
            self.AddToList(item: (alert.textFields?[0].text)!)
        }
        alert.addAction(okAction) 
        alert.addAction(UIAlertAction(title: "Cancel", style: UIAlertActionStyle.cancel, handler: nil))
        
        // 4. add value to the list and commit change to file
        self.present(alert, animated: true, completion: nil)
    }
    
    func AddToList(item: String) -> Void{
        ingredientsList.append(item)
        writeFile(file: filePath, list: ingredientsList)
        updateList()
    }
}
