//
//  ObservableClipboardViewModel.swift
//  iosApp
//
//  Created by Илья Шевцов on 26.03.2026.
//

import ComposeApp
import Combine

@MainActor
class ObservableClipboardViewModel: ObservableObject {
    let viewModel: ClipboardViewModel
    
    @Published var state: ClipboardState = ClipboardStateLoading()
    
    init(viewModel: ClipboardViewModel) {
        self.viewModel = viewModel

        self.state = viewModel.state.value
    }
    
    func activate() async {
        let stateTask = Task {
            for await newState in viewModel.state {
                self.state = newState
            }
        }
        
        let sideEffectTask = Task {
            for await effect in viewModel.sideEffect {
                handleSideEffect(effect)
            }
        }
        
        _ = await [stateTask.value, sideEffectTask.value]
    }
    
    private func handleSideEffect(_ effect: ClipboardSideEffect) {
        if let snackbar = effect as? ClipboardSideEffectShowSnackbar {
            let string = StringResourceKt.getStringFromRes(resource: snackbar.message)
            print("Show alert: \(string)")
        }
    }
}
