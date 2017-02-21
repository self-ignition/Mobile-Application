//
//  LogInViewController.swift
//  Cabbage
//
//  Created by Bartlomiej Morawiec on 10/02/2017.
//  Copyright © 2017 Self-Ignition. All rights reserved.
//

import UIKit

class LogInViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var LogInScrollView: UIScrollView!
    
    @IBOutlet weak var UsernameTextField: UITextField!
    
    @IBOutlet weak var PasswordTextField: UITextField!
    
    @IBOutlet weak var LogInButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        
        return true
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        LogInScrollView.setContentOffset(CGPoint(x: 0,y: 60), animated: true)
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        LogInScrollView.setContentOffset(CGPoint(x: 0,y: 0), animated: true)
    }
    
    @IBAction func cancelToLogInViewController(segue:UIStoryboardSegue) {
    }
    
    @IBAction func unwindToLogIn(segue: UIStoryboardSegue) {
    }

    //Funciton to create an alert message. CRW
    func displayMyAlertMessage(userMessage:String) {
        //Creating an alert called my alert
        let myAlert = UIAlertController(title:"Alert", message:userMessage, preferredStyle: UIAlertControllerStyle.alert);
        //Creating an aciton called okAciton
        let okAction = UIAlertAction(title:"OK", style:UIAlertActionStyle.default, handler:nil);
        //adding the action to the alert
        myAlert.addAction(okAction);
        //creating a new view controller of itself with the alert in
        self.present(myAlert, animated:true, completion:nil);
    }
    
    //When Login is tapped
    @IBAction func loginButtonTapped(_ sender: Any) {
        
        let userEmail = UsernameTextField.text!;
        let userPassword = PasswordTextField.text!;
        
        //Check for empty fields. CRW
        if((userEmail.isEmpty) || (userPassword.isEmpty))
        {
            //display alert message and return
            displayMyAlertMessage(userMessage: "All Fields Must Be Filled");
            return;
        }
        
        //Create the Connection
        
        let myUrl = NSURL(string: "http://computing.derby.ac.uk/~cabbage/login.php")
        let request = NSMutableURLRequest(url: myUrl! as URL)
        //Set Method to POST. CRW
        request.httpMethod = "POST"
        //Make the string the details required. CRW
        let postString = "email=\(userEmail)&password=\(userPassword)"
        //set the values and encode it. CRW
        request.httpBody = postString.data(using: String.Encoding.utf8)
        //set paramaters of session. CRW
        let task = URLSession.shared.dataTask(with: request as URLRequest){data, response, error in
            if error != nil {
                print("ERROR ****\(error)")
                return
            }
            
        }
        task.resume()
        
        //Check if email entered is the same as server response
        
        //Check if password entered is the same as server response
        
        
    }
    
    
}
