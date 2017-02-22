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
        let myAlert = UIAlertController(title:"Alert", message:userMessage, preferredStyle: UIAlertControllerStyle.alert)
        //Creating an aciton called okAciton
        let okAction = UIAlertAction(title:"OK", style:UIAlertActionStyle.default, handler:nil)
        //adding the action to the alert
        myAlert.addAction(okAction)
        //creating a new view controller of itself with the alert in
        self.present(myAlert, animated:true, completion:nil)
    }
    
    //When the sign up button is tapped following code will execute. CRW
    @IBAction func signupButtonTapped(_ sender: Any)
    {
        
        let userUsername = UsernameTextField.text!
        let userEmail = EmailTextField.text!
        let userPassword = PasswordTextField.text!
        let userConfirmPw = ConfirmPasswordTextField.text!
        
        //Check for empty fields. CRW
        if((userUsername.isEmpty) || (userEmail.isEmpty) || (userPassword.isEmpty) || (userConfirmPw.isEmpty)){
            //display alert message and return
            displayMyAlertMessage(userMessage: "All Fields Must Be Filled");
            return;
        }
        
        //Check if P/W Match. CRW
        if(userPassword != userConfirmPw){
            //dispaly alert message and return
            displayMyAlertMessage(userMessage: "Passwords do not match")
            return;
        }
        else{
            let server = serverRequests()
            server.signUp(username: userUsername, password: userPassword, email: userEmail, callBack: self)
        }
        
    }
    
    //when Server has made a request. CRW
    func onRequestComplete(response: String) -> Void {
        print("RESPONSE ***** \(response)")
        let dataStringArr = response.components(separatedBy: ":")
        //Get last char of first string, the 1 means all okay. CRW
        if dataStringArr[0].characters.last! == "1"{
            // Move to a background thread to do some long running work. CRW
            DispatchQueue.global(qos: .userInitiated).async {
                let login = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "Login") as!
                    LogInViewController
                
                // Bounce back to the main thread to update the UI. CRW
                DispatchQueue.main.async {
                    self.present(login, animated: true)
                }
            }
            
        }
        else{
            DispatchQueue.global(qos: .userInitiated).async {
                //let signup = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "Signup") as! SignUpViewController
                DispatchQueue.main.async {
                    //Pop Up alert with relevant message as to why they couldnt log in
                    self.displayMyAlertMessage(userMessage:"oops... Soemthing Went Wrong")
                    //self.present(signup, animated: true)
                }
                
            }
        }
        
    }
}
