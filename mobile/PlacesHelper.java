package mobile;

import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONException;
import oracle.adfmf.json.JSONObject;

public class PlacesHelper {
    public PlacesHelper() {
        super();
    }
    
    
    public static PlacesResponse transformObject(ServiceResult service) {
        PlacesResponse response = new PlacesResponse();
        response.setStatus(service.getStatus());    
        
        PlacesResultList results = new PlacesResultList(); 
        response.setResults(results);

        JSONArray resultList = service.getResults();
        for ( int i = 0 ;  i <  resultList.length() ; i++ ) {
            try {
               PlacesResult placeResult = new PlacesResult();
               JSONObject result = resultList.getJSONObject(i);
  
                if ( result.get("geometry") != null ) { 
                    JSONObject geometry = (JSONObject)result.get("geometry");
                    PlacesGeometry place = new PlacesGeometry();
                    
                    if (geometry.get("location") != null ) {
                        JSONObject location = (JSONObject)geometry.get("location");
                           
                        LatLng latLng = new LatLng();
                        latLng.setLat( location.getDouble("lat") ); 
                        latLng.setLng( location.getDouble("lng") ); 
                        place.setLocation(latLng);
                    }                   
                        
                    placeResult.setGeometry(place);
                }
                    if ( result.getString("icon") != null ) { 
                        placeResult.setIcon(result.getString("icon"));
                    }    
                    
                    if ( result.getString("id") != null ) { 
                        placeResult.setId(result.getString("id"));
                    }  
                    
                    if ( result.getString("name") != null ) { 
                        placeResult.setName(result.getString("name"));
                    }  
                  if ( result.get("opening_hours") != null ) {   
                    JSONObject opening_hours = (JSONObject)result.get("opening_hours");
                    if ( opening_hours.get("open_now") != null ) { 
                        placeResult.setOpen_now(opening_hours.getBoolean("open_now"));
                    }
                  }
               
               //get photos TODO??
               
                if ( result.getString("rating") != null ) { 
                    placeResult.setRating(result.getDouble("rating"));
                }  

                if ( result.getString("reference") != null ) { 
                    placeResult.setReference(result.getString("reference"));
                }                  
               
               if ( result.get("types") != null ) { 
                  JSONArray types = (JSONArray)result.get("types");
                  String placeType = "";
                  for ( int p = 0 ;  p <  types.length() ; p++ ) {
                    placeType += types.get(p) + ",";
                  }
                  placeResult.setTypes(placeType);
               }
                if ( result.getString("vicinity") != null ) { 
                    placeResult.setVicinity(result.getString("vicinity"));
                }  
                //Kan blir fel har?
                results.AddPlaceResult(placeResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        
        return response;
    }
}
