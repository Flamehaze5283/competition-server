package ysu.edu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ysu.edu.pojo.Email;


public interface IMailService {
    boolean sendMail(Email email);
    boolean sendSecurityCode(String email) throws JsonProcessingException;
}
