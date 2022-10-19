//
//  Firebase.swift
//  iosApp
//
//  Created by Takahiro Tominaga on 2022/10/19.
//  Copyright © 2022 orgName. All rights reserved.
//
import UserNotifications
import SwiftUI
import shared
import FirebaseCore
import FirebaseMessaging

class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()
        Messaging.messaging().delegate = self

        if #available(iOS 10.0, *) {
        // For iOS 10 display notification (sent via APNS)
            UNUserNotificationCenter.current().delegate = self

            let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
            UNUserNotificationCenter.current().requestAuthorization(
                options: authOptions,
                completionHandler: { _, _ in }
            )
        } else {
            let settings: UIUserNotificationSettings =
                UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
            application.registerUserNotificationSettings(settings)
        }
          
        Messaging.messaging().token { token, error in
            if let error = error {
                print("Error fetching FCM registration token: \(error)")
            } else {
                if let token = token {
                    print("FCM registration token: \(token)")
                }
            }
        }

        application.registerForRemoteNotifications()

        return true
    }
}

extension AppDelegate: UNUserNotificationCenterDelegate {
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
    }
  
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("Failed to register with push")
    }
}

// https://firebase.google.com/docs/cloud-messaging/ios/client?hl=ja&authuser=0#monitor-token-refresh
// https://github.com/firebase/quickstart-ios/blob/5a171d3cbd937991d805ebf545ad9dae33f094f2/messaging/MessagingExampleSwift/AppDelegate.swift#L170-L182
extension AppDelegate: MessagingDelegate {
    // [START refresh_token]
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
    print("Firebase registration token: \(String(describing: fcmToken))")

    let dataDict: [String: String] = ["token": fcmToken ?? ""]
    NotificationCenter.default.post(
        name: Notification.Name("FCMToken"),
        object: nil,
        userInfo: dataDict
    )
        //FCM 登録コードをサーバーに送信。
        if let token = fcmToken {
            FCMSDK().register(token: token) { error in
                if let error = error {
                    print("error in FCMSDK().register: \(error)")
                }
            }
        }
    }

    // [END refresh_token]
}
