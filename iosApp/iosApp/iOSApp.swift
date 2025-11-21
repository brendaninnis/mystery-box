
import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        // initializeDependencies()
        // initializeSentry()
        IAPRepositoryKt.initializeIAP()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
