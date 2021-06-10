package com.dyuvarov.travelatorbot.dao.API;

import com.dyuvarov.travelatorbot.model.Catering;
import com.dyuvarov.travelatorbot.model.Organisation;
import com.dyuvarov.travelatorbot.model.TravelCost;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TwoGisAPI implements MapsAPI{
    final String URL;
    final String APIKEY;


    TwoGisAPI(@Value("${api.url}") String url, @Value("${api.apikey}") String apikey) {
        this.URL = url;
        this.APIKEY = apikey;
    }

    @Override
    public TravelCost calculateLiving(String city) {
        return null;
    }

    @Override
    public TravelCost calculateEating(String city) {
        TravelCost travelCost= new TravelCost();
        QueryResult queryResult = askAPI("общественное питание," + city);
        if (queryResult == null)
            return null;
        Set<Catering> caterings = createCateringSet(queryResult);
        if (caterings.isEmpty())
            return null;
        Catering anyCatering = caterings.stream().findAny().get();
        int minCost = anyCatering.getCost();
        Organisation minCostCatering = anyCatering;
        int maxCost = anyCatering.getCost();
        Organisation maxCostCatering = anyCatering;
        int totalSum = 0;
        for (Catering catering : caterings) {
            int currentCost = catering.getCost();
            if (currentCost < minCost){
                minCost = currentCost;
                minCostCatering = catering;
            }
            else if (currentCost > maxCost) {
                maxCost = currentCost;
                maxCostCatering = catering;
            }
            totalSum+=currentCost;
        }
        travelCost.setEconomy(minCostCatering);
        travelCost.setMinPrice(minCost);
        travelCost.setPremium(maxCostCatering);
        travelCost.setMaxPrice(maxCost);
        travelCost.setAveragePrice((float)totalSum / caterings.size());
        return travelCost;
    }

    private Set<Catering> createCateringSet(QueryResult queryResult) {
        Set<Catering> caterings = new HashSet<>();

        for (Item item : queryResult.getResult().getItems()) {
            String name = item.getName();
            for (StopFactor stopFactor : item.getContext().getStop_factors()) {
                String factor = stopFactor.getName();
                if (factor.contains("Средний чек")){
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(factor);
                    if (matcher.find()) {
                        String value = factor.substring(matcher.start(), matcher.end());
                        Integer cost = Integer.parseInt(value);
                        caterings.add(new Catering(name, cost));
                    }
                }
            }
        }
        return caterings;
    }

    private QueryResult askAPI(String text) {
        RestTemplate restTemplate = new RestTemplate();

        //String jsonAnswer = restTemplate.getForObject(createQuery(text),String.class);
        String jsonAnswer = testQueryResult();
        return (deserializeJson(jsonAnswer));
    }

    private String createQuery(String text) {
        String query = URL + "?key=" + APIKEY + "&q=" + text + "&locale=ru_RU" + "&type=branch"
                + "&fields=items.context";
        return query;
    }

    private QueryResult deserializeJson(String jsonAnswer) {
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

    private String testQueryResult(){
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("/Users/ugreyiro/JavaProj/TravelatorBOT/answ");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return reader.readLine();
        }
        catch (IOException e) {
            return null;
        }
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