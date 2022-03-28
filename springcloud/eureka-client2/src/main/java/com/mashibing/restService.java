package com.mashibing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class restService {

    @Autowired
    @Qualifier("myRestTemplate2")
    private RestTemplate myRestTemplate2;



   // @HystrixCommand(fallbackMethod ="myFallBack" )
    public Person getPerson() {
        String url="http://provider01/client8";
        Person person = myRestTemplate2.getForObject(url, Person.class);
        return  person;
    }



    public Person myFallBack(){
        return  new Person("备用的人",11);
    }
}
