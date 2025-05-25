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
  - ✅ **Dark and Light Theme** support

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
  - **Memory and disk caching** implemented (via Coil)

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
- 🎥 [**Watch demo video**](https://drive.google.com/file/d/1YNRcG_v6pzwD4tmslsYHtZOJRSGXmr63/view?usp=sharing) 

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

> Below are representative screenshots of the app UI in both dark and light modes.

### 🏠 Landing Page (Light & Dark)

| Light | Dark |
|-------|------|
| ![Landing1](https://github.com/Prudhvikopella/MoviesLot/blob/master/landing_page_1.png) | ![Landing2](https://github.com/Prudhvikopella/MoviesLot/blob/master/dark_landing_page_1.png) |

| Light | Dark |
|-------|------|
| ![Landing1](https://github.com/Prudhvikopella/MoviesLot/blob/master/landing_page_2.png) | ![Landing2](https://github.com/Prudhvikopella/MoviesLot/blob/master/dark_landing_page_2.png) |

### 🎞️ Movie Details (Light & Dark)

| Detail 1 | Detail 2 | Detail 3 |
|----------|----------|----------|
| ![Details1](https://github.com/Prudhvikopella/MoviesLot/blob/master/movie_detail_1.png) | ![Details2](https://github.com/Prudhvikopella/MoviesLot/blob/master/movie_detail_2.png) | ![Details3](https://github.com/Prudhvikopella/MoviesLot/blob/master/movie_detail_3.png) |

| Detail 1 | Detail 2 | Detail 3 |
|----------|----------|----------|
| ![Details1](https://github.com/Prudhvikopella/MoviesLot/blob/master/dark_movie_detail_1.png) | ![Details2](https://github.com/Prudhvikopella/MoviesLot/blob/master/dark_movie_detail_2.png) | ![Details3](https://github.com/Prudhvikopella/MoviesLot/blob/master/dark_movie_detail_3.png) |

### 🔍 Filter Bottom Sheet (Light & Dark)

| Light | Dark |
|-------|------|
| ![Filter](https://github.com/Prudhvikopella/MoviesLot/blob/master/filter_menu.png) | ![Filter](https://github.com/Prudhvikopella/MoviesLot/blob/master/filter_dark.png) |

### 👆 Swipe Card Gesture (Light & Dark)

| Light | Dark |
|-------|------|
| ![Swipe](https://github.com/Prudhvikopella/MoviesLot/blob/master/swipegesture.png) |  ![Swipe](https://github.com/Prudhvikopella/MoviesLot/blob/master/swipegesture.png) |


### 📋 Movie List (Light & Dark)

| Light | Dark |
|-------|------|
| ![List](https://github.com/Prudhvikopella/MoviesLot/blob/master/list_light.png) |  ![List](https://github.com/Prudhvikopella/MoviesLot/blob/master/dark_list_page.png) |

### 📈 MyLists Page

| Light | Dark |
|-------|------|
| ![MyList](https://github.com/Prudhvikopella/MoviesLot/blob/master/mylist.png) |  ![MyList](https://github.com/Prudhvikopella/MoviesLot/blob/master/dark_my_lits.png) |

### 💬 Reviews Page

| Light | Dark |
|-------|------|
| ![Review](https://github.com/Prudhvikopella/MoviesLot/blob/master/reviews.png) |  ![Review](https://github.com/Prudhvikopella/MoviesLot/blob/master/dark_review.png) |

---

## 👨‍💻 Author

**Prudhvi**  
📧 `prudhvi@floatvoicechat.com`
