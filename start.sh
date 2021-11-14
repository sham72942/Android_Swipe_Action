#/bin/bash
screen appium --no-reset
screen emulator @$(emulator -list-avds | sed -n 1p)
