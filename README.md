# Tinder/Bumble right swipe

Do you think all those right swipes that did not match are a waste of your time & that you would rather choose to blindly swipe right & later upon filter those matches as per your choice, you will do exactly those things here.

## Architecture
We will use adb to copy files to your device(here device mean either emulator or a real device), install apps etc.
I used Android Studio in my case, checkout this for how to install & use [Android Studio](https://developer.android.com/studio/install).
We would be using the android emulator & adb from Android Studio(If interested you can separately get those only for our purpose.[genymotion](https://www.genymotion.com/download/) is a good emulator)

We will be using [Appium](https://appium.io/) as our automation framework.

Start the emulator, make sure it's visible using `adb devices -l`.

Download [Tinder](https://en.softonic.com/download/tinder/android/post-download), [Bumble](https://en.softonic.com/download/bumble/android/post-download) apk & install them to emulator/device 
Install to emulator using
`adb install "path/to/downloaded/app.apk"`

## Running the code
Start appium in screen using `screen appium --no-reset`
start the emulator in screen using & remember to use emulator from sdk/emulator path only & do not forget @ `screen ~/Android/Sdk/emulator/emulator @Pixel_5_API_28` you can also start the emulator by using android studio avd manager.
Install the project using `mvn clean dependency:copy-dependencies package`, this creates an executable jar in the target folder.

All the configs needed to execute the process are stored in `src/main/resources/*.txt` files, pass the respective file to be used as argument to the program as per the required app.
Then execute the jar created above  by running `java -jar target/swipe-1.0-SNAPSHOT.jar 10000 bumble.txt` 1000 being the number of different profiles you want to swipe right.
