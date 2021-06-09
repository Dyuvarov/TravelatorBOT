package com.dyuvarov.travelatorbot.dao.API;

import com.dyuvarov.travelatorbot.model.TravelCost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class YandexAPI implements MapsAPI {
    final String URL;
    final String APIKEY;


    YandexAPI(@Value("${api.url}") String url, @Value("${api.apikey}") String apikey) {
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
        RestTemplate restTemplate = new RestTemplate();
        String queryResult = restTemplate.getForObject(createQuery(city, "столовая"),String.class);
        return travelCost;
    }

    private String createQuery(String city, String text) {
        String query = URL + "?apikey="+APIKEY + "&text=" + text + "&lang=ru_RU" + "&type=biz" + "&ll=49.12214,55.78874"
                + "&spn=0.552069,0.400552";
        return query;
    }
}

/** Yandex **/
/*
{"type":"FeatureCollection","properties":{"ResponseMetaData":{"SearchResponse":{"found":90,"display":"multiple","boundedBy":[[49.03944088,55.75429461],[49.17732012,55.85429205]]},
"SearchRequest":{"request":"столовая","skip":0,"results":10,"boundedBy":[[48.8461055,55.58794697],[49.3981745,55.98849899]]}}},

"features":[{"type":"Feature","geometry":{"type":"Point","coordinates":[49.115108,55.790733]},
"properties":{"name":"Добрая столовая","description":"ул. Баумана, 21, Казань, Россия","boundedBy":[[49.111003,55.78841993],[49.119213,55.79304593]],"CompanyMetaData":{"id":"1171439851","name":"Добрая столовая",
"address":"Россия, Республика Татарстан, Казань, улица Баумана, 21","url":"https://dobraya.su/","Phones":[{"type":"phone","formatted":"+7 (843) 226-34-03"},{"type":"phone","formatted":"+7 (843) 297-91-99"},
{"type":"phone","formatted":"8 (800) 234-01-91"}],"Categories":[{"class":"cafe","name":"Столовая"},{"class":"supermarket","name":"Магазин кулинарии"}],"Hours":{"text":"пн-пт 07:30–21:00; сб,вс 08:00–20:00",
"Availabilities":[{"Intervals":[{"from":"07:30:00","to":"21:00:00"}],"Monday":true,"Tuesday":true,"Wednesday":true,"Thursday":true,"Friday":true},
{"Intervals":[{"from":"08:00:00","to":"20:00:00"}],"Saturday":true,"Sunday":true}]}}}},

{"type":"Feature","geometry":{"type":"Point","coordinates":[49.123,55.797508]},"properties":{"name":"Домашняя Столовая","description":"Большая Красная ул., 34/16, Вахитовский район, Казань, Россия","boundedBy":
[[49.1188945,55.79519543],[49.1271055,55.79982043]],"CompanyMetaData":{"id":"57236734968","name":"Домашняя Столовая","address":"Россия, Республика Татарстан, Казань, Вахитовский район, Большая Красная улица, 34/16",
"url":"http://www.domstolovaya.ru/","Phones":[{"type":"phone","formatted":"+7 (987) 277-44-70"},{"type":"phone","formatted":"+7 (937) 043-00-51"},{"type":"phone","formatted":"+7 (917) 397-34-97"},{"type":"phone",
"formatted":"+7 (843) 528-23-45"}],"Categories":[{"class":"cafe","name":"Столовая"},{"class":"cafe","name":"Кафе"}],"Hours":{"text":"пн-сб 07:30–21:00; вс 09:00–21:00","Availabilities":
[{"Intervals":[{"from":"07:30:00","to":"21:00:00"}],"Monday":true,"Tuesday":true,"Wednesday":true,"Thursday":true,"Friday":true,"Saturday":true},{"Intervals":[{"from":"09:00:00","to":"21:00:00"}],"Sunday":true}]}}}},

{"type":"Feature","geometry":{"type":"Point","coordinates":[49.118851,55.792527]},"properties":{"name":"Домашняя Столовая","description":"Кремлёвская ул., 27, Казань, Россия",
"boundedBy":[[49.1147455,55.79021393],[49.1229565,55.79483993]],"CompanyMetaData":{"id":"15671031610","name":"Домашняя Столовая","address":"Россия, Республика Татарстан, Казань, Кремлёвская улица, 27",
"url":"http://domstolovaya.ru/","Phones":[{"type":"phone","formatted":"+7 (919) 690-67-38"}],"Categories":[{"class":"cafe","name":"Столовая"}],"Hours":{"text":"пн-сб 07:30–21:00; вс 09:00–21:00",
"Availabilities":[{"Intervals":[{"from":"07:30:00","to":"21:00:00"}],"Monday":true,"Tuesday":true,"Wednesday":true,"Thursday":true,"Friday":true,"Saturday":true},{"Intervals":[{"from":"09:00:00","to":"21:00:00"}],
"Sunday":true}]}}}},{"type":"Feature","geometry":{"type":"Point","coordinates":[49.130349,55.787943]},"properties":{"name":"Домашняя столовая","description":"ул. Бутлерова, 21, Казань, Россия",
"boundedBy":[[49.1262435,55.78562993],[49.1344545,55.79025593]],"CompanyMetaData":{"id":"25366712239","name":"Домашняя столовая",
"address":"Россия, Республика Татарстан, Казань, улица Бутлерова, 21","url":"http://www.domstolovaya.ru/","Phones":[{"type":"phone","formatted":"+7 (903) 343-85-67"},{"type":"phone","formatted":"+7 (917) 397-34-97"}],"Categories":[{"class":"cafe","name":"Столовая"},{"class":"cafe","name":"Кафе"},{"class":"fast food","name":"Быстрое питание"}],"Hours":{"text":"пн-сб 07:30–21:00; вс 09:00–21:00","Availabilities":[{"Intervals":[{"from":"07:30:00","to":"21:00:00"}],"Monday":true,"Tuesday":true,"Wednesday":true,"Thursday":true,"Friday":true,"Saturday":true},{"Intervals":[{"from":"09:00:00","to":"21:00:00"}],"Sunday":true}]}}}},{"type":"Feature","geometry":{"type":"Point","coordinates":[49.14806,55.782913]},"properties":{"name":"Добрая столовая","description":"ул. Калинина, 62, Вахитовский район, Казань, Россия","boundedBy":[[49.1439545,55.78059943],[49.1521655,55.78522643]],"CompanyMetaData":{"id":"71362045281","name":"Добрая столовая","address":"Россия, Республика Татарстан, Казань, Вахитовский район, улица Калинина, 62","url":"https://dobraya.su/","Phones":[{"type":"phone","formatted":"+7 (843) 225-02-50"},{"type":"phone","formatted":"+7 (843) 297-91-99"}],"Categories":[{"class":"cafe","name":"Столовая"},{"class":"supermarket","name":"Магазин кулинарии"}],"Hours":{"text":"пн-пт 07:30–21:00; сб,вс 08:00–21:00","Availabilities":[{"Intervals":[{"from":"07:30:00","to":"21:00:00"}],"Monday":true,"Tuesday":true,"Wednesday":true,"Thursday":true,"Friday":true},{"Intervals":[{"from":"08:00:00","to":"21:00:00"}],"Saturday":true,"Sunday":true}]}}}},{"type":"Feature","geometry":{"type":"Point","coordinates":[49.127214,55.782328]},"properties":{"name":"Домашняя Столовая","description":"Спартаковская ул., 1, Казань, Россия","boundedBy":[[49.1231085,55.78001443],[49.1313195,55.78464143]],"CompanyMetaData":{"id":"206965575419","name":"Домашняя Столовая","address":"Россия, Республика Татарстан, Казань, Спартаковская улица, 1","url":"http://domstolovaya.ru/","Phones":[{"type":"phone","formatted":"+7 (917) 397-34-97"},{"type":"phone","formatted":"+7 (987) 205-92-69"}],"Categories":[{"class":"cafe","name":"Столовая"},{"class":"cafe","name":"Кафе"}],"Hours":{"text":"пн-сб 07:30–21:00; вс 09:00–21:00","Availabilities":[{"Intervals":[{"from":"07:30:00","to":"21:00:00"}],"Monday":true,"Tuesday":true,"Wednesday":true,"Thursday":true,"Friday":true,"Saturday":true},{"Intervals":[{"from":"09:00:00","to":"21:00:00"}],"Sunday":true}]}}}},{"type":"Feature","geometry":{"type":"Point","coordinates":[49.101626,55.789433]},"properties":{"name":"Добрая столовая","description":"ул. Рустема Яхина, 13, Казань, Россия","boundedBy":[[49.0975205,55.78711993],[49.1057315,55.79174593]],"CompanyMetaData":{"id":"1035783665","name":"Добрая столовая","address":"Россия, Республика Татарстан, Казань, улица Рустема Яхина, 13","url":"http://dobraya.su/","Phones":[{"type":"phone","formatted":"+7 (987) 226-34-08"},{"type":"phone","formatted":"+7 (987) 297-91-99"}],"Categories":[{"class":"cafe","name":"Столовая"},{"class":"supermarket","name":"Магазин кулинарии"},{"class":"cafe","name":"Кафе"}],"Hours":{"text":"пн-пт 07:30–21:00; сб,вс 08:00–20:00","Availabilities":[{"Intervals":[{"from":"07:30:00","to":"21:00:00"}],"Monday":true,"Tuesday":true,"Wednesday":true,"Thursday":true,"Friday":true},{"Intervals":[{"from":"08:00:00","to":"20:00:00"}],"Saturday":true,"Sunday":true}]}}}},{"type":"Feature","geometry":{"type":"Point","coordinates":[49.113611,55.826308]},"properties":{"name":"Добрая столовая","description":"просп. Ямашева, 37Б, Казань, Россия","boundedBy":[[49.109506,55.82399693],[49.117716,55.82861893]],"CompanyMetaData":{"id":"61706584732","name":"Добрая столовая","address":"Россия, Республика Татарстан, Казань, проспект Ямашева, 37Б","url":"https://dobraya.su/","Phones":[{"type":"phone","formatted":"+7 (843) 226-34-07"},{"type":"phone","formatted":"+7 (843) 297-91-99"}],"Categories":[{"class":"cafe","name":"Столовая"},{"class":"fallback services","name":"Доставка еды и обедов"},{"class":"supermarket","name":"Магазин кулинарии"}],"Hours":{"text":"пн-пт 07:30–21:00; сб,вс 08:00–20:00","Availabilities":[{"Intervals":[{"from":"07:30:00","to":"21:00:00"}],"Monday":true,"Tuesday":true,"Wednesday":true,"Thursday":true,"Friday":true},{"Intervals":[{"from":"08:00:00","to":"20:00:00"}],"Saturday":true,"Sunday":true}]}}}},{"type":"Feature","geometry":{"type":"Point","coordinates":[49.132371,55.819786]},"properties":{"name":"Добрая столовая","description":"Чистопольская ул., 61А, Казань, Россия","boundedBy":[[49.128266,55.81747493],[49.136476,55.82209693]],"CompanyMetaData":{"id":"1594964512","name":"Добрая столовая","address":"Россия, Республика Татарстан, Казань, Чистопольская улица, 61А","url":"https://dobraya.su/","Phones":[{"type":"phone","formatted":"+7 (953) 480-77-80"},{"type":"phone","formatted":"+7 (843) 297-91-99"},{"type":"phone","formatted":"+7 (843) 248-40-87"}],"Categories":[{"class":"cafe","name":"Столовая"},{"class":"supermarket","name":"Магазин кулинарии"},{"class":"restaurants","name":"Ресторан"},{"class":"fast food","name":"Быстрое питание"}],"Hours":{"text":"пн-пт 07:30–21:00; сб,вс 08:00–20:00","Availabilities":[{"Intervals":[{"from":"07:30:00","to":"21:00:00"}],"Monday":true,"Tuesday":true,"Wednesday":true,"Thursday":true,"Friday":true},{"Intervals":[{"from":"08:00:00","to":"20:00:00"}],"Saturday":true,"Sunday":true}]}}}},{"type":"Feature","geometry":{"type":"Point","coordinates":[49.068701,55.812022]},"properties":{"name":"Добрая столовая","description":"ул. Алафузова, 3, Казань, Россия","boundedBy":[[49.0645955,55.80971043],[49.0728065,55.81433343]],"CompanyMetaData":{"id":"1532630615","name":"Добрая столовая","address":"Россия, Республика Татарстан, Казань, улица Алафузова, 3","url":"http://dobraya.su/","Phones":[{"type":"phone","formatted":"+7 (843) 297-91-99"},{"type":"phone","formatted":"+7 (843) 226-34-02"}],"Categories":[{"class":"cafe","name":"Столовая"},{"class":"supermarket","name":"Магазин кулинарии"}],"Hours":{"text":"пн-пт 07:30–21:00; сб,вс 08:00–20:00","Availabilities":[{"Intervals":[{"from":"07:30:00","to":"21:00:00"}],"Monday":true,"Tuesday":true,"Wednesday":true,"Thursday":true,"Friday":true},{"Intervals":[{"from":"08:00:00","to":"20:00:00"}],"Saturday":true,"Sunday":true}]}}}}]}
*/
