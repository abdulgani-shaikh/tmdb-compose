# TMDB Android App

Welcome to the TMDB Android App! This application allows you to explore movies and TV shows using the TMDB (The Movie Database) API. Built with Jetpack Compose, the app offers a modern interface for a seamless user experience.

## Features
- Search for specific movies, TV shows and actors
- View detailed information about movies
- Browse trending movies and TV shows
- Discover popular and top-rated movies and shows

## Setup Instructions

1. **Clone the Repository**
   ```
   git clone https://github.com/abdulgani-shaikh/tmdb-compose.git
   cd tmdb-app
   ```
2. **Create secret.properties File**
   In the root directory of the project, create a file named secret.properties. This file is used to securely store your TMDB API key.
   `secret.properties`
    ```
    API_KEY = your_tmdb_api_key_here //Replace your_tmdb_api_key_here with your actual TMDB API key.
    ```

3. **Add Firebase Configuration**
    - Go to the Firebase Console.
    - Create a new project or use an existing one.
    - Register your Android app with Firebase by adding your app’s package name.
    - Download the google-services.json file from the Firebase Console.
    - Place the google-services.json file in the app directory of your project.

4. **Update Gradle Files for Firebase**

    - Open your build.gradle files and add the required Firebase dependencies.
      `Project-level`:`build.gradle`
        ```
        buildscript {
            dependencies {
                classpath 'com.google.gms:google-services:4.3.15'
            }
        }
        ```
      `App-level`:`build.gradle`
        ```
        plugins {
            id 'com.android.application'
            id 'com.google.gms.google-services'
        }
     
        dependencies {
            implementation 'com.google.firebase:firebase-analytics:21.3.0'
            // Add other Firebase dependencies as needed
        }
        ```

5. **Sync Gradle**
   Open the project in Android Studio and sync the Gradle files to resolve dependencies.

6. **Run the App**
   Connect an Android device or start an emulator. Run the app through Android Studio to see it in action.

## Configuration
- **API Key**
  The app relies on the TMDB API for data. Ensure your secret.properties file is properly set up with a valid API key.

- **Gradle Configuration**
  The build.gradle files include dependencies for Jetpack Compose and other libraries. If you update or add dependencies, sync Gradle to apply changes.

## Troubleshooting
- **API Key Errors**
  Verify that secret.properties is correctly placed in the root directory and contains a valid TMDB API key. Check logs for specific error messages related to API requests.

- **Firebase Configuration Issues**
  Ensure that the google-services.json file is correctly placed in the app directory and that Firebase dependencies are properly configured in your build.gradle files.

- **Build Issues**
  If you encounter build issues, try cleaning and rebuilding the project:
    ```
    ./gradlew clean
    ./gradlew build
    ```

## Credits

- **UI Design**: [Figma Design by Community Contributor](https://www.figma.com/design/qgJNKIr6yqgiCPDOpZU4dK/TMDB-(Community)?node-id=66-2&node-type=canvas&t=VU0pep0nuKJbeNu0-0)  
  The UI design for this app was sourced from a publicly available Figma file found online. We appreciate the contributions of the community in sharing design resources.

## Contributing
Contributions are welcome! To contribute:

## Contact
For questions or support, reach out to shkhabdulgani@gmail.com