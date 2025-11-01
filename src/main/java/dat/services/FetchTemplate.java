package dat.services;

import dat.entities.Example;

public class FetchTemplate {
    private final FetchTools fetchTools;

    public FetchTemplate(FetchTools fetchTools) { this.fetchTools = fetchTools; }

    public Example getexample(String sample) {
        return fetchTools.getFromApi(Uri(sample), Example.class);
    }

    private static String Uri(String sample){
        return "https://packingapi.cphbusinessapps.dk/packinglist/" +  sample;
    }
}
