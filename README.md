# GPS Tag in Images 
--------------
When even you click image from phone's camera then your phone stores some information too with image.
So this project is all about how to get those information specially <b>Location<b>.

 Please have a look on getImageDetail() method in the project.
 For more detail please look in to class ExifInterface. Using this class all these things are posible.
 link https://developer.android.com/reference/android/media/ExifInterface.html
 
 Note :- <font color="red"> GPS related info can only be accessible when you GPS and internet connection is active.</font>
 
 ```java
    ExifInterface exifInterface = new ExifInterface(file); // file path of image 
    String imageLength = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
    String tagwWidth = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
    String tagDateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
    String tagMake = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
    String tagModel = exifInterface.getAttribute(ExifInterface.TAG_MODEL);
    String tagOperation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
    String tagWhiteBalance = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
    String tagFocalLength = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
    String tagFlash = exifInterface.getAttribute(ExifInterface.TAG_FLASH);

    String tagGpsTimeStamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
    String tagGpsLatitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
    String tagGpsLatituteRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
    String tagGpsLongitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
    String tagGpsLongitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
    String tagGpsProcessingMethod = exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);
 ```

![screenshot_2017-01-31-22-28-50](https://cloud.githubusercontent.com/assets/7554816/22475924/a1a43dfa-e806-11e6-8211-30a8c3ce9305.png)
![screenshot_2017-01-31-22-30-15](https://cloud.githubusercontent.com/assets/7554816/22475925/a1aba1b2-e806-11e6-8367-c69d37180441.png)

 
 
