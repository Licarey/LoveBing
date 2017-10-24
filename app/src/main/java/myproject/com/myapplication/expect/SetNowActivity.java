package myproject.com.myapplication.expect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.florent37.expectanim.ExpectAnim;

import butterknife.BindView;
import butterknife.ButterKnife;
import myproject.com.myapplication.R;

import static com.github.florent37.expectanim.core.Expectations.invisible;


public class SetNowActivity extends AppCompatActivity {

    @BindView(R.id.follow)
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_now);
        ButterKnife.bind(this);

        new ExpectAnim()
                .expect(view)
                .toBe(
                        invisible()
                )
                .toAnimation()
                .setNow();

    }

}
