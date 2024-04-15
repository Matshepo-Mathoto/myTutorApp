package com.mytutor.demo;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class LocationService {

    
    /**
     * The ipinfo api key saved in application properties file
     */
    @Value("${ipinfo.api.key}") 
    private String apiKey;

    private final String apiUrl = "https://ipinfo.io";

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Get details of the user or clients ip address using IpInfo seveices
     * @return rest template of user ip address information and location
     */
    @GetMapping("/showLocation")
    public String getLocation() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String clientIp = getClientIp(request);

        String url = apiUrl + "/" + clientIp + "?token=" + apiKey; // Add your API key if required
        return restTemplate.getForObject(url, String.class);
    }

    /**
     * Gets the ip address of the user for this httpsession
     * @param request: HttpServlet request
     * @return client ip address
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader != null) {
            return xForwardedForHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }


  /**
   * Get location specific coordinates from the getLocation() results using json
     * @return coordinates of the ip address of the client
     */
      public String getLoc() {
        String location = getLocation();
        //System.out.println(location);
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(location);

            String loc = (String) json.get("loc");
            return loc;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null; 
        }
    }
}
