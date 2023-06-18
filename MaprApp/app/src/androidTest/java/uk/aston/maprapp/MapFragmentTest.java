package uk.aston.maprapp;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MapFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mapFragmentTest() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.textView5), withText("Map"),
                        withParent(withParent(withId(R.id.nav_host_fragment_activity_main))),
                        isDisplayed()));
        textView.check(matches(withText("Map")));

        ViewInteraction frameLayout = onView(
                allOf(withId(R.id.google_map),
                        withParent(withParent(withId(R.id.nav_host_fragment_activity_main))),
                        isDisplayed()));
        frameLayout.check(matches(isDisplayed()));

        ViewInteraction frameLayout2 = onView(
                allOf(withId(R.id.nav_view),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        frameLayout2.check(matches(isDisplayed()));

        ViewInteraction frameLayout3 = onView(
                allOf(withId(R.id.navigation_settings), withContentDescription("Settings"),
                        withParent(withParent(withId(R.id.nav_view))),
                        isDisplayed()));
        frameLayout3.check(matches(isDisplayed()));

        ViewInteraction frameLayout4 = onView(
                allOf(withId(R.id.navigation_map), withContentDescription("Map"),
                        withParent(withParent(withId(R.id.nav_view))),
                        isDisplayed()));
        frameLayout4.check(matches(isDisplayed()));

        ViewInteraction frameLayout5 = onView(
                allOf(withId(R.id.navigation_locations), withContentDescription("Locations"),
                        withParent(withParent(withId(R.id.nav_view))),
                        isDisplayed()));
        frameLayout5.check(matches(isDisplayed()));
    }
}
