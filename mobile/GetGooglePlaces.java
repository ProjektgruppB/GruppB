package gruppB.mobile;

import com.sun.util.logging.Level;

import oracle.adfmf.amx.event.ActionEvent;
import oracle.adfmf.dc.ws.rest.RestServiceAdapter;
import oracle.adfmf.framework.api.JSONBeanSerializationHelper;
import oracle.adfmf.framework.api.Model;
import oracle.adfmf.util.Utility;
import oracle.adfmf.util.logging.Trace;


public class GetGooglePlaces {
    //private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    // KEY!
    private static final String API_KEY = "AIzaSyBvCutV8lGlxao53-eWZdsjXrLoi-5ckrg";
        private String result = "empty";
        private PlacesResultList placeResult = null;
        private double lat = 59.3308;
        private double lng = 18.0631;
    public GetGooglePlaces() {
    }
    
    public void setResult(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
        }

        public void setPlaceResult(PlacesResultList placeResult) {
            this.placeResult = placeResult;
        }

        public PlacesResultList getPlaceResult() {
            return placeResult;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLat() {
            return lat;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLng() {
            return lng;
        }

    public void searchAction(ActionEvent actionEvent) {
        System.out.println("begin");
                this.result = "called";
                RestServiceAdapter restServiceAdapter = Model.createRestServiceAdapter();
                
                // Clear any previously set request properties, if any
                restServiceAdapter.clearRequestProperties();
                
                // Set the connection name
                restServiceAdapter.setConnectionName("GooglePlaceJSON");
                
                // Specify the type of request
                restServiceAdapter.setRequestType(RestServiceAdapter.REQUEST_TYPE_GET);
                restServiceAdapter.addRequestProperty("Content-Type", "application/json");
                restServiceAdapter.addRequestProperty("Accept", "application/json; charset=UTF-8");
                // Specify the number of retries
                restServiceAdapter.setRetryLimit(0);
                
                // Set the URI which is defined after the endpoint in the connections.xml.
                // The request is the endpoint + the URI being set
                restServiceAdapter.setRequestURI("?location=59.3308,18.0631&radius=500&types=food|bar|cafe|restaurant&sensor=true&key="+API_KEY);
                //&rankby=distance eller &rankby=prominence
                //&types=food|bar|cafe|restaurant
                String response = "not found";
                System.out.println("\n"+ restServiceAdapter.getRequestURI()+"\n");
                JSONBeanSerializationHelper jsonHelper = new JSONBeanSerializationHelper();
                
                try {
                    // For GET request, there is no payload
                    response = restServiceAdapter.send("");
                    System.out.println("\nStart Response\n");
                    System.out.print(response);
                    System.out.println("\nEnd Response\n");
                    ServiceResult responseObject = (ServiceResult)jsonHelper.fromJSON(ServiceResult.class, response);
                    if ( "OK".equalsIgnoreCase( responseObject.getStatus()) ) {
                        placeResult = PlacesHelper.transformObject(responseObject).getResults();
                    }
                    this.result = responseObject.getStatus();
                    
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.result = "error";
                }
                System.out.println("\nResult status:"+result+"\n");
                   
    }
}
