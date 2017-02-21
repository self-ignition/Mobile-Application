//
//  SignUpViewController.swift
//  Cabbage
//
//  Created by Bartlomiej Morawiec on 10/02/2017.
//  Copyright © 2017 Self-Ignition. All rights reserved.
//

import UIKit
import Foundation

class SignUpViewController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var ScrollView: UIScrollView!
    
    @IBOutlet weak var UsernameTextField: UITextField!
    
    @IBOutlet weak var EmailTextField: UITextField!
    
    @IBOutlet weak var PasswordTextField: UITextField!
    
    @IBOutlet weak var ConfirmPasswordTextField: UITextField!
    
    @IBOutlet weak var SignUpButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool
    {
        textField.resignFirstResponder()
        
        return true
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField)
    {
        ScrollView.setContentOffset(CGPoint(x: 0, y: 60), animated: true)
    }
    
    func textFieldDidEndEditing(_ textField: UITextField)
    {
        ScrollView.setContentOffset(CGPoint(x: 0, y: 0), animated: true)
    }
    
    @IBAction func unwindToLogIn(_ sender: UIButton) {
        self.performSegue(withIdentifier: "unwindToLogInSegue", sender: self)
    }
    
    //Funciton to create an alert message. CRW
    func displayMyAlertMessage(userMessage:String)
    {
        //Creating an alert called my alert
        let myAlert = UIAlertController(title:"Alert", message:userMessage, preferredStyle: UIAlertControllerStyle.alert);
        //Creating an aciton called okAciton
        let okAction = UIAlertAction(title:"OK", style:UIAlertActionStyle.default, handler:nil);
        //adding the action to the alert
        myAlert.addAction(okAction);
        //creating a new view controller of itself with the alert in
        self.present(myAlert, animated:true, completion:nil);
    }
    
    //When the sign up button is tapped following code will execute. CRW
    @IBAction func signupButtonTapped(_ sender: Any)
    {
        
        let userUsername = UsernameTextField.text!;
        let userEmail = EmailTextField.text!;
        let userPassword = PasswordTextField.text!;
        let userConfirmPw = ConfirmPasswordTextField.text!;
        
        //Check for empty fields. CRW
        if((userUsername.isEmpty) || (userEmail.isEmpty) || (userPassword.isEmpty) || (userConfirmPw.isEmpty))
        {
            //display alert message and return
            displayMyAlertMessage(userMessage: "All Fields Must Be Filled");
            return;
        }
        
        //Check if P/W Match. CRW
        if(userPassword != userConfirmPw)
        {
            //dispaly alert message and return
            displayMyAlertMessage(userMessage: "Passwords do not match")
            return;
        }
        
        //Add logic to store data here. CRW
        
        //Create URL. CRW
        let myUrl = NSURL(string: "http://computing.derby.ac.uk/~cabbage/signup.php")
        let request = NSMutableURLRequest(url: myUrl! as URL)
        //Set Method to POST. CRW
        request.httpMethod = "POST"
        //Make the string the details required. CRW
        let postString = "username=\(userUsername)&email=\(userEmail)&password=\(userPassword)"
        //set the values and encode it. CRW
        request.httpBody = postString.data(using: String.Encoding.utf8)
        //set paramaters of session. CRW
        let task = URLSession.shared.dataTask(with: request as URLRequest){data, response, error in
            if error != nil {
                print("ERROR ****\(error)")
                return
            }
            let dataString =  String(data: data!, encoding: String.Encoding.utf8)
            let dataStringArr = dataString?.components(separatedBy: ":")
            //Get last char of first string
            if dataStringArr![0].characters.last! == "1"{
                
            }
            else{
                
            }
       }
        //run session
        task.resume()
    }
}
