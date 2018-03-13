package humanface.pwc.com.facesign_face.bean;

/**
 * Created by Shunyi Bai on 19/07/2017.
 */

public class Face {

    /**
     * face_rectangle : {"width":279,"top":235,"left":65,"height":279}
     * face_token : f66a68a7013bd192371a3014a3315ec7
     */

    private FaceRectangleBean face_rectangle;
    private String face_token;

    public FaceRectangleBean getFace_rectangle() {
        return face_rectangle;
    }

    public void setFace_rectangle(FaceRectangleBean face_rectangle) {
        this.face_rectangle = face_rectangle;
    }

    public String getFace_token() {
        return face_token;
    }

    public void setFace_token(String face_token) {
        this.face_token = face_token;
    }

    @Override
    public String toString() {
        return "Face{" +
                "face_rectangle=" + face_rectangle +
                ", face_token='" + face_token + '\'' +
                '}';
    }

    public static class FaceRectangleBean {
        /**
         * width : 279
         * top : 235
         * left : 65
         * height : 279
         */

        private int width;
        private int top;
        private int left;
        private int height;

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

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public String toString() {
            return "FaceRectangleBean{" +
                    "width=" + width +
                    ", top=" + top +
                    ", left=" + left +
                    ", height=" + height +
                    '}';
        }
    }
}
