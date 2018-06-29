package org.daniandruk.launchpad.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.daniandruk.launchpad.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ImageUtils {

    private final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    private Color averageColor(String path) throws IOException {
        URL url = new URL(path);
        BufferedImage bufferedImage = ImageIO.read(url);
        long width = bufferedImage.getWidth();
        long height = bufferedImage.getHeight();
        long size = width * height;
        long reds = 0, greens = 0, blues = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color pixel = new Color(bufferedImage.getRGB(i, j));
                reds += pixel.getRed();
                greens += pixel.getGreen();
                blues += pixel.getBlue();
            }
        }
        int everageRed = (int) (reds / size);
        int everageGreen = (int) (greens / size);
        int everageBlue = (int) (blues / size);
        return new Color(everageRed, everageGreen, everageBlue);
    }

    private double distance(Color c1, Color c2) throws IOException {
        return Math.abs(c1.getRed() - c2.getRed()) + Math.abs(c1.getGreen() - c2.getGreen()) + Math.abs(c1.getBlue() - c2.getBlue());
    }

    private Map<Item, Double> getDistance(List<Item> items, Color color) {
        Map<Item, Double> map = new HashMap<>();
        items.forEach((item) -> {
            try {
                Color itemEverageColor = averageColor(item.getThumbnail());
                map.put(item, distance(color, itemEverageColor));
            } catch (IOException ex) {
                map.put(item, Double.MAX_VALUE);
                logger.error(ex.getMessage());
            }
        });
        return map;
    }

    public List<Item> sortByDistance(List<Item> items, Color color, int limit) {
        Map<Item, Double> map = getDistance(items, color);
        List<Item> result = map.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());
        return result;
    }

}
