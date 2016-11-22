package aviv.myicebreaker.facebook_check;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import aviv.myicebreaker.R;

/**
 * Created by Aviad on 13/11/2016.
 */


public class GridItemActivity extends AppCompatActivity {
    private TextView imgTitle;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);

        /**Enables the toolbar, gives it a title**/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AVIAD");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        /**from MainActivity**/
        String title = getIntent().getStringExtra("Image Title");
        String image = getIntent().getStringExtra("Image");

        /**Set textview to the "Image Title" and Imageview to the "Image" obtained from MainActivity**/
        imgTitle = (TextView) findViewById(R.id.title);
        imgView = (ImageView) findViewById(R.id.grid_item_image);
        imgTitle.setText(title);

        /**Picasso does the relevant conversions and sets the image to the imageview**/
        Glide.with(this).load(image).into(imgView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**handles clicks to the back arrow in the toolbar**/
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}