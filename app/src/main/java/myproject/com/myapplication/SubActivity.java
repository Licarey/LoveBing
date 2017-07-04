package myproject.com.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * Created by apple on 17-7-3.
 */

public class SubActivity extends Activity {
    private TextView text_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        text_title = (TextView) findViewById(R.id.text_title);
    }
}
