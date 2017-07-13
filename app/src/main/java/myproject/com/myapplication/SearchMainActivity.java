package myproject.com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by apple on 17-7-13.
 */

public class SearchMainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        final RelativeLayout tvSearchRlt = (RelativeLayout) findViewById(R.id.tv_search_rlt);
        tvSearchRlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchMainActivity.this,SearchActivity.class);
                int location[] = new int[2];
                tvSearchRlt.getLocationOnScreen(location);
                intent.putExtra("x",location[0]);
                intent.putExtra("y",location[1]);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
    }
}
