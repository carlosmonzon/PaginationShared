import SwiftUI
import Shared

struct ContentView: View {
    let viewModel = PopularsViewModel()
    @State var movies: [Movie] = []
    @State private var showError: Bool = false
    @State private var showNextPage: Bool = false
    @State private var showLoadingPlaceHolder: Bool = false
    
    var body: some View {
        List {
            ForEach(movies, id: \.self) { movie in
                MovieView(movie: movie)
                    .padding()
                    .frame(maxWidth: .infinity)
                    .listRowBackground(Color.clear)
                    .listRowInsets(EdgeInsets())
            }.listRowSeparator(.hidden)
            
            if showLoadingPlaceHolder {
                HStack {
                    ProgressView("Loading placeholder")
                }
                .id(UUID()) //this is required in list
                .frame(maxWidth: .infinity)
                .listRowInsets(EdgeInsets())
                .listRowBackground(Color.clear)
            }
            
            if showNextPage && showError == false && !movies.isEmpty {
                ProgressView()
                .id(UUID()) //this is required in list
                .frame(maxWidth: .infinity)
                .listRowBackground(Color.clear)
                .scaleEffect(2.0, anchor: .center)
                .onAppear {
                    // trigger load more pages when this view is in the portrait
                    viewModel.loadMore()
                }
            }
            
            if showError  {
                Text("Error occurred").onTapGesture {
                    viewModel.retry()
                    withAnimation {
                        self.showError = false
                    }
                }
            }
        }.task {
            await collectMovies()
        }.task {
            await collectLoadState()
        }
        .listStyle(.plain)
    }

    func collectMovies() async {
        for await movies in viewModel.moviesSnapshotList {
            self.movies = movies as! [Movie]
        }
    }
    
    private func collectLoadState() async {
        for await loadState in viewModel.loadStateFlow {
            switch onEnum(of: loadState?.append) {
            case .error(_):
                withAnimation {
                    self.showError = true
                    self.showLoadingPlaceHolder = false
                }
            case .loading(_):
                break
            case .notLoading(let state):
                self.showNextPage = !state.endOfPaginationReached
            case .none:
                break
                
            }
            
            switch onEnum(of: loadState?.refresh) {
            case .error(_):
                withAnimation {
                    self.showError = true
                    self.showLoadingPlaceHolder = false
                }
            case .loading(_):
                self.showLoadingPlaceHolder = true
            case .notLoading(_):
                self.showLoadingPlaceHolder = false
            case .none:
                break
            }
        }
    }
}

struct MovieView: View {
    var movie: Movie
    var body: some View {
        VStack(alignment: .leading) {
            if let posterPath = movie.posterPath {
                AsyncImage(url: URL(string: "https://image.tmdb.org/t/p/w500\(posterPath)")) { image in
                    image.resizable().scaledToFit()
                } placeholder: {
                    ProgressView()
                }
                .frame(width: 80, height: 120)
                .cornerRadius(8)
            }
            Spacer(minLength: 8)
            Text(movie.title)
                .font(.headline)
            Spacer(minLength: 4)
            Text(movie.overview)
                .font(.caption)
        }
        .padding()
        .background(Color(.systemBackground))
        .cornerRadius(10)
        .shadow(color: Color.black.opacity(0.2), radius: 5, x: 0, y: 2)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
