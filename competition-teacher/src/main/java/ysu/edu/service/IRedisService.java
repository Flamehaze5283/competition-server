package ysu.edu.service;

public interface IRedisService {
    void set(String key, String value, long time);
    String get(String key);
    boolean hasKey(String key);
    boolean expire(String key, long time);
}
