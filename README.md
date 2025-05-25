# 🎬 HumanHere Assignment 2 — Movie App

This is the **second assignment** submission for HumanHere. The project is a Jetpack Compose-based Android app built with modern best practices and TMDB APIs. The focus is on screen implementation, modularity, responsiveness, and clean UI/UX.

---

## 📁 Project Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/Prudhvikopella/MoviesLot.git
   ```

2. **Get your TMDB API key**
   - Sign up at [https://www.themoviedb.org](https://www.themoviedb.org)
   - Navigate to your [API settings](https://www.themoviedb.org/settings/api) and generate an API key.

3. **Add your API key**
   - Replace the placeholder in `ApiConstants.kt`:
     ```kotlin
     const val API_KEY = "YOUR_TMDB_API_KEY"
     ```

4. **Open the project in Android Studio** (Giraffe or newer recommended)

5. **Build and run** on an emulator or physical device (API 26+)

---

## 🚀 Implemented Features

### 🖼️ UI and UX

- 5 Core Screens built with:
  - ✅ **Bottom Navigation Bar**
  - ✅ **Bottom Sheet filters**
  - ✅ **Swipe Card Gesture** support
  - ✅ **Compose animations**
  - ✅ **Adaptive layout** (Landing Page only)

### 🔁 Networking & Data

- ✅ **TMDB APIs** used to fetch:
  - Popular movies
  - Trending content
  - Person details
- ✅ **Retrofit** for API integration
- ✅ **Koin** for dependency injection
- ✅ Modular structure with a clean separation of concerns
- ✅ Caching to persist data over network changes

### 🖼️ Image Display and Caching (HIGH PRIORITY)

- ✅ Display of:
  - Movie **posters**
  - **Person** (cast & crew) images
  - **Backdrop** images
- ✅ Image caching for performance:
  - **Memory and disk caching** implemented (via Coil/Glide)

### 💾 Persistence & State

- ✅ **SharedPreferences** for local data
- ✅ **Loading indicators** while fetching data
  - Note: Indicators may appear unevenly placed due to time constraints

---

## ⚠️ Known Limitations / TODOs

- ❗ Full **adaptive layouts** implemented **only on the Landing Page**
- ❗ Other screens are currently optimized only for **compact portrait mode**
- ⚠️ **Loading indicators** may appear **misaligned**
- ❗ Primary focus was **presentation and max feature implementation**, not code repetition or polish on non-functional features
- ❗ **iOS-style dynamic background blur (glassmorphism)** is **not natively supported** in Android like in iOS. Alternatives (e.g., RenderScript or BlurModifier) are limited and inconsistent across devices.
- 🔜 Expand responsive design to all screens

---

## 📦 APK & Demo

- ✅ [**Download APK here**](https://github.com/Prudhvikopella/MoviesLot/blob/master/movieslot.apk) 
- 🎥 [**Watch demo video**](https://your-link.com/demo) *(Optional)*

To install the APK:
- Download the APK on your Android device.
- Open file manager and locate the APK.
- Allow installation from unknown sources (if prompted).
- Install and launch the app.

To watch the demo video:
- Click on the video link.
- The video will walk through the UI/UX and features implemented.

---

## 📂 Tech Stack

| Component              | Tool/Framework           |
|------------------------|--------------------------|
| Language               | Kotlin                   |
| UI Framework           | Jetpack Compose          |
| Navigation             | Bottom Bar, Bottom Sheet |
| Architecture           | MVVM                     |
| Dependency Injection   | Koin                     |
| Networking             | Retrofit + TMDB API      |
| Image Loading          | Coil (with caching)      |
| Data Persistence       | SharedPreferences        |
| IDE                    | Android Studio           |
| Caching                | Memory + Disk            |
| UX Features            | Animations, Swipe Cards  |

---

## 🌐 API Used

- 🎬 [TMDB API](https://developer.themoviedb.org/) — Used for fetching:
  - Movies
  - Persons
  - Backdrops and images

---

## 📸 Screenshots

> Below are representative screenshots of the app UI.

### 🏠 Landing Page

| Landing 1 | Landing 2 |
|-----------|-----------|
| ![Landing1](https://github.com/Prudhvikopella/MoviesLot/blob/master/landing_page_1.png) | ![Landing2](https://github.com/Prudhvikopella/MoviesLot/blob/master/landing_page_2.png) |

### 📄 Movie Details Page

| Details 1 | Details 2 | Details 3 |
|-----------|-----------|-----------|
| ![Details1](https://github.com/Prudhvikopella/MoviesLot/blob/master/movie_detail_1.png) | ![Details2](https://github.com/Prudhvikopella/MoviesLot/blob/master/movie_detail_2.png) | ![Details3](https://github.com/Prudhvikopella/MoviesLot/blob/master/movie_detail_3.png) |

### 🔍 Filter Bottom Sheet

| Filter |
|--------|
| ![Filter](https://github.com/Prudhvikopella/MoviesLot/blob/master/filter_menu.png) |

### 👆 Swipe Card Gesture

| Swipe |
|-------|
| ![Swipe](https://github.com/Prudhvikopella/MoviesLot/blob/master/swipegesture.png) |

### 📈 Trending Tab

| My Lists |
|----------|
| ![Trending](https://github.com/Prudhvikopella/MoviesLot/blob/master/mylist.png) |

---

## 👨‍💻 Author

**Prudhvi**  
📧 `prudhvi@floatvoicechat.com`
