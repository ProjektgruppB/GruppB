package gruppB.mobile;

import oracle.adfmf.json.JSONArray;

public class PlacesResult {
    
      private PlacesGeometry geometry;
      private String icon;
      private String id;
      private String name;
      private boolean open_now;
      private double rating;
      private String reference;
      private String types;
      private String vicinity;
      
    public PlacesResult() {
        super();
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOpen_now(boolean open_now) {
        this.open_now = open_now;
    }

    public boolean isOpen_now() {
        return open_now;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getTypes() {
        return types;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setGeometry(PlacesGeometry geometry) {
        this.geometry = geometry;
    }

    public PlacesGeometry getGeometry() {
        return geometry;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }
}


