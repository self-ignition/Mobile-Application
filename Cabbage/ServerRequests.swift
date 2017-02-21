//
//  ServerRequests.swift
//  Cabbage
//
//  Created by Chris Roberts-Watts on 21/02/2017.
//  Copyright © 2017 Self-Ignition. All rights reserved.
//

import Foundation
import UIKit

//Just to get server requests
class serverRequests {
    
    // Connect to the server & Return data recieved. CRW
    
    func logIn(email: String, password: String, callBack: LogInViewController) -> Void {
        
        //Make URL. CRW
        let myUrl = NSURL(string: "http://computing.derby.ac.uk/~cabbage/login.php")
        let request = NSMutableURLRequest(url: myUrl! as URL)
        
        //Set Method to POST. CRW
        request.httpMethod = "POST"
        
        //Make the string the details required. CRW
        let postString = "email=\(email)&password=\(password)"
        
        //set the values and encode it. CRW
        request.httpBody = postString.data(using: String.Encoding.utf8)
        
        //set paramaters of session. CRW
        let task = URLSession.shared.dataTask(with: request as URLRequest){data, response, error in
            if error != nil {
                print("ERROR ****\(error)")
                return
            }
            let dataString =  String(data: data!, encoding: String.Encoding.utf8)
            callBack.onRequestComplete(response: dataString!)
        }
        task.resume()
    }
}

