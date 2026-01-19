package com.fungsitama.dhsshopee.activity.dhs.Unloading;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fungsitama.dhsshopee.R;
import com.fungsitama.dhsshopee.activity.dhs.Loading.DaftarGambarBarcodeManualActivity;
import com.fungsitama.dhsshopee.activity.dhs.Loading.ScanBarcodeLoadingActivity;
import com.fungsitama.dhsshopee.activity.dhs.Loading.TambahBarcodeManualActivity;
import com.fungsitama.dhsshopee.util.ApiConfig;
import com.fungsitama.dhsshopee.util.SessionManager;
import com.fungsitama.dhsshopee.util.VolleyMultipartRequest;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TambahBarcodeManualUnloadingActivity extends AppCompatActivity {

    SessionManager manager;
    private static String vtoken,vusername,vtypeLogin,vtypeUser;

    private String id,idGambar,noTrans,barcode;

    private EditText etBarcodeManual;
    private Button btnSimpan;

    private LinearLayout ly_textWarning,ly_textWarning02;
    private TextView text_Warning;

    private CardView cv_chekBox,cv_foto;
    private TextView txt_chekBox;
    private Switch checkbox_Switch;
    public Boolean scan;

    private static final int REQUEST_PERMISSIONS = 100;
    static final int PICK_IMAGE_REQUEST = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    private static final int CAMERA_PIC_REQUEST = 1337;
    private Bitmap bitmap;
    private Bitmap scaledBitmap,photo;
    Bitmap rotatedBMP;
    String filePath;
    PhotoView selectedImage;
    Button cameraBtn,galleryBtn;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barcode_manual);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        idGambar = intent.getStringExtra("idGambar");
        noTrans = intent.getStringExtra("noTrans");
        barcode = intent.getStringExtra("barcode");

        Log.d("TAG", "id: " + id);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Bongkar Muat Barang Manual "+ noTrans);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new SessionManager();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vtoken = defaultSharedPreferences.getString("token", null);
        vusername = defaultSharedPreferences.getString("username", null);
        vtypeLogin = defaultSharedPreferences.getString("typeLogin", null);
        vtypeUser = defaultSharedPreferences.getString("typeUser", null);

        cv_chekBox = findViewById(R.id.cv_chekBox);
        txt_chekBox = findViewById(R.id.txt_chekBox);
        checkbox_Switch = findViewById(R.id.checkbox_Switch);
        cv_foto = findViewById(R.id.cv_foto);
        ly_textWarning02 = findViewById(R.id.ly_textWarning02);

        text_Warning = findViewById(R.id.text_Warning);
        text_Warning.setText("Barcode manual setelah disimpan maka akan di tandai wrana hijau");

        etBarcodeManual = findViewById(R.id.et_BarcodeManual);
        btnSimpan = findViewById(R.id.btnSimpan);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (barcode.equals("")){

                    String manualBarcode = etBarcodeManual.getText().toString();

                    if (manualBarcode.isEmpty()){
                        new SweetAlertDialog(TambahBarcodeManualUnloadingActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                                .setContentText("Barcode manual masih kosong !")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                    }
                                }).show();
                    }else{
                        //Toast.makeText(TambahBarcodeManualActivity.this, "Barcode udah ada", Toast.LENGTH_SHORT).show();
                        getBarcodeLoading(id, etBarcodeManual.getText().toString());
                    }

                }else{
                    //Toast.makeText(TambahBarcodeManualActivity.this, "Barcode Ada", Toast.LENGTH_SHORT).show();
                    //showCamera();
                }
            }
        });

        selectedImage = findViewById(R.id.displayImageView);
        cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.galleryBtn);


        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)&& (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(TambahBarcodeManualUnloadingActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(TambahBarcodeManualUnloadingActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))&& (ActivityCompat.shouldShowRequestPermissionRationale(TambahBarcodeManualUnloadingActivity.this,
                            Manifest.permission.CAMERA))) {

                    } else {
                        ActivityCompat.requestPermissions(TambahBarcodeManualUnloadingActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    Log.e("Else", "Else");

                    if (barcode.equals("")){

                        String manualBarcode = etBarcodeManual.getText().toString();

                        if (manualBarcode.isEmpty()){
                            new SweetAlertDialog(TambahBarcodeManualUnloadingActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                                    .setContentText("Barcode manual masih kosong !")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.cancel();
                                        }
                                    }).show();
                        }else{
                            //Toast.makeText(TambahBarcodeManualActivity.this, "Barcode udah ada", Toast.LENGTH_SHORT).show();
                            getBarcodeLoading(id, etBarcodeManual.getText().toString());
                            showCamera();

                        }

                    }else{
                        //Toast.makeText(TambahBarcodeManualActivity.this, "Barcode Ada", Toast.LENGTH_SHORT).show();
                        showCamera();
                    }


                }
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(TambahBarcodeManualUnloadingActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(TambahBarcodeManualUnloadingActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {

                    } else {
                        ActivityCompat.requestPermissions(TambahBarcodeManualUnloadingActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    Log.e("Else", "Else");

                    if (barcode.equals("")){

                        String manualBarcode = etBarcodeManual.getText().toString();

                        if (manualBarcode.isEmpty()){
                            new SweetAlertDialog(TambahBarcodeManualUnloadingActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                                    .setContentText("Barcode manual masih kosong !")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.cancel();
                                        }
                                    }).show();
                        }else{
                            //Toast.makeText(TambahBarcodeManualActivity.this, "Barcode udah ada", Toast.LENGTH_SHORT).show();
                            getBarcodeLoading(id, etBarcodeManual.getText().toString());
                            showFileChooser();
                        }

                    }else{
                        //Toast.makeText(TambahBarcodeManualActivity.this, "Barcode Ada", Toast.LENGTH_SHORT).show();
                        getBarcodeLoading(id, etBarcodeManual.getText().toString());
                        showFileChooser();
                    }

                }
            }
        });

        scan = Boolean.valueOf("false");
        txt_chekBox.setText("Tidak");
        cv_foto.setVisibility(View.GONE);
        ly_textWarning02.setVisibility(View.GONE);
        btnSimpan.setVisibility(View.VISIBLE);
        cameraBtn.setVisibility(View.GONE);
        galleryBtn.setVisibility(View.GONE);

        checkbox_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    scan = Boolean.valueOf("true");
                    txt_chekBox.setText("Iya");
                    cv_foto.setVisibility(View.VISIBLE);
                    ly_textWarning02.setVisibility(View.VISIBLE);
                    btnSimpan.setVisibility(View.GONE);
                    cameraBtn.setVisibility(View.VISIBLE);
                    galleryBtn.setVisibility(View.VISIBLE);

                } else {
                    scan = Boolean.valueOf("false");
                    txt_chekBox.setText("Tidak");
                    cv_foto.setVisibility(View.GONE);
                    ly_textWarning02.setVisibility(View.GONE);
                    btnSimpan.setVisibility(View.VISIBLE);
                    cameraBtn.setVisibility(View.GONE);
                    galleryBtn.setVisibility(View.GONE);
                }
            }
        });


    }

    private void getBarcodeLoading(String xId, String xManualBarcode) {

        RequestQueue newRequestQueue = Volley.newRequestQueue(this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("trCargoId", xId);
            jSONObject.put("qrcode", xManualBarcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String jSONObject2 = jSONObject.toString();

        Log.d("TAG", "getNomorDObyScan: " + jSONObject2);

        StringBuilder sb = new StringBuilder();
        sb.append(ApiConfig.UpdateUnloading);
        JsonObjectRequest r3 = new JsonObjectRequest(1, sb.toString(), jSONObject, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject jSONObject) {
                try {
                    String msg = jSONObject.getString("message");
                    if (scan.equals(false)){
                        new SweetAlertDialog(TambahBarcodeManualUnloadingActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Informasi")
                                .setContentText(msg)
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        if (barcode.equals("")){
                                            Intent intent = new Intent(TambahBarcodeManualUnloadingActivity.this, ScanBarcodeLoadingActivity.class);

                                            intent.putExtra("id", id);
                                            intent.putExtra("noTrans", noTrans);

                                            (TambahBarcodeManualUnloadingActivity.this).finish();
                                            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            startActivity(intent);

                                        }else{
                                            Intent intent = new Intent(TambahBarcodeManualUnloadingActivity.this, DaftarGambarBarcodeManualActivity.class);

                                            intent.putExtra("id", idGambar);
                                            intent.putExtra("trCargoId", id);
                                            intent.putExtra("noTrans", noTrans);
                                            intent.putExtra("barcode", barcode);

                                            (TambahBarcodeManualUnloadingActivity.this).finish();
                                            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            startActivity(intent);
                                        }
                                        sweetAlertDialog.cancel();
                                    }
                                }).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
                String responseBody = null;
                try {
                    responseBody = new String(volleyError.networkResponse.data, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JSONObject data = null;
                try {
                    data = new JSONObject(responseBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String msg = data.optString("message");
                new SweetAlertDialog(TambahBarcodeManualUnloadingActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                        .setContentText(msg)
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        }).show();

            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }

        };
        newRequestQueue.add(r3);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void showCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            if (filePath != null) {
                try {


                    Log.d("filePath", String.valueOf(filePath));
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                   /* uploadBitmap(bitmap);
                    imageView.setImageBitmap(bitmap);*/

                    /*int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);*/

                    BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
                    options.inJustDecodeBounds = true;
                    Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

                    int actualHeight = options.outHeight;
                    int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

                    float maxHeight = 816.0f;
                    float maxWidth = 612.0f;
                    float imgRatio = actualWidth / actualHeight;
                    float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

                    if (actualHeight > maxHeight || actualWidth > maxWidth) {
                        if (imgRatio < maxRatio) {
                            imgRatio = maxHeight / actualHeight;
                            actualWidth = (int) (imgRatio * actualWidth);
                            actualHeight = (int) maxHeight;
                        } else if (imgRatio > maxRatio) {
                            imgRatio = maxWidth / actualWidth;
                            actualHeight = (int) (imgRatio * actualHeight);
                            actualWidth = (int) maxWidth;
                        } else {
                            actualHeight = (int) maxHeight;
                            actualWidth = (int) maxWidth;

                        }
                    }

//      setting inSampleSize value allows to load a scaled down version of the original image

                    options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
                    options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
                    options.inPurgeable = true;
                    options.inInputShareable = true;
                    options.inTempStorage = new byte[16 * 1024];

                    try {
//          load the bitmap from its path
                        bmp = BitmapFactory.decodeFile(filePath, options);
                    } catch (OutOfMemoryError exception) {
                        exception.printStackTrace();

                    }
                    try {
                        scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
                    } catch (OutOfMemoryError exception) {
                        exception.printStackTrace();
                    }

                    float ratioX = actualWidth / (float) options.outWidth;
                    float ratioY = actualHeight / (float) options.outHeight;
                    float middleX = actualWidth / 2.0f;
                    float middleY = actualHeight / 2.0f;

                    Matrix scaleMatrix = new Matrix();
                    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

                    Canvas canvas = new Canvas(scaledBitmap);
                    canvas.setMatrix(scaleMatrix);
                    canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
                    ExifInterface exif;
                    try {
                        exif = new ExifInterface(filePath);

                        int orientation = exif.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION, 0);
                        Log.d("EXIF", "Exif: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                            Log.d("EXIF", "Exif: " + orientation);
                        } else if (orientation == 3) {
                            matrix.postRotate(180);
                            Log.d("EXIF", "Exif: " + orientation);
                        } else if (orientation == 8) {
                            matrix.postRotate(270);
                            Log.d("EXIF", "Exif: " + orientation);
                        }
                        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                                scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                                true);
                        uploadBitmap(scaledBitmap);
                        selectedImage.setImageBitmap(scaledBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(
                        TambahBarcodeManualUnloadingActivity.this,"no image selected",
                        Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CAMERA_PIC_REQUEST) {

            photo = (Bitmap) data.getExtras().get("data");

            uploadFoto(photo);
            selectedImage.setImageBitmap(photo);
        }
       /* if (resultCode == RESULT_OK) {

            if(requestCode == PICK_IMAGE_REQUEST){
                Uri picUri = data.getData();

                filePath = getPath(picUri);

                Log.d("picUri", picUri.toString());
                Log.d("filePath", filePath);

                imageView.setImageURI(picUri);

            }

        }*/

    }
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private void uploadFoto(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ApiConfig.UploadFile,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            Log.d("TAG", "onResponse: "+response.toString());
                            JSONObject obj = new JSONObject(new String(response.data));
                            //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                            String msg = obj.getString("message");
                            new SweetAlertDialog(TambahBarcodeManualUnloadingActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Informasi")
                                    .setContentText(msg)
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            if (barcode.equals("")){
                                                Intent intent = new Intent(TambahBarcodeManualUnloadingActivity.this, ScanBarcodeLoadingActivity.class);

                                                intent.putExtra("id", id);
                                                intent.putExtra("noTrans", noTrans);

                                                (TambahBarcodeManualUnloadingActivity.this).finish();
                                                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                startActivity(intent);

                                            }else{
                                                Intent intent = new Intent(TambahBarcodeManualUnloadingActivity.this, DaftarGambarBarcodeManualActivity.class);

                                                intent.putExtra("id", idGambar);
                                                intent.putExtra("trCargoId", id);
                                                intent.putExtra("noTrans", noTrans);
                                                intent.putExtra("barcode", barcode);

                                                (TambahBarcodeManualUnloadingActivity.this).finish();
                                                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                startActivity(intent);
                                            }
                                            sweetAlertDialog.cancel();
                                        }
                                    }).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onResponse gagal: " + e.toString());

                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
                String responseBody = null;
                try {
                    responseBody = new String(volleyError.networkResponse.data, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JSONObject data = null;
                try {
                    data = new JSONObject(responseBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String msg = data.optString("message");
                new SweetAlertDialog(TambahBarcodeManualUnloadingActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                        .setContentText(msg)
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        }).show();

            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                //  long imagename = System.currentTimeMillis();
                params.put("file", new VolleyMultipartRequest.DataPart(id + ".jpg",getFileDataFromDrawable(photo),"image/jpg"));

                Log.d("TAG", "getParams: " + params.toString());

                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("trCargoId", id);
                params.put("qrcode", etBarcodeManual.getText().toString());

                Log.d("TAG", "getParams: " + params.toString());

                return params;
            }

        };
        //I used this because it was sending the file twice to the server
        volleyMultipartRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        0,
                        -1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    private void uploadBitmap(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ApiConfig.UploadFile,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            Log.d("TAG", "onResponse: "+response.toString());
                            JSONObject obj = new JSONObject(new String(response.data));
                            //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                            String msg = obj.getString("message");
                            new SweetAlertDialog(TambahBarcodeManualUnloadingActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Informasi")
                                    .setContentText(msg)
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            if (barcode.equals("")){
                                                Intent intent = new Intent(TambahBarcodeManualUnloadingActivity.this, ScanBarcodeLoadingActivity.class);

                                                intent.putExtra("id", id);
                                                intent.putExtra("noTrans", noTrans);

                                                (TambahBarcodeManualUnloadingActivity.this).finish();
                                                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                startActivity(intent);

                                            }else{
                                                Intent intent = new Intent(TambahBarcodeManualUnloadingActivity.this, DaftarGambarBarcodeManualActivity.class);

                                                intent.putExtra("id", idGambar);
                                                intent.putExtra("trCargoId", id);
                                                intent.putExtra("noTrans", noTrans);
                                                intent.putExtra("barcode", barcode);

                                                (TambahBarcodeManualUnloadingActivity.this).finish();
                                                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                startActivity(intent);
                                            }
                                            sweetAlertDialog.cancel();
                                        }
                                    }).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },new Response.ErrorListener() {
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
                String responseBody = null;
                try {
                    responseBody = new String(volleyError.networkResponse.data, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JSONObject data = null;
                try {
                    data = new JSONObject(responseBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String msg = data.optString("message");
                new SweetAlertDialog(TambahBarcodeManualUnloadingActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Informasi")
                        .setContentText(msg)
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        }).show();

            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("Bearer ");
                sb.append(vtoken);
                hashMap.put("Authorization", sb.toString());
                return hashMap;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                //  long imagename = System.currentTimeMillis();
                params.put("file", new VolleyMultipartRequest.DataPart(id + ".jpg",getFileDataFromDrawable(scaledBitmap),"image/jpg"));

                Log.d("TAG", "getParams: " + params.toString());

                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("trCargoId", id);
                params.put("qrcode", etBarcodeManual.getText().toString());

                Log.d("TAG", "getParams: " + params.toString());

                return params;
            }

        };
        //I used this because it was sending the file twice to the server
        volleyMultipartRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        0,
                        -1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

}