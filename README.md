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
The folder `/app/src/main/java/uk/ac/cam/grp_proj/mike` contains three packages with the Java code files. These make up the three main components of the app: the user-side interface, the database and the service.
#### `twork_app`
Contains the files that manage the user interface.
* Activities: the major, disjoint components of the user experience: Splash Activity (the entry point to the app), Setup Activity (a one-time setup process), Main Activity (contains the home screen and different menus) and Detail Activity (displaying details of computations). Transitioning between activities happens via Intents.
* Fragments: dynamic interface components, loosely corresponding to screens in the UI. Transitioning between them happens via FragmentTransitions.

#### `twork_data`
Contains the SQLite database implementation which keeps track of the computations and jobs and communicates with the UI as well as the service.
* `TworkDBHelper`: a singleton helper class for the database with auxiliary methods to help modifying and querying the database.
* `Computation`: the in-app representation of a Computation, maps to a single record in the `computation` table.

#### `twork_service`
Contains the implementation of the Android background service and all the client-side classes that perform the computations. Includes code for Prime Computation, Greyscale and Sepia effects, and a filter that does nothing.

### Resource files
The folder `/app/src/main/res` contains the resource files for the application, mainly written in XML.

#### `layout`
Every screen in the application needs to have a specific layout which is "inflated" when the screen is shown. This folder contains the XML files for the activity and fragment layouts. A common design pattern in Android development is to have two files for each Activity layout: one describing the static elements (e.g. toolbar at the top of the screen) and the other describing the content, which is included in the main layout. Moreover, the `content` file usually has a container for a fragment, and the fragments can be dynamically swapped in and out of this container.

#### `drawable` and `mipmap`
These folders contains the graphical assets of the application. Most of the vector icons are saved as `.xml` files with the point coordinates, but some of the icons are bitmap images. The `mipmap` folder contains the application icon in several different sizes to accomodate different screen resolutions.

#### `values`
This folder contains literal values for most of the parameters used in the application, as well as the style declarations for the UI. It is a common design practice and makes changing text used in many different places straightforward (e.g. localisation). We weren't very strict on this approach so the code and layout files contain many literal values without resource references – localisation wasn't a priority for us, and this is easily fixed using automated tools.

#### Other folders
The rest of the folders contain other files describing the layouts and values used in the application. For example, `menu` contains the list of menu elements in the navigation bar and context menus.

Improvements
-----
Although we are generally happy with the result, there are a few additions and improvements that could have been made if we had more time. Most of these didn't make it into the final report because at the time we couldn't know what we could or could not implement until the submission deadline.

#### Technical
* Although Android Studio does offer integration with the Android UI testing framework, we didn't start using it from day one and later decided that retrofitting it would take a lot of time, while not providing obvious benefits over manual trial-and-error. Time permitting, we would have done formal UI testing as well, but at the start we didn't even know that such a thing exists.
* The code is a bit sloppy at times (inconsistent naming and commenting, code smells, etc.) – again, this can be improved with time.

#### Functional
* One of the main components that we had to take out is location services: although proper code files have been written, we couldn't get the Google Maps API authorisation working, and without that it is very difficult to develop and test this functionality. 
* We also had difficulties in creating a proper Settings screen and in the end decided to include settings in the initial setup and provide the users the opportunity to redo that setup process. In the end, there weren't enough proper customisations to warrant a separate settings screen, but with more development our initial aim could be achieved.
* It would improve the user experience if the application gave visual feedback on job computations – the UI is quite static for now and the user cannot really know if anything is happening without looking at the logs.
* All in all, we are satisfied with the user interface, but there is still a lot that can be done to make it more dynamic and visually pleasing, following the Android Material Design guidelines.

&nbsp;

&copy; 2016 Laura Nechita (redls), James Wood (laMudri), Dmitrij Szamozvancev (DimaSamoz)
