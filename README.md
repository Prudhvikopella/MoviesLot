# 🎬 HumanHere Assignment 2 — Movie App

This is the **second assignment** submission for HumanHere. The project is a Jetpack Compose-based Android app built with modern best practices and TMDB APIs. The focus is on screen implementation, modularity, responsiveness, and clean UI/UX.

---

## 📁 Project Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/humanhere-assignment2.git
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
  - ✅ Bottom Navigation Bar
  - ✅ Bottom Sheet filters
  - ✅ Swipe Card Gesture support
  - ✅ Compose animations
  - ✅ Adaptive layout (Landing Page only)

### 🔁 Networking & Data

- ✅ TMDB APIs used to fetch:
  - Popular movies
  - Trending content
  - Person details
- ✅ Retrofit for API integration
- ✅ Koin for dependency injection
- ✅ Modular structure with a clean separation of concerns
- ✅ Caching to persist data over network changes

### 🖼️ Image Display and Caching (HIGH PRIORITY)

- ✅ Display of:
  - Movie **posters**
  - **Person** (cast & crew) images
  - **Backdrop** images
- ✅ Image caching for performance:
  - **Memory and disk caching** implemented

### 💾 Persistence & State

- ✅ SharedPreferences for local data
- ✅ Loading indicators while fetching data
  - Note: Indicators may appear unevenly placed due to time constraints

---

## ⚠️ Known Limitations / TODOs

- ❗ Full adaptive layouts implemented **only on the Landing Page**
- ❗ Other screens are currently optimized only for **compact portrait mode**
- ⚠️ Loading indicators may appear **misaligned**
- ❗ Primary focus was **presentation and max feature implementation**, not code repetition or polish on non-functional features
- 🔜 Expand responsive design to all screens

---

## 📦 APK & Demo

### 🔗 Download APK

📥 [**Click here to download APK**](https://your-link.com/apk)  
(To install the APK: transfer it to your Android phone and open it. You may need to allow installation from unknown sources.)

### 🎥 Watch Demo Video

📺 [**Click here to watch the demo**](https://your-link.com/demo)  
(Demo covers landing page, navigation, filters, and gesture UI.)

---

## 📸 Screenshots

> Below are representative screenshots of the app UI (add your actual images):

| Landing Page | Movie Details | Filter Bottom Sheet |
|--------------|----------------|---------------------|
| ![Landing](screenshots/landing.png) | ![Details](screenshots/details.png) | ![Filter](screenshots/filter.png) |

| Swipe Card Gesture | Trending Tab |
|--------------------|--------------|
| ![Swipe](screenshots/swipe.png) | ![Trending](screenshots/trending.png) |

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

## 👨‍💻 Author

**Prudhvi**  
📧 `prudhvi@floatvoicechat.com`
