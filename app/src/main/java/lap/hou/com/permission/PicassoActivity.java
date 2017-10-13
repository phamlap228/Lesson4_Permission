package lap.hou.com.permission;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class PicassoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picasso);
        ImageView mImg_load = (ImageView) findViewById(R.id.img_load);
        String mUrl = "http://file.vforum.vn/hinh/2014/6/hinh-anh-buon-2.jpg";
        Glide.with(this)
                .load(mUrl)
                .centerCrop()
                .placeholder(R.drawable.load)
                .into(mImg_load);

    }
}
