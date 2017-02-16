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
    
    @IBAction func loginButtonTapped(_ sender: Any) {
        
        /*let userEmail = UsernameTextField.text;
        let userPassword = PasswordTextField.text;*/
        
        //Create a request variable with the URL for our webserver, using the POST method
        let request = NSMutableURLRequest(url: NSURL(string: "http://computing.derby.ac.uk/~cabbage/login.php") as! URL);
            request.httpMethod = "POST";
        
        //Create a string to send to the server using the username and password text fields (CURRENTLY NO LIMITED CHARACTERS)
        let postString = "a=\(UsernameTextField.text)&b=\(PasswordTextField.text)";
        
        //Encode the string
        request.httpBody = postString.data(using: String.Encoding.utf8);
        
        //Start session
        let task = URLSession.shared.dataTask(with: request as URLRequest) {
            data, response, error in
            
            if error != nil {
                print("error=\(error)")
                return
            }
            
            print("response = \(response)")
            
            let responseString = NSString(data: data!, encoding: String.Encoding.utf8.rawValue)
            print("responseString = \(responseString)")
        }
        
        task.resume()
    }
    
    
}
