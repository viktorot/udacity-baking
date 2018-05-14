package io.viktorot.udacity_baking.ui.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.viktorot.udacity_baking.R;
import io.viktorot.udacity_baking.data.Step;
import io.viktorot.udacity_baking.ui.main.MainActivity;

public class StepDetailsFragment extends Fragment {

    public static final String TAG = "StepDetailsFragment";

    private static final String ARG_STEP = "arg_step";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static StepDetailsFragment newInstance(@NonNull Step step) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_STEP, step);

        StepDetailsFragment fragment = new StepDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException("arguments must be set");
        }

        Step step = args.getParcelable(ARG_STEP);
        if (step == null) {
            throw new IllegalArgumentException("step cannot be null");
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);

        ButterKnife.bind(this, view);

        toolbar.setTitle("[Step Details]");
        toolbar.setNavigationOnClickListener(view1 -> onBackPressed());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_vector);

        return view;
    }

    private void onBackPressed() {
        MainActivity.getNavigator(requireActivity()).back();
    }
}
