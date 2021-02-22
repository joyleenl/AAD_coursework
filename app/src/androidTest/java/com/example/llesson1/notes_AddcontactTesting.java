package com.example.llesson1;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class notes_AddcontactTesting<ActivityTestRule> {

    @Rule
    public ActivityTestRule mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.CAMERA");

    @Test
    public void notes_AddcontactTesting() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.Magnify), withText("Magnify"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton.perform(click());

        pressBack();

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.ContactList), withText("Add Contact List"), withContentDescription("Add Contact List"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.phone),
                        childAtPosition(
                                allOf(withId(R.id.upperlayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.phone),
                        childAtPosition(
                                allOf(withId(R.id.upperlayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("082232135542"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.addPN), withText("Add"),
                        childAtPosition(
                                allOf(withId(R.id.layoutButton),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.phone),
                        childAtPosition(
                                allOf(withId(R.id.upperlayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("082232135542"), closeSoftKeyboard());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.deletePN), withText("Delete"),
                        childAtPosition(
                                allOf(withId(R.id.layoutButton),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        materialButton4.perform(click());

        pressBack();

        pressBack();

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.Howto), withText("Instructions"), withContentDescription("Helping Hands"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton5.perform(click());

        pressBack();

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.noteButton), withText("Notes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.imageAddNote), withContentDescription("Helping Hands"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.inputTitle),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        appCompatEditText4.perform(scrollTo(), replaceText("Testib"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.inputNote),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                6)));
        appCompatEditText5.perform(scrollTo(), replaceText("q212dqh"), closeSoftKeyboard());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.textMisc), withText("Miscellaneous"),
                        childAtPosition(
                                allOf(withId(R.id.layoutMisc),
                                        childAtPosition(
                                                withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction materialTextView2 = onView(
                allOf(withId(R.id.textMisc), withText("Miscellaneous"),
                        childAtPosition(
                                allOf(withId(R.id.layoutMisc),
                                        childAtPosition(
                                                withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        materialTextView2.perform(click());

        ViewInteraction view = onView(
                allOf(withId(R.id.viewColor4),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutNoteColor),
                                        3),
                                0),
                        isDisplayed()));
        view.perform(click());

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.imageSave), withContentDescription("Notes"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        appCompatImageView2.perform(scrollTo(), click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.notesRecylerView),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                3)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.inputSubtitle),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                4)));
        appCompatEditText6.perform(scrollTo(), replaceText("123"), closeSoftKeyboard());

        ViewInteraction appCompatImageView3 = onView(
                allOf(withId(R.id.imageSave), withContentDescription("Notes"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        appCompatImageView3.perform(scrollTo(), click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.notesRecylerView),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                3)));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction materialTextView3 = onView(
                allOf(withId(R.id.textMisc), withText("Miscellaneous"),
                        childAtPosition(
                                allOf(withId(R.id.layoutMisc),
                                        childAtPosition(
                                                withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        materialTextView3.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.layoutDeleteNote),
                        childAtPosition(
                                allOf(withId(R.id.layoutMisc),
                                        childAtPosition(
                                                withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                                1)),
                                2),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction materialTextView4 = onView(
                allOf(withId(R.id.textDeleteNote), withText("Delete Note"),
                        childAtPosition(
                                allOf(withId(R.id.layoutDeleteContainer),
                                        childAtPosition(
                                                withId(R.id.custom),
                                                0)),
                                3),
                        isDisplayed()));
        materialTextView4.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
