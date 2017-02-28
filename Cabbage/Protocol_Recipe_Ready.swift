//
//  Protocol_Recipe_Ready.swift
//  Cabbage
//
//  Created by Student on 27/02/2017.
//  Copyright © 2017 Self-Ignition. All rights reserved.
//

import Foundation
protocol RecipeReady {
    func onRecipeReady(recipe: Recipe) -> Void
}
