Twork – Android Application
==============

This repository is the Android Studio project for the Twork application.
It's written in Java and XML using the Android SDK.


Installation instructions
------------------------

Requirements:

* A phone running Android 4.2 Jelly Bean or later.
* Optionally, Android Studio to open the project.

Procedure:

* Upload the `app/Twork.apk` file to your Android device and install by tapping on the file.

Code structure
--------------

### Configuration and dependencies:

* `build.gradle` contains the dependencies for the project.

### Code files
The folder `/app/src/main/java/uk/ac/cam/grp_proj/mike` folder contains three packages with the Java code files. These make up the three main components of the app: the user-side interface, the database and the service.
#### `twork_app`
Contains the files that manage the user interface.
* Activities: the major, disjoint components of the user experience: Splash Activity (the entry point to the app), Setup Activity (a one-time setup process), Main Activity (contains the home screen and different menus) and Detail Activity (displaying details of computations). Transitioning between activities happens via Intents.
* Fragments: dynamic interface components, loosely corresponding to screens in the UI. Transitioning between them happens via FragmentTransitions.

#### `twork_data`
Contains the SQLite database implementation which keeps track of the computations and jobs and communicates with the UI as well as the service.
* `TworkDBHelper`: a singleton helper class for the database with auxiliary methods to help modifying and querying the database.
* `Computation`: the in-app representation of a Computation, maps to a single record in the `computation` table.

#### `twork_service`
Contains the implementation of the Android background service and all the client-side classes that perform the computations.

&nbsp;

&copy; 2016 Laura Nechita (redls), James Wood (laMudri), Dmitrij Szamozvancev (DimaSamoz)
