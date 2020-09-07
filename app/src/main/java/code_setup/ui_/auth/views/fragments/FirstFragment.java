package code_setup.ui_.auth.views.fragments;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.electrovese.setup.R;


public class FirstFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    private TextView pageHeading, pageDesc;
    private ImageView pageImage;

    // newInstance constructor for creating fragment with arguments
    public static FirstFragment newInstance(int page, String title) {
        FirstFragment fragmentFirst = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        Log.e("onCreate", "  " + page);


    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tutorial_fragment_first, container, false);
        pageHeading = (TextView) view.findViewById(R.id.pageHeading);
        pageDesc = (TextView) view.findViewById(R.id.pageDesc);
        pageImage = (ImageView) view.findViewById(R.id.pageImage);
//		tvLabel.setText(page + " -- " + title);


        switch (page) {
            case 0:
                pageHeading.setText(R.string.str_hpdt_taxt_booking);
                pageDesc.setText(R.string.str_first_desc);
                pageImage.setImageResource(R.mipmap.ic_launcher);
                break;
            case 1:
                pageHeading.setText(R.string.str_second_heading);
                pageDesc.setText(Html.fromHtml(getString(R.string.str_second_desc)));
                pageImage.setImageResource(R.mipmap.ic_launcher);
                break;
            case 2:
                pageHeading.setText(R.string.str_third_heading);
                pageDesc.setText(Html.fromHtml(getString(R.string.str_third_desc)));
                pageImage.setImageResource(R.mipmap.ic_launcher);
                break;
        }
        return view;
    }
}