package aviv.myicebreaker.module.CustomObjects;

/**
 * Created by Aviad on 16/11/2016.
 */
public class FacebookAlbumObject {

    String albumUrlImgCover, albumName, albumId;

    public FacebookAlbumObject(String albumUrlImgCover, String albumName, String albumId) {
        this.albumUrlImgCover = albumUrlImgCover;
        this.albumName = albumName;
        this.albumId = albumId;
    }

    public String getAlbumUrlImgCover() {
        return albumUrlImgCover;
    }

    public void setAlbumUrlImgCover(String albumUrlImgCover) {
        this.albumUrlImgCover = albumUrlImgCover;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
}
