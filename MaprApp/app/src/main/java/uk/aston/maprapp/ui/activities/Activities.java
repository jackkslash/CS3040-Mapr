package uk.aston.maprapp.ui.activities;

public class Activities {

    private String placeName;
    private String rating;
    private String vicinity;
    private String plusCode;

    public Activities(String placeName, String rating, String vicinity, String plusCode){
        this.placeName = placeName;
        this.rating = rating;
        this.vicinity = vicinity;
        this.plusCode = plusCode;
    }

    public Activities() {

    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getPlusCode() {
        return plusCode;
    }

    public void setPlusCode(String plusCode) {
        this.plusCode = plusCode;
    }
}
