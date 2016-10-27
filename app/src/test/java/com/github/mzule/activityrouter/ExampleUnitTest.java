package com.github.mzule.activityrouter;

import android.net.Uri;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testString(){
//        {
//            String a = "test?112";
//            String[] list = a.split("\\?");
//            String b = list[0];
//        }
//
//        {
//            String a = "test";
//            String[] list = a.split("\\?");
//            String b = list[0];
//        }

        Uri uri = Uri.parse("router://user/collection?id=911/home/211");
        String path = uri.getPath();
        String scheme = uri.getScheme();
        List<String> ab = uri.getPathSegments();
        String a = "";
    }
}