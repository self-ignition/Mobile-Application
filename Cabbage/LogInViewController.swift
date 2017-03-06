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
    
    @IBOutlet weak var loginToggle: UISwitch!
    
    // Do any additional setup after loading the view.
    override func viewDidLoad() {
        super.viewDidLoad()
        //if defaults have values then send to server requests and log in 
        let defaults = UserDefaults.standard
        let emailStored = defaults.string(forKey: "email")
        let passwordStored = defaults.string(forKey: "password")
        let server = serverRequests()
        //For DEBUG
        print("PASSWORD STORED ****\(passwordStored)")
        if(emailStored != nil && passwordStored != nil){
            //do something
            server.logIn(email: emailStored!, password: passwordStored!, callBack: self)
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        
    }
    
    @IBAction func cancelToLogInViewController(segue:UIStoryboardSegue) {}
    
    @IBAction func unwindToLogIn(segue: UIStoryboardSegue) {}
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        
        return true
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
        
        let userEmail = UsernameTextField.text!
        let userPassword = PasswordTextField.text!
        
        //Check for empty fields. CRW
        if((userEmail.isEmpty) || (userPassword.isEmpty)){
            //display alert message and return
            displayMyAlertMessage(userMessage: "All fields must be filled")
            return
        }
        else{
            let server = serverRequests()
            server.logIn(email: userEmail, password: userPassword, callBack: self)
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
                let login = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "TabBar1") as! UITabBarController
                if(self.loginToggle.isOn){
                    //Save user data to device
                    let defaults = UserDefaults.standard
                    let email = self.UsernameTextField.text!
                    let password = self.PasswordTextField.text!
                    defaults.set(email, forKey:"email")
                    defaults.set(password, forKey:"password")
                    defaults.synchronize()
                }
                // Bounce back to the main thread to update the UI. CRW
                DispatchQueue.main.async {
                    self.present(login, animated: true)
                }
            }
            
        }
        else{
            DispatchQueue.global(qos: .userInitiated).async {
                let login = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "Login") as! LogInViewController
                DispatchQueue.main.async {
                    //Pop Up alert with relevant message as to why they couldnt log in 
                    self.displayMyAlertMessage(userMessage:dataStringArr[1])
                    self.present(login, animated: true)
                }
                
            }
            
        }

    }
    
}
