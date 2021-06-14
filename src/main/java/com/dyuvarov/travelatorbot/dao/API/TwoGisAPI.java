package com.dyuvarov.travelatorbot.dao.API;

import com.dyuvarov.travelatorbot.bot.TravelatorBot;
import com.dyuvarov.travelatorbot.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TwoGisAPI implements MapsAPI{
    final String    URL;
    final String    APIKEY;
    final Integer   MAX_PAGE;
    final String    HOTEL_FACTOR;
    final String    CATERING_FACTOR;
    final String    HOTEL_QUERY;
    final String    CATERING_QUERY;

    TwoGisAPI(  @Value("${api.url}") String url,
                @Value("${api.apikey}") String apikey,
                @Value("${api.maxPage}") Integer maxPage)
    {
        this.URL = url;
        this.APIKEY = apikey;
        this.MAX_PAGE = maxPage;
        this.HOTEL_FACTOR = "Проживание от";
        this.CATERING_FACTOR = "Средний чек";
        this.HOTEL_QUERY = "отель,";
        this.CATERING_QUERY = "общественное питание,";
    }

    @Override
    public TravelCost calculateLiving(String city) {
        return (calculateTravelPart(city, HOTEL_FACTOR, HOTEL_QUERY));
    }

    @Override
    public TravelCost calculateEating(String city) {
       return(calculateTravelPart(city, CATERING_FACTOR, CATERING_QUERY));
    }

    /**
     * find organisations and calculate average price
     * @param city - name of city to search
     * @param factor - string to find price
     * @param query - query string to API
     * @return TravelCost object
     */
    private TravelCost calculateTravelPart(String city, String factor, String query) {
        TravelCost travelCost;
        if (query.equals(HOTEL_QUERY))
           travelCost = new HotelCost();
        else if (query.equals(CATERING_QUERY))
            travelCost = new CateringCost();
        else
            return null;

        int page = 1;
        while (travelCost.getOrganisations().size() < 20 && page <= MAX_PAGE) { //TODO increase page count
            QueryResult queryResult = askAPI(query + city, page);
            if (queryResult == null) {
                ++page;
                continue;
            }
            Set<Organisation> caterings = createOrganisationsSet(queryResult, factor);
            travelCost.addOrganisation(caterings);
            ++page;
        }
        if (travelCost.getOrganisations().isEmpty())
            return null;
        travelCost.calculateAvgPrice();
        //TODO: add data in database
        return travelCost;
    }

    /**
     * Create set of organisations with not empty price
     * @param queryResult - result of query to API
     * @param factorName - string to find price
     * @return Set of Hotel/Catering objects
     */
    private Set<Organisation> createOrganisationsSet(QueryResult queryResult, String factorName) {
        Set<Organisation> orgSet = new HashSet<>();
        if (queryResult.getResult() == null)
            return orgSet;
        for (Item item : queryResult.getResult().getItems()) {
            if (item == null || item.getContext() == null)
                continue;
            String name = item.getName();
            String url = findItemUrl(item);
            for (StopFactor stopFactor : item.getContext().getStop_factors()) {
                String factor = stopFactor.getName();
                if (factor.contains(factorName)){
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(factor);
                    if (matcher.find()) {
                        String value = factor.substring(matcher.start(), matcher.end());
                        Integer cost = Integer.parseInt(value);
                        if (factorName.equals(CATERING_FACTOR))
                        {
                            orgSet.add(new Catering(name, cost, url));
                            break ;
                        }
                        else if (factorName.equals(HOTEL_FACTOR))
                        {
                            orgSet.add(new Hotel(name, cost, url));
                            break ;
                        }
                    }
                }
            }
        }
        return orgSet;
    }

    private String findItemUrl(Item item) {
        if (item == null)
            return "";

        Ad ad = item.getAds();
        if (ad == null)
            return "";

        Link link = ad.getLink();
        if (link == null)
            return "";

        String linkStr = link.getValue();
        if (linkStr.isEmpty())
            return "";
        String spliter = "http";
        String[] linkArr = linkStr.split(spliter);
        if (linkArr.length > 1 && linkArr[linkArr.length-1].length() < 100)
            return spliter+linkArr[linkArr.length-1];
        return "";
    }

    /**
     * GET query to API
     * @param text - query tex ("общественное питание"/"отель")
     * @param page - number of page in answer
     * @return QueryResult object
     */
    private QueryResult askAPI(String text, int page) {
        RestTemplate restTemplate = new RestTemplate();

//        String jsonAnswer = restTemplate.getForObject(createQuery(text, page),String.class);

//        For tests:
        if (page > 1)
            return null;
        String jsonAnswer = "";
        if (text.contains(HOTEL_QUERY))
            jsonAnswer  = testQueryResult("/Users/ugreyiro/JavaProj/TravelatorBOT/hotelAnsw2org");
        else
            jsonAnswer = testQueryResult("/Users/ugreyiro/JavaProj/TravelatorBOT/answ.txt");

        return (deserializeJson(jsonAnswer));
    }

    /**
     * create full query string for API GET request
     * @param text - query tex ("общественное питание"/"отель")
     * @param page - number of page in answer
     * @return url for GET
     */
    private String createQuery(String text, int page) {
        String query = URL + "?key=" + APIKEY + "&q=" + text + "&page=" + page +
                "&locale=ru_RU&type=branch&fields=items.context&search_type=one_branch";
        return query;
    }

    /**
     * Create QueryResult object from JSON string
     * @param jsonAnswer - API JSON answer
     * @return QueryResult object
     */
    private QueryResult deserializeJson(String jsonAnswer) {
        if ((jsonAnswer == null) || (!jsonAnswer.contains("\"code\":200")))
            return null;
        ObjectMapper mapper = new ObjectMapper();
        QueryResult queryResult = null;
        try {
            queryResult = mapper.readValue(jsonAnswer, QueryResult.class);
        }
        catch (JsonMappingException e) {
            e.printStackTrace();
            //TODO: handle it and log it
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            //TODO: handle it and log it
        }
        return queryResult;
    }

    private String testQueryResult(String path){
        String result = "";
        char[] buf = new char[1024];
        try {
//            FileReader reader = new FileReader("/Users/ugreyiro/JavaProj/TravelatorBOT/answ.txt");
            FileReader reader = new FileReader(path);
            while (reader.read(buf) != -1)
            {
                result += new String(buf);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }//TODO: deleteme
}

class QueryResult {

    private Meta     meta;
    private Result   result;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

}

class Meta {
    private String api_version;
    private String code;
    private String issue_date;

    public String getApi_version() {
        return api_version;
    }

    public void setApi_version(String api_version) {
        this.api_version = api_version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(String issue_date) {
        this.issue_date = issue_date;
    }
}

class Result{
    private List<Item> items = new ArrayList<>();
    private String   total;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

class StopFactor {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Context {
    private List<StopFactor> stop_factors = new ArrayList<>();

    public List<StopFactor> getStop_factors() {
        return stop_factors;
    }

    public void setStop_factors(List<StopFactor> stop_factors) {
        this.stop_factors = stop_factors;
    }
}

class Link {
    private String text;
    private String value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

class Ad {
    private String  article;
    private String  article_warning;
    private Link    link;
    private String  text;
    private String  text_warning;
    private String  warning;

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getArticle_warning() {
        return article_warning;
    }

    public void setArticle_warning(String article_warning) {
        this.article_warning = article_warning;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText_warning() {
        return text_warning;
    }

    public void setText_warning(String text_warning) {
        this.text_warning = text_warning;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }
}

class Item {
    private String  address_comment;
    private String  address_name;
    private Context context;
    private Ad      ads;
    private String  id;
    private String  name;
    private String  full_name;
    private String  type;
    private String  propertyName;
    private String  purpose_name;
    private String  building_name;

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public String getPurpose_name() {
        return purpose_name;
    }

    public void setPurpose_name(String purpose_name) {
        this.purpose_name = purpose_name;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getAddress_comment() {
        return address_comment;
    }

    public void setAddress_comment(String address_comment) {
        this.address_comment = address_comment;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public Ad getAds() {
        return ads;
    }

    public void setAds(Ad ads) {
        this.ads = ads;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}