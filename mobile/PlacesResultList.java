package mobile;

import java.util.ArrayList;
import java.util.List;

public class PlacesResultList {
    
    private List placeResults;
    
    public PlacesResultList() {
        super();

        if (placeResults == null) {
            placeResults = new ArrayList();
        }           
    }

    public PlacesResult[] getplaceResults() {
        PlacesResult e[] = null;
        e = (PlacesResult[])placeResults.toArray(new PlacesResult[placeResults.size()]);
        return e;
    }

    public int  getPlaceResultCount() {
        return placeResults.size();
    }    

    public void AddPlaceResult(PlacesResult result) {
        placeResults.add(result);
    }

}

