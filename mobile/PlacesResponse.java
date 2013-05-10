package gruppB.mobile;

public class PlacesResponse {
    
    private String status;
    private PlacesResultList results;
    public PlacesResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public void setResults(PlacesResultList results) {
        this.results = results;
    }

    public PlacesResultList getResults() {
        return results;
    }
}

