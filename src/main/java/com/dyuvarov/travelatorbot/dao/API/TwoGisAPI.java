package com.dyuvarov.travelatorbot.dao.API;

import com.dyuvarov.travelatorbot.model.Catering;
import com.dyuvarov.travelatorbot.model.Organisation;
import com.dyuvarov.travelatorbot.model.TravelCost;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.corba.se.idl.constExpr.Or;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.transform.impl.AddStaticInitTransformer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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

        List<Catering> caterings = new LinkedList<>();
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
        return travelCost;
    }

    private QueryResult askAPI(String text) {
        RestTemplate restTemplate = new RestTemplate();

        //String jsonAnswer = restTemplate.getForObject(createQuery(text),String.class);
        String jsonAnswer = testQueryResult();
        return (deserializeJson(jsonAnswer));
    }

    private String createQuery(String city, String text) {
        String query = URL + "?key=" + APIKEY + "&q=" + text + "&locale=ru_RU" + "&type=branch"
                + "&fields=items.context" + "&page_size=20";
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

    private String testQueryResult() {
        return "{\"meta\":{\"api_version\":\"3.0.606536\",\"code\":200,\"issue_date\":\"20210610\"}," +
                "\"result\":" +
                "   {\"items\":" +
                    "[{\"address_comment\":\"1 этаж\",\"address_name\":\"Чистопольская, 33в\",\"ads\":{\"article\":" +
                    "\"\"," +
                    "\"article_warning\":\" ООО «Бургер Рус», г. Москва, ОГРН 1097746274009.\"," +
                    "\"link\":{\"text\":\"А еще — доставка\",\"value\":\"http://link.2gis.ru/1.2/40AF06A5/webapi/20210601/project21/70000001026609205/2gis.ru/dly7py08566H4H8301IG23GG6eluz746G6G9537626870C31fcqpB66865G32G6G1J197J2JD9hEuv1926341013346H1J4HHHGJ272?https://burgerking.ru/delivery#\"}," +
                    "\"text\":\"Попробуй новый мексиканский гамбургер + 5 блюд всего за 200 рублей!\",\"text_warning\":\"0+.\",\"warning\":\" ООО «Бургер Рус», г. Москва, ОГРН 1097746274009.\"}," +
                    "\"context\":{\"stop_factors\":[{\"name\":\"Средний чек 300 ₽\"},{\"name\":\"Доставка\"}]},\"id\":\"70000001026609205\",\"name\":\"Бургер Кинг, сеть ресторанов быстрого питания\",\"type\":\"branch\"}]," +
                "\"total\":2223}}";
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
    private String  type;


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