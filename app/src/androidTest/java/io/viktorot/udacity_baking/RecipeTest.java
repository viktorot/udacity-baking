package io.viktorot.udacity_baking;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import io.reactivex.Single;
import io.viktorot.udacity_baking.data.Recipe;
import io.viktorot.udacity_baking.ui.main.MainActivity;
import io.viktorot.udacity_baking.utils.RecyclerViewItemCountAssertion;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class RecipeTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @Mock
    public Repo mockRepo;

    private Recipe mockRecipe;

    private BakingApplication app;

    @Before
    public void setup () {
        app = (BakingApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();

        mockRecipe = new Recipe();
        mockRecipe.id = 0;
        mockRecipe.steps = Collections.emptyList();
        mockRecipe.ingredients = Collections.emptyList();
        mockRecipe.name = "Test title";
    }

    @Test
    public void testEmptyListRendering() {
        Mockito.when(mockRepo.getRecipes()).thenReturn(Single.just(Collections.emptyList()));

        app.setRepo(mockRepo);

        activityTestRule.launchActivity(new Intent());

        Espresso.onView(ViewMatchers.withId(R.id.recycler))
                .check(new RecyclerViewItemCountAssertion(0));
    }

    @Test
    public void testRenderRecipe() {
        List<Recipe> recipes = Collections.singletonList(mockRecipe);

        Mockito.when(mockRepo.getRecipes()).thenReturn(Single.just(recipes));

        app.setRepo(mockRepo);

        activityTestRule.launchActivity(new Intent());

        Espresso.onView(ViewMatchers.withId(R.id.recycler))
                .check(new RecyclerViewItemCountAssertion(1));
    }

    @Test
    public void testRenderRecipeTitle() {
        List<Recipe> recipes = Collections.singletonList(mockRecipe);

        Mockito.when(mockRepo.getRecipes()).thenReturn(Single.just(recipes));

        app.setRepo(mockRepo);

        activityTestRule.launchActivity(new Intent());

        onView(withId(R.id.recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));

        onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(is(mockRecipe.name))));
    }






    private static Matcher<Object> withToolbarTitle(final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }

            @Override public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }
        };
    }
}
