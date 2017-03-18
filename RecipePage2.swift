//
//  RecipePage2.swift
//  Cabbage
//
//  Created by Bartlomiej Morawiec on 18/03/2017.
//  Copyright © 2017 Self-Ignition. All rights reserved.
//

import UIKit

// Label adjusting to the text. BM
    /*extension UILabel {
 @IBOutlet weak var labelIngredients: UILabel!
        func requiredHeight() -> CGFloat {
 @IBOutlet weak var labelMethod: UILabel!
            let label:UILabel = UILabel(frame: CGRectMake(0, 0, self.frame.width, CGFloat.greatestFiniteMagnitude))
            label.numberOfLines = 0
            label.lineBreakMode = NSLineBreakMode.byWordWrapping
            label.font = self.font
            label.text = self.text
            
            label.sizeToFit()
            
            return label.frame.height
        }
    }*/

class RecipePage2: UIViewController {

    @IBOutlet weak var labelIngredients: UILabel!
    @IBOutlet weak var labelMethod: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()

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
