package com.example.product_inventory_application;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.product_inventory_application.Draw.current_brush;
import static com.example.product_inventory_application.Draw.staticcanvas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class paint extends AppCompatActivity {
    public static Path path = new Path ();
    public static Paint paintbrush = new Paint ();
    Button btn_green, btn_red, btn_blue, btn_done;
    View layout;
    TextView filename,dirname;
    ImageView imgview;
    FileOutputStream outputstream;
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.paint);
        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("img");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        btn_green = findViewById (R.id.button_green);
        btn_blue = findViewById (R.id.button_blue);
        btn_red = findViewById (R.id.button_red);
        btn_done = findViewById (R.id.btn_backtoproductdescriptionfrompaint);
        imgview = findViewById (R.id.imageView_paint);
        layout = findViewById (R.id.displaydisplay);
        filename = findViewById (R.id.edttxt_filename);
        dirname = findViewById (R.id.edttxt_dirname);
        layout.setBackground (new BitmapDrawable (getResources(), bmp));
       // imgview.setImageResource (R.drawable.car1);
        Bitmap imgbitmap = (( BitmapDrawable) imgview.getDrawable ()).getBitmap ();

        btn_done.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                //Drawable img  = layout.getBackground ();
                Bitmap bt = Bitmap.createBitmap (layout.getWidth (),layout.getHeight (),Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas (bt);
                layout.draw (canvas);
                imgview.setImageBitmap (bt);


                //Bitmap bitmap = ((BitmapDrawable)imgview.getDrawable ()).getBitmap();
                //ByteArrayOutputStream stream1 = new ByteArrayOutputStream();

                //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
                //byte[] bitmapdata = stream1.toByteArray();

                Uri myUri = Uri.parse (""+bt);
                //Uri savedImageURI = Uri.parse(file.getAbsolutePath());


              // imgview.setImageURI(savedImageURI);
              // dirname.setText("Image saved in internal storage.\n" + savedImageURI);

                //createFile (myUri);
                //openDirectory (myUri);
                /*Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                File photo = null;
                try {
                    photo = createphotofile ()
                } catch (IOException e) {
                    e.printStackTrace ();
                }

                if(photo!=null)
                    Uri photos  -FileProvider.getUriForFile (this,
                    "com.example.android.fileprovider",
                    photo);*/


                File file = Environment.getExternalStorageDirectory ();
                File dir = new File (file.getAbsolutePath () +"/painted pictures/");
                dir.mkdir ();
                String filenmame = String.format ("%d.png", System.currentTimeMillis ());
                File output = new File (dir,filenmame);
                try {
                    outputstream = new FileOutputStream (output);
                }catch (Exception e){
                    e.printStackTrace ();
                }
                //bitmap = ((BitmapDrawable)imgview.getDrawable ()).getBitmap();
                imgbitmap.compress (Bitmap.CompressFormat.JPEG,100,outputstream);
                try {
                    outputstream.flush ();
                }catch (Exception e){
                    e.printStackTrace ();
                }
                try {
                    outputstream.close ();
                }catch (Exception e){
                    e.printStackTrace ();
                }
                Intent intent = new Intent (paint.this,activity_productdescription.class);
               // intent.putExtra ("imagefrompaint", bitmapdata);
                intent.putExtra ("imageexists",getIntent ().getIntExtra ("id",0));
                //startActivity (intent);
            }
        });



        btn_green.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                greenColor (v);
            }
        });

        btn_blue.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                blueColor (v);
            }
        });

        btn_red.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                redColor (v);
            }
        });
    }



    private static final int CREATE_FILE = 1;
    private void createFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "newvalue");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, CREATE_FILE);
    }

    public void openDirectory(Uri uriToLoad) {
        // Choose a directory using the system's file picker.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when it loads.
        byte a = 12;
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriToLoad);

        startActivityForResult(intent, 100);
    }

    String currentphotopath;
    private File createphotofile() throws IOException{
        String timesstamp = new SimpleDateFormat ("yyyyMMdd_HHmmss").format (new Date ());
        String itemfilename = "JPEG_"+ timesstamp+"_";
        File stroragedir = getExternalFilesDir (Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile (
                itemfilename, "jpg",stroragedir
        );

        currentphotopath = image.getAbsolutePath ();
        return image;
    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public void currentColor(int c){
        current_brush = c;
        path = new Path();
    }

    public void greenColor(View view){
        paintbrush.setColor(Color.GREEN);
        currentColor(paintbrush.getColor());
    }
    public void blueColor(View view){
        paintbrush.setColor(Color.BLUE);
        currentColor(paintbrush.getColor());
    }
    public void redColor (View view){
        paintbrush.setColor(Color.RED);
        currentColor(paintbrush.getColor());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult (requestCode, resultCode, resultData);
        if (requestCode == 100
                && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData ();
                // Perform operations on the document using its URI.
            }
        }
    }

}
