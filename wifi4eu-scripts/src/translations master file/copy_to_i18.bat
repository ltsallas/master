@echo ff
DEL /Q /S ..\..\..\wifi4eu-portal\wifi4eu-portal-web\src\main\angular\src\assets\i18n\*.json
DEL /Q /S ..\..\..\wifi4eu-public-portal\wifi4eu-public-portal-web\src\main\angular\src\assets\i18n\*.json
DEL /Q /S ..\..\..\wifi4eu-scripts\src\i18n\en.json

COPY /Y .\translations\*.json ..\..\..\wifi4eu-portal\wifi4eu-portal-web\src\main\angular\src\assets\i18n
COPY /Y.\translations\*.json ..\..\..\wifi4eu-public-portal\wifi4eu-public-portal-web\src\main\angular\src\assets\i18n
COPY /Y .\translations\en.json ..\..\..\wifi4eu-scripts\src\i18n\en.json