import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        KoinInitIosKt.doInitKoinIos()
        NapierInitIosKt.setupNapier()
    }

    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    let helper = KoinHelper()

    var body: some Scene {
        WindowGroup {
//            ClipboardView(viewModel: helper.getClipboardViewModel())
            ContentView()
        }
    }
}
