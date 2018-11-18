package com.atronandbeyond.domains;

import java.util.List;

public class ImageResults {
    public class Items {
        public class Image {
            int height;
            int width;

            public int getWidth() {
                return width;
            }

            @Override
            public String toString() {
                return "Image{" +
                        "height=" + height +
                        ", width=" + width +
                        '}';
            }
        }

        String link;
        Image image;

        public String getLink() {
            return link;
        }

        public Image getImage() {
            return image;
        }

        @Override
        public String toString() {
            return "Items{" +
                    "link='" + link + '\'' +
                    ", image=" + image +
                    '}';
        }
    }

    List<Items> items;

    public List<Items> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "ImageResults{" +
                "items=" + items +
                '}';
    }
}
