# ExpenseTracker-Android-App

This repository contains an Android App with which the user can manage his/her uploaded financial activities. This app was implemented for the course [Software Engineering 2, University of Vienna, Winter Semester 2021](https://ufind.univie.ac.at/de/course.html?lv=051050&semester=2021W) as a group project alongside with the team members @abdday, @Jas1r and @Momo1970.

The following functionalities are supported by the app: 

1. CRUD operations on **accounts** which can be of various types (e.g.  Cash, Bank Account, Card, Stock, or user defined) 
2. CRUD operations on **categories** which can be assigned to **incomes** (e.g.  Salary, Dividend, or user defined) and **expenses** (e.g.  Food, Transportation, Education, or user defined)
3. CRUD operations on **transactions** which can be either **incomes** or **expenses**
4. Viewing **reports** with the uploaded transactions displayed in a list (that can be sorted by category and time periods), on a line graph and on a bar graph
5. Receiving a **warning** on the screen if the account balance is approaching a certain treshold

## Technical information

- Framework: Android (Java)
- minSdk: 26, targetSdk: 31, compileSdk: 31
- Devices on which the app was tested:
  - Phone Pixel 2, Release: Q (API Level 29), Target: Android 10.0
  - Phone Pixel XL, (API Level 30), Target: Android 10.0
  - Phone Samsung Galaxy A40, having Android 11
- For persistent storage of all the elements mentioned above, the library [Android Room](https://developer.android.com/jetpack/androidx/releases/room) was used
- For augmenting the reports with graphical representations (line and bar graphs), the library [GraphView](https://github.com/jjoe64/GraphView) was used
