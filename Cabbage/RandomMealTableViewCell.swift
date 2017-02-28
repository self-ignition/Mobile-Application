//
//  RandomMealTableViewCell.swift
//  Cabbage
//
//  Created by Bartlomiej Morawiec on 16/02/2017.
//  Copyright © 2017 Self-Ignition. All rights reserved.
//

import UIKit

class RandomMealTableViewCell: UITableViewCell {

    @IBOutlet weak var randomMealPicture: UIImageView!
    
    @IBOutlet weak var randomMealIngredients: UILabel! //no longer used
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        // Configure the view for the selected state
    }
}
