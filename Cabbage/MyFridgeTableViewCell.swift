//
//  MyFridgeTableViewCell.swift
//  Cabbage
//
//  Created by Bartlomiej Morawiec on 25/02/2017.
//  Copyright © 2017 Self-Ignition. All rights reserved.
//

import UIKit

class MyFridgeTableViewCell: UITableViewCell {

    @IBOutlet weak var ingredientLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
