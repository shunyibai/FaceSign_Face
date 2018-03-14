package humanface.pwc.com.facesign_face.bean;

/**
 * Created by Shunyi Bai on 14/03/2018.
 */

public class WordBaidu {

    /**
     * location : {"width":1385,"top":306,"height":147,"left":2557}
     * words : 普华永道中天会计师事务所
     */

    private LocationBean location;
    private String words;

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public static class LocationBean {
        /**
         * width : 1385
         * top : 306
         * height : 147
         * left : 2557
         */

        private int width;
        private int top;
        private int height;
        private int left;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }
    }
}
