package lap.hou.com.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText mEditTextUrl;
    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();
        mEditTextUrl = (EditText) findViewById(R.id.edt_url);
        Button buttonDownload = (Button) findViewById(R.id.btn_download);
        mImageView = (ImageView) findViewById(R.id.img_show);
        Button buttonPicasso = (Button) findViewById(R.id.button_picasso);

        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImageLoadTask(MainActivity.this, mEditTextUrl.getText().toString(), mImageView).execute();
            }
        });
        buttonPicasso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PicassoActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permision Write File is Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Permision Write File is Denied", Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(MainActivity.this, "Permission isn't granted ", Toast.LENGTH_SHORT).show();
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    Toast.makeText(MainActivity.this, "Permisson don't granted and dont show dialog again ", Toast.LENGTH_SHORT).show();
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        }
    }

    private static class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private Context context;
        private String url;
        private ImageView imgShow;

        ImageLoadTask(Context context, String url, ImageView imgShow) {
            this.url = url;
            this.context = context;
            this.imgShow = imgShow;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap myBitmap = null;

            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);
                if (myBitmap != null && context != null) {
                    MediaStore.Images.Media.insertImage(context.getContentResolver(), myBitmap, "Anpanman", "Image");
                }
                return myBitmap;
            } catch (SecurityException e) {
                e.printStackTrace();
                Log.d(getClass().getSimpleName(), "doInBackground: ");
            } catch (Exception e) {
                e.printStackTrace();

            }
            return myBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null && this.context != null) {
                Toast.makeText(this.context, "Download finish", Toast.LENGTH_SHORT).show();
                imgShow.setImageBitmap(bitmap);
            } else if (this.context != null) {
                Toast.makeText(this.context, "Download fail", Toast.LENGTH_SHORT).show();
            }
        }

    }

}

