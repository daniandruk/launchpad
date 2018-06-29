package org.daniandruk.launchpad.rest;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.daniandruk.launchpad.image.ImageUtils;
import org.daniandruk.launchpad.model.Item;
import org.daniandruk.launchpad.service.RedditService;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EndpointTest {

    @InjectMocks
    private Endpoint endpoint;

    @Mock
    private RedditService redditService;

    @Mock
    private ImageUtils imageUtils;

    @Test
    public void testSearch() {
        List<Item> expResult = new ArrayList<>();
        when(redditService.getItems(Matchers.anyString(), Matchers.anyInt())).thenReturn(expResult);
        List<Item> result = endpoint.search("something");
        assertTrue(expResult.equals(result));
    }

    @Test
    public void testRgb() {
        List<Item> expResult = new ArrayList<>();
        when(redditService.getItems(Matchers.anyString(), Matchers.anyInt())).thenReturn(expResult);
        when(imageUtils.sortByDistance(Matchers.any(), Matchers.any(Color.class), Matchers.anyInt())).thenReturn(expResult);
        List<Item> result = endpoint.rgb("something", 60, 60, 60);
        assertTrue(expResult.equals(result));
    }

}
