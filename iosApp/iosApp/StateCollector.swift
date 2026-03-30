//
//  ObservableClipboardViewModel.swift
//  iosApp
//
//  Created by Илья Шевцов on 26.03.2026.
//

import ComposeApp
import Combine


@MainActor
class StateCollector<T: AnyObject>: ObservableObject {
    @Published private(set) var value: T
    private var task: Task<Void, Never>?
    
    init(flow: SkieSwiftStateFlow<T>, initial: T) {
        self.value = initial

        self.task = Task { [weak self] in
            for await newValue in flow {
                self?.value = newValue
            }
        }
    }
    
    deinit {
        task?.cancel()
    }
}
