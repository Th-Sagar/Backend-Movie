package com.movieapi.Movie_Api.auth.services;

import com.movieapi.Movie_Api.auth.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public boolean updatePassword(String email,String newPassword){
        Query query = new Query(Criteria.where("email").is(email));
        Update update = new Update().set("password",newPassword);
       var result= mongoTemplate.updateFirst(query,update, User.class);

       return result.getMatchedCount()>0;
    }
}
