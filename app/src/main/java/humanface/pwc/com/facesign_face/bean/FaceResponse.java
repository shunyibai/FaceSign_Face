package humanface.pwc.com.facesign_face.bean;

/**
 * Created by Shunyi Bai on 19/07/2017.
 */

public class FaceResponse {

    /**
     * image_id : gdrSPvYCMs2PZ0Rf+YryrQ==
     * request_id : 1500452108,66de52c8-28ad-493b-aa3f-7c59a30e0fad
     * time_used : 182
     * faces : dfsf
     */

    private String image_id;
    private String request_id;
    private int time_used;
    private String faces;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public String getFaces() {
        return faces;
    }

    public void setFaces(String faces) {
        this.faces = faces;
    }

    @Override
    public String toString() {
        return "FaceResponse{" +
                "image_id='" + image_id + '\'' +
                ", request_id='" + request_id + '\'' +
                ", time_used=" + time_used +
                ", faces='" + faces + '\'' +
                '}';
    }
}
