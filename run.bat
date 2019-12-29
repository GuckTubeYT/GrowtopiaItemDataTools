@echo off
echo "   _____ _                 _____        _     _______          _        "
echo "  |_   _| |               |  __ \      | |   |__   __|        | |       "
echo "    | | | |_ ___ _ __ ___ | |  | | __ _| |_ __ _| | ___   ___ | |___    "
echo "    | | | __/ _ \ '_ ` _ \| |  | |/ _` | __/ _` | |/ _ \ / _ \| / __|   "
echo "   _| |_| ||  __/ | | | | | |__| | (_| | || (_| | | (_) | (_) | \__ \   "
echo "  |_____|\__\___|_| |_| |_|_____/ \__,_|\__\__,_|_|\___/ \___/|_|___/   "
echo "                                                                        "
echo "                                                                        "

if not exist "loaded" mkdir loaded
for /f %%A in ('dir loaded ^| find "File(s)"') do set cnt=%%A

If %cnt% == 0 (java -jar target\GrowtopiaItemDataTools-1.0.jar true) else (java -jar target\GrowtopiaItemDataTools-1.0.jar false)