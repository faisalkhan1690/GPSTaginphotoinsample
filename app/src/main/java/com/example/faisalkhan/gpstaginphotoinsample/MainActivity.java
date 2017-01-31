package com.example.faisalkhan.gpstaginphotoinsample;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * When even you click image from phone's camera then your phone stores some information too with image.
 * So this project is all about how to get those information.
 *
 * Please have a look on getImageDetail() method.
 *
 * For more detail please look in to class ExifInterface. Using this class all these things are possible.
 *
 * For more details :- https://developer.android.com/reference/android/media/ExifInterface.html
 *
 * Note :- GPS related info can only be accessible when you GPS and internet connection is active.
 *
 * @author Faisal Khan
 */
public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1000;
    private static final int GALLERY_REQUEST = 1001;
    private ImageView ivImage;
    private TextView tvLocation;
    private Uri mCapturedImageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        tvLocation = (TextView) findViewById(R.id.tv_location);
    }


    public void clickImage(View view) {

        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "Image_File_name1");
                    mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    startActivityForResult(intentPicture, CAMERA_REQUEST);

                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            String url = getImagePath(mCapturedImageURI);

            File imgFile = new File(url);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ivImage.setImageBitmap(myBitmap);
                getImageDetail(url);
            }else{
                Toast.makeText(this, "Something went wrong pleas try after some time.", Toast.LENGTH_SHORT).show();
            }


        } else if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ivImage.setImageBitmap(bitmap);
                getImageDetail(getRealPathFromURI(uri));
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong pleas try after some time.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Method to get real path from Uri
     * @param url url
     * @return real path
     */
    public String getImagePath(Uri url) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(url, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return url.getPath();
        }
    }

    /**
     * Method to get real path from content path
     * @param contentURI content path
     * @return real path
     */
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    /**
     * Method that fetch info from your image file path.
     * @param file file path of image
     */
    private void getImageDetail(String file) {
        try {
            ExifInterface exifInterface = new ExifInterface(file);

//            String imageLength = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
//            String tagwWidth = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
//            String tagDateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
//            String tagMake = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
//            String tagModel = exifInterface.getAttribute(ExifInterface.TAG_MODEL);
//            String tagOperation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
//            String tagWhiteBalance = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
//            String tagFocalLength = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
//            String tagFlash = exifInterface.getAttribute(ExifInterface.TAG_FLASH);

//            String tagGpsTimeStamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
            String tagGpsLatitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
//            String tagGpsLatituteRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String tagGpsLongitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
//            String tagGpsLongitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
//            String tagGpsProcessingMethod = exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try{

                List<Address> addresses = geocoder.getFromLocation(convertToDegree(tagGpsLatitude),convertToDegree(tagGpsLongitude), 1);
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);
                tvLocation.setText(cityName+" "+stateName+""+countryName);
            }catch (NumberFormatException | NullPointerException | IOException e){
                e.printStackTrace();
                tvLocation.setText("Please check you internet and gps.");
            }


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Method to convert degrees, minutes, seconds to Decimal coordinates
     * @param stringDMS string of degrees, minutes, seconds
     * @return decimal coordinate
     */
    private double convertToDegree(String stringDMS){
        double result ;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = Double.valueOf(stringD[0]);
        Double D1 = Double.valueOf(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = Double.valueOf(stringM[0]);
        Double M1 = Double.valueOf(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = Double.valueOf(stringS[0]);
        Double S1 = Double.valueOf(stringS[1]);
        Double FloatS = S0/S1;
        result = FloatD + (FloatM / 60) + (FloatS / 3600);
        return result;


    }
}
