//
//  PopularViewModel+Extensions.swift
//  iosApp
//
//  Created by Carlos Monzon on 29/3/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Shared
import Foundation

@MainActor
class PopularVMBridge: ObservableObject {
    
    let vm = PopularsViewModel()
    @Published var movies: [Movie] = []
    
    func activate() async {
        for await movies in vm.moviesSnapshotList {
            self.movies = movies as! [Movie]
        }
    }
}
