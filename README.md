# Android Trending Repositories App
Welcome to the Trending Repositories Android app! This application allows users to explore trending GitHub repositories with advanced search and filtering options. Users can also add their favorite repositories to a list of favorites. The project is built using Java, RxJava 3, Room Database, Coil for image loading, Dagger Hilt for dependency injection, Retrofit 2 for network requests, and adheres to Material Design 2 guidelines.

## Features

- **Trending Repositories:** Browse and discover the currently trending GitHub repositories.
- **Search:** Search for repositories based on keywords.
- **Time-based Filtering:** Filter repositories based on the time range (last month, last week, last day).
- **Repository Details:** View detailed information about a selected repository, including its description, stars, forks, and language.
- **Add to Favorites:** Mark repositories as favorites and save them for quick access.
- **Favorites List:** Access your list of favorite repositories for easy reference.
- **Master-Detail Flow:** Utilizes a master-detail UI pattern for a seamless user experience. The master view displays the list of trending repositories, and selecting an item opens the detailed view with comprehensive repository information.
- **Screen Size and Orientation Support:** The app is designed to support various screen sizes and orientations, ensuring a consistent and user-friendly experience across different Android devices.

## Technologies Used

- **Java**
- **RxJava 3**
- **Room Database**
- **Coil**
- **Dagger Hilt**
- **Retrofit 2**
- **Material Design 2**
- 
## API Usage

The app utilizes the GitHub free API, which has a limited number of requests. When the request limit is exceeded, the app will display a corresponding error message to inform the user.
