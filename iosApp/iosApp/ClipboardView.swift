//
//  ClipboardView.swift
//  iosApp
//
//  Created by Илья Шевцов on 26.03.2026.
//

import SwiftUI
import ComposeApp

struct ClipboardView: View {
    private let viewModel: ClipboardViewModel
    
    @StateObject private var state: StateCollector<ClipboardState>
    @State private var alertMessage: String?
    @State private var isShowingAlert = false
    
    init(viewModel: ClipboardViewModel) {
        self.viewModel =  viewModel
        _state = StateObject(wrappedValue: StateCollector(
                flow: viewModel.state,
                initial: viewModel.state.value
            ))
    }

    var body: some View {
        VStack(spacing: 20) {
            switch onEnum(of: state.value) {
            case .success(let success):
                Text("Remote: \(success.remoteValue)")
                    .font(.headline)
                
                Text("Local: \(success.localValue)")
                    .foregroundColor(.secondary)
                
                Button("Send Local") {
                    viewModel.process(
                        intent: ClipboardIntentSendLocal(localClipboard: success.localValue)
                    )
                }
                Button("call side effect") {
                    viewModel.process(
                        intent: ClipboardIntentCopied()
                    )
                }
            case .loading:
                ProgressView()
            case .error:
                Text("Error")
            }
            
        }.alert("Уведомление", isPresented: $isShowingAlert) {
            Button("OK", role: .cancel) {}
        } message: {
            Text(alertMessage ?? "")
        }.task {
            for await effect in viewModel.sideEffect {
                handleSideEffect(effect)
            }
        }
    }
    
    private func handleSideEffect(_ effect: ClipboardSideEffect) {
        switch effect {
        case let snack as ClipboardSideEffectShowSnackbar:
            let message = StringResourceKt.getStringFromRes(resource: snack.message)
            alertMessage = message
            isShowingAlert = true
            print(message)
        default:
            print("def sideEffect")
        }
    }
    
}
