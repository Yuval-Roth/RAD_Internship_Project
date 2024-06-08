package com.arealcompany.ms_api_gateway.repository;

import com.arealcompany.ms_api_gateway.business.security.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("usersRepository")
public interface UsersRepository extends MongoRepository<User, String>{
}
