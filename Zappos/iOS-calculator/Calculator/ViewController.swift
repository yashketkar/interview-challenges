//
//  ViewController.swift
//  Calculator
//
//  Created by Ketkar, Yash Sumant on 2/2/16.
//  Copyright © 2016 Ketkar, Yash Sumant. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    var isTypingNumber = false
    var firstNumber = 0.0
    var secondNumber = 0.0
    var operation = ""

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBOutlet weak var calculatorDisplay: UILabel!

    @IBAction func numberTapped(sender: AnyObject) {
        let number = sender.currentTitle!!
        
        if isTypingNumber {
            calculatorDisplay.text = calculatorDisplay.text! + number
        } else {
            calculatorDisplay.text = number
            isTypingNumber = true
        }
    }
    @IBAction func calculationTapped(sender: AnyObject) {
        isTypingNumber = false
        firstNumber = Double(calculatorDisplay.text!)!
        operation = sender.currentTitle!!
    }
    @IBAction func equalsTapped(sender: AnyObject) {
        
        isTypingNumber = false
        var result = 0.0
        secondNumber = Double(calculatorDisplay.text!)!
        
        if operation == "+" {
            result = firstNumber + secondNumber
        } else if operation == "-" {
            result = firstNumber - secondNumber
        } else if operation == "X" {
            result = firstNumber * secondNumber
        } else if operation == "/" {
            result = firstNumber / secondNumber
        }
        
        calculatorDisplay.text = "\(result)"
    }
    
    
    @IBAction func allClearTapped(sender: AnyObject) {
        isTypingNumber = false
        firstNumber = 0.0
        secondNumber = 0.0
        operation = ""
        calculatorDisplay.text="0"
    }
}

