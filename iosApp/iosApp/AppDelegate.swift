//
//  UIApplicationDelegate.swift
//  iosApp
//
//  Created by Илья Шевцов on 07.03.2026.
//
import FirebaseCore
import SwiftUI

class AppDelegate : NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()
        return true
    }
}
