import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
//        ClipboardView(viewModel: KoinHelper().getClipboardViewModel()).ignoresSafeArea()
        ComposeView()
            .ignoresSafeArea()
    }
}

