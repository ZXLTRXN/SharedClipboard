import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        KoinInitIosKt.doInitKoinIos()
        NapierInitIosKt.setupNapier()
    }

    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
