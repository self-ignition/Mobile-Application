//
//  RecipePage1.swift
//  Cabbage
//
//  Created by Bartlomiej Morawiec on 16/03/2017.
//  Copyright © 2017 Self-Ignition. All rights reserved.
//

import UIKit

class RecipePage1: UIViewController {

    @IBOutlet weak var recipeImage: UIImageView!
    
    var detailRecipe: Recipe? {
        didSet {
            configureView()
        }
    }
    
    func configureView() {
        /*if let detailRecipe = detailRecipe {
            if let recipeImage = recipeImage {
                recipeImage.image = UIImage(named: detailRecipe.title)
                title = detailRecipe.title
            }
        }*/
        recipeImage.image = detailRecipe?.image
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureView()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
