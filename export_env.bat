@echo off
setlocal enabledelayedexpansion

REM Define the path to your .env file
set ENV_FILE=keys.env

REM Add or remove other variables as needed
set KEYS=RAPIDAPI_KEY GNEWS_KEY

REM Clear existing .env file
del "%ENV_FILE%" 2>nul

REM Loop through each key, retrieve value from environment, and append to .env file
for %%A in (%KEYS%) do (
    echo %%A=!%%A!>>"%ENV_FILE%"
)

echo Keys exported to %ENV_FILE%