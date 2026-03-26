//
//  ClipboardView.swift
//  iosApp
//
//  Created by Илья Шевцов on 26.03.2026.
//

import SwiftUI
import ComposeApp

struct ClipboardView: View {
    @StateObject private var observableModel: ObservableClipboardViewModel
    
    init(viewModel: ClipboardViewModel) {
        _observableModel = StateObject(wrappedValue: ObservableClipboardViewModel(viewModel: viewModel))
    }

    var body: some View {
        VStack(spacing: 20) {

            if let state = observableModel.state as? ClipboardStateSuccess {
                Text("Remote: \(state.remoteValue)")
                    .font(.headline)
                
                Text("Local: \(state.localValue)")
                    .foregroundColor(.secondary)
                
                Button("Send Local") {
                    observableModel.viewModel.process(intent: ClipboardIntentSendLocal(localClipboard: "New Clip"))
                }
            } else if observableModel.state is ClipboardStateLoading {
                ProgressView("Loading...")
            } else {
                Text("Something went wrong")
            }
        }
        .padding()
        .task {
            await observableModel.activate()
        }
    }
}
