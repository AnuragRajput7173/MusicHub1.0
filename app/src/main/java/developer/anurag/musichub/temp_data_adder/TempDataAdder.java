package developer.anurag.musichub.temp_data_adder;

public class TempDataAdder {
    int id;
    String  trackUri,posterUri;
    public String title,singer;
    public TempDataAdder(){}

    public TempDataAdder(int id, String title, String singer, String trackUri, String posterUri){
        this.id=id;
        this.title=title;
        this.singer=singer;
        this.trackUri=trackUri;
        this.posterUri=posterUri;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrackUri() {
        return trackUri;
    }

    public void setTrackUri(String trackUri) {
        this.trackUri = trackUri;
    }

    public String getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(String posterUri) {
        this.posterUri = posterUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getId() {
        return id;
    }

}
