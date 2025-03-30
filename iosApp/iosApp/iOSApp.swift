import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        Shared.CommonModuleKt.doInitKoin()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
