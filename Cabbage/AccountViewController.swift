//
//  AccountViewController.swift
//  Cabbage
//
//  Created by Bartlomiej Morawiec on 25/02/2017.
//  Copyright © 2017 Self-Ignition. All rights reserved.
//

import UIKit

class AccountViewController: UIViewController {

    @IBOutlet weak var changePassword: UIButton!
    
    @IBOutlet weak var logOut: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func unwindToAccount(segue: UIStoryboardSegue) {}
    
    @IBAction func unwindToLogIn(_ sender: UIBarButtonItem) {
        performSegue(withIdentifier: "unwindToLogIn", sender: self)
    }
    
    @IBAction func changePasswordIsTapped(_ sender: UIButton) {
        
    }

    //Reset user passwords and take you back to the home page
    @IBAction func logOutTapped(_ sender: UIButton) {
        let defaults = UserDefaults.standard
        defaults.set(nil, forKey:"email")
        defaults.set(nil, forKey:"password")
        defaults.synchronize()
        let login = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "Login")
        // Bounce back to the main thread to update the UI. CRW
        DispatchQueue.main.async {
            self.present(login, animated: true)
        }
    }
}
