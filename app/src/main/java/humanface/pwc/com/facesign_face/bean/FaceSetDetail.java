package humanface.pwc.com.facesign_face.bean;

import java.util.List;

/**
 * Created by Shunyi Bai on 26/10/2017.
 */

public class FaceSetDetail {

    /**
     * faceset_token : d8d1257e3e99a37437ee6ddbbcdcf64e
     * display_name : qa_test1470367697.2
     * face_tokens : ["ad248a809408b6320485ab4de13fe6a9","3b0327c24c195a7c7937348f26b9e0eb"]
     * time_used : 22
     * tags : clouddevelop,megvii,cloudqa
     * user_data : rCimxzuUlIq4hYbMrCimxzuUlIq4hYbMrCimxzuUlIq4hYbMrCimxzuUlIq4hYbMrCimxzuUlIq4hYbMrCimxzuUlIq4hYbMrCimxzuUlIq4hYbMrCimxzuUlIq4hYbMrCimxzuUlIq4hYbM
     * face_count : 2
     * request_id : 1470367720,39be3e5e-0288-4e54-8b6c-b8a03b943e2b
     * outer_id :
     */

    private String faceset_token;
    private String display_name;
    private int time_used;
    private String tags;
    private String user_data;
    private int face_count;
    private String request_id;
    private String outer_id;
    private List<String> face_tokens;

    public String getFaceset_token() {
        return faceset_token;
    }

    public void setFaceset_token(String faceset_token) {
        this.faceset_token = faceset_token;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUser_data() {
        return user_data;
    }

    public void setUser_data(String user_data) {
        this.user_data = user_data;
    }

    public int getFace_count() {
        return face_count;
    }

    public void setFace_count(int face_count) {
        this.face_count = face_count;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getOuter_id() {
        return outer_id;
    }

    public void setOuter_id(String outer_id) {
        this.outer_id = outer_id;
    }

    public List<String> getFace_tokens() {
        return face_tokens;
    }

    public void setFace_tokens(List<String> face_tokens) {
        this.face_tokens = face_tokens;
    }

    @Override
    public String toString() {
        return "FaceSetDetail{" +
                "faceset_token='" + faceset_token + '\'' +
                ", display_name='" + display_name + '\'' +
                ", time_used=" + time_used +
                ", tags='" + tags + '\'' +
                ", user_data='" + user_data + '\'' +
                ", face_count=" + face_count +
                ", request_id='" + request_id + '\'' +
                ", outer_id='" + outer_id + '\'' +
                ", face_tokens=" + face_tokens +
                '}';
    }
}
