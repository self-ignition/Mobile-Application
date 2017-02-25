//
//  HomepageViewController.swift
//  Cabbage
//
//  Created by Bartlomiej Morawiec on 03/02/2017.
//  Copyright © 2017 Self-Ignition. All rights reserved.
//

import UIKit

class HomepageViewController: UIViewController /*UITableViewDataSource,* UITableViewDelegate, UISearchBarDelegate, UISearchDisplayDelegate*/ {

    @IBOutlet weak var randomMealNameLabel: UILabel!
    
    @IBOutlet weak var randomMealIngredientsLabel: UILabel!
    
    @IBOutlet weak var randomMealPhotoImageView: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    func onRequestComplete(recipe: String) -> Void {
       
        
    }
    
    @IBAction func unwindToHomepage(segue: UIStoryboardSegue) {}
    
}

