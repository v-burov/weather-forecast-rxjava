# Weather forecast (MVVM-RxJava)

A simple application that provides the weather forecast.

- The app shows the user a map and allows him to choose a city.
- The user enters a city name, the app sends api request and shows a list of
  query suggestions.
- After the user has selected a city, pin is dropped on the map.
- The app fetches data from the network and saves it locally on android device.
- Every 24 hours the app re-fetches data from the network and updates UI.
- Error handler handles different errors such as api errors, internet connection
  and displays an error either in dialog or snack bar.

# Preview

<img src="https://github.com/v-burov/weather-app-mvvm/blob/master/forecast_rx.gif" width="360" height="640">

# Stack:

Android Architecture Components (ViewModel, Lifecycle), Retrofit, Dagger 2,
RxJava 2, GreedDao, Glide.

# Good to improve:

1. Refactor Network Helper.
2. Use interface for WeatherRepository to follow Dependency Inversion Principle.
3. Use diff utils instead of re-draw weather details in case update comes.
4. Change dynamically navigation bar title.
5. Improve UI design. 