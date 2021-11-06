# Tinder/Bumble right swipe

Do you think all those right swipes that did not match are a waste of your time & that you would rather choose to blindly swipe right & later upon match filter as per your choice. 

You will do exactly those things here.

##Architecture
We will use adb to copy files to your device(here device mean either emulator or a real device), install apps etc.
I used Android Studio in my case, checkout this for how to install & use [Android Studio](https://developer.android.com/studio/install).
We would be using the android emulator & adb from Android Studio(If interested you can separately get those only, [genymotion](https://www.genymotion.com/download/) is a good emulator) for our purpose.

We will be using [Appium](https://appium.io/) as our automation framework.

Start the emulator, make sure it's visible using `adb devices -l`.

Download [Tinder](https://en.softonic.com/download/tinder/android/post-download), [Bumble](https://en.softonic.com/download/bumble/android/post-download) apk & install them to emulator/device 
Install to emulator using
`adb install "path/to/downloaded/app.apk"`

##Running the code
Install the project using `mvn clean dependency:copy-dependencies package`, this creates an executable jar in the target folder.

All the configs needed to execute the process are stored in `.bumble.rc & .tinder.rc` files, `source ./.tinder.rc` to export those configs as your environment variables.
Then execute the jar created above  by running `java -jar target/appiumTest-1.0-SNAPSHOT.jar 1000` 1000 being the number of different profiles you want to swipe right.