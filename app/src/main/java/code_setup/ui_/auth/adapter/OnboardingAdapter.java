package code_setup.ui_.auth.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import code_setup.ui_.auth.views.fragments.FirstFragment;


public class OnboardingAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    public OnboardingAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return FirstFragment.newInstance(0, "Page # 1");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return FirstFragment.newInstance(1, "Page # 2");
            case 2: // Fragment # 1 - This will show SecondFragment
                return FirstFragment.newInstance(2, "Page # 3");
           /* case 3: // Fragment # 1 - This will show SecondFragment
                return FirstFragment.newInstance(3, "Page # 4");
            case 4: // Fragment # 1 - This will show SecondFragment
                return FirstFragment.newInstance(4, "Page # 5");*/

            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}
        