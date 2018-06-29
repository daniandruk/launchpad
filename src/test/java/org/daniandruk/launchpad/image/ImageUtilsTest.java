package org.daniandruk.launchpad.image;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.daniandruk.launchpad.model.Item;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ImageUtilsTest {

    private static final String RED_IMG = "http://www.htmlcsscolor.com/preview/gallery/FF0000.png";
    private static final String YELLOW_IMG = "http://www.htmlcsscolor.com/preview/gallery/FFFF00.png";
    private static final String INVALID_ULR_IMG = "NOPE";
    private static final String GRADIENT_IMG = "http://www.htmlcsscolor.com/gradient-linear/85CF44/to/21EEC6.png"; //Everage Color = 83, 222, 132

    private List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setThumbnail(RED_IMG);
        items.add(item);
        item = new Item();
        item.setThumbnail(YELLOW_IMG);
        items.add(item);
        item = new Item();
        item.setThumbnail(INVALID_ULR_IMG);
        items.add(item);
        item = new Item();
        item.setThumbnail(GRADIENT_IMG);
        items.add(item);
        return items;
    }

    @Test
    public void testSortByDistance() {
        ImageUtils imageUtils = new ImageUtils();

        List<Item> result = imageUtils.sortByDistance(getItems(), Color.yellow, 4);
        assertTrue(result.size() == 4);
        assertTrue(result.get(0).getThumbnail().equals(YELLOW_IMG));
        assertTrue(result.get(1).getThumbnail().equals(RED_IMG));
        assertTrue(result.get(2).getThumbnail().equals(GRADIENT_IMG));
        assertTrue(result.get(3).getThumbnail().equals(INVALID_ULR_IMG));

        result = imageUtils.sortByDistance(getItems(), Color.red, 5);
        assertTrue(result.size() == 4);
        assertTrue(result.get(0).getThumbnail().equals(RED_IMG));
        assertTrue(result.get(1).getThumbnail().equals(YELLOW_IMG));
        assertTrue(result.get(2).getThumbnail().equals(GRADIENT_IMG));
        assertTrue(result.get(3).getThumbnail().equals(INVALID_ULR_IMG));

        result = imageUtils.sortByDistance(getItems(), Color.green, 3);
        assertTrue(result.size() == 3);
        assertTrue(result.get(0).getThumbnail().equals(GRADIENT_IMG));
        assertTrue(result.get(1).getThumbnail().equals(YELLOW_IMG));
        assertTrue(result.get(2).getThumbnail().equals(RED_IMG));

    }

}
