<p align="center">
   <a href="https://github.com/willyborja95/RegistrateApp/releases">
    <img src="https://img.shields.io/github/release/geduo83/FlyTour.svg" alt="Latest Stable Version" />
  </a>
   <a href="https://developer.android.com/about/versions/marshmallow/android-6.0">
    <img src="https://img.shields.io/badge/API-14%2B-blue.svg?style=flat-square" alt="Min Sdk Version" />
  </a>
</p>

# Registrate App

This app in build on Java using the Android Studio IDE.
The patter used now is the MVP (Model Vie Presenter).

**View** is compose by the layout .xml and also the class Activities or Fragments.
**Model** are that models our data source.
**Presenter** en charge to communicate the View with the data source

The files are organized according this components in packages with the same name.
The interaction flow chart is specified here:
```mermaid
graph LR
A(View)
B(Presenter) --> A
B --> C(Interactor)
C --> D(Model)
C --> E(Repository)
C --> B
A --> B
```

