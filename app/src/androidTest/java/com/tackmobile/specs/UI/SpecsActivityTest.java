package com.tackmobile.specs.UI;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.tackmobile.specs.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SpecsActivityTest {

    @Rule
    public ActivityTestRule<SpecsActivity> mActivityTestRule = new ActivityTestRule<>(SpecsActivity.class);

    @Test
    public void specsActivityTest() {
        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.bottomBarItemTwo),
                        withParent(allOf(withId(R.id.bb_bottom_bar_item_container),
                                withParent(withId(R.id.bb_bottom_bar_outer_container)))),
                        isDisplayed()));
        linearLayout.perform(click());
        onView(withId(R.id.orientation_text)).check(matches(withText("Portrait")));
        onView(withId(R.id.size_text)).check(matches(withText("Normal")));
        onView(withId(R.id.dpi_text)).check(matches(withText("480")));
        onView(withId(R.id.res_text)).check(matches(withText("XXHDPI")));
    }
}
