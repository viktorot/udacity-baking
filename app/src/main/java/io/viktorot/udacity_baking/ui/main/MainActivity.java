package io.viktorot.udacity_baking.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;
import io.viktorot.udacity_baking.Navigator;
import io.viktorot.udacity_baking.R;

public class MainActivity extends AppCompatActivity implements Navigator.Provider {

    public static Navigator getNavigator(Activity activity) {
        return ((Navigator.Provider) activity).getNavigator();
    }

    @BindView(R.id.main_holder)
    View mainHolder;

    @BindView(R.id.detail_holder)
    @Nullable
    View detailsHolder;

    private Navigator navigator = new Navigator() {
        @Override
        public void close() {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        navigator.init(getSupportFragmentManager(), mainHolder, detailsHolder);

        navigator.navigateToRecipeList();
    }

    @Override
    protected void onStop() {
        navigator.clear();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        navigator.back();
    }

    @Override
    public Navigator getNavigator() {
        return navigator;
    }
}
