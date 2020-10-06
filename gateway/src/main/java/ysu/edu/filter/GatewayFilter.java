package ysu.edu.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GatewayFilter implements GlobalFilter,Ordered {
//    @Resource
//    IRedisService redisService;

//    @lombok.SneakyThrows

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        System.out.println("进入过滤器");
//        ServerHttpRequest request = exchange.getRequest();
//        ServerHttpResponse response = exchange.getResponse();
//
//        //静态资源访问,接口通过判断url中是否含有指定的字符来判断是否放行,在这里设置url的白名单
//        String path = request.getURI().getPath();
//        System.out.println(path);
//        //静态资源访问,接口通过判断url中是否含有指定的字符来判断是否放行,在这里设置url的白名单
//        //如果是白名单路由，放行
//        if(path.equals("/teacher/logout") || path.equals("/teacher/token") || path.equals("/download/person") || path.equals("/download/team")
//                || path.equals("/teacher/rePassword") || path.equals("/email/sendSecurityCode")){
//            return chain.filter(exchange);
//        }
//
//        // 通过request.getHeaders() 可以拿到请求头 headers
//        HttpHeaders headers = request.getHeaders();
//        //取出token
//        String token = headers.getFirst("token");
//        //如果没有token
//        if(StringUtils.isEmpty(token)){
//            return hasNotToken(response, ServerResponse.noToken());
//        }
//
//        //2 获取ssdb中的 密码 跟 token中的密码是否一致，//NOTOKEN
//        //3 客户端的登录时间 和 ssdb中的登录时间 不一致（被其它人登录过）//NOTOKEN
//        //获取token中的email，根据key 获取 ssdb中的登录信息
//        if(!checkToken(token)){
//            return hasNotToken(response,ServerResponse.noToken());
//        }
//        //4 更新下 有效期
//        String email = JWT.decode(token).getClaim("email").asString();
//        String email_key = JWTUtil.TEACHERKEY +"-"+ email;
//        redisService.expire(email_key, 60 * 30);

        return  chain.filter(exchange);
    }


//    private boolean checkToken(String token) throws JsonProcessingException {
//        String email = JWT.decode(token).getClaim("email").asString();
//        String email_key = JWTUtil.TEACHERKEY +"-"+ email;
//        if(redisService.hasKey(email_key)){
//            //
//            String password = JWT.decode(token).getClaim("password").asString();
//            Long timestamp = JWT.decode(token).getClaim("timestamp").asLong();
//
//            String userJson = redisService.get(email_key);
//            ObjectMapper objectMapper =  new ObjectMapper();
//            Map userMap = objectMapper.readValue(userJson, Map.class);
//
//            long lastLogin = (long) userMap.get("last_login");
//
//            if(!password.equals(userMap.get("password"))
//                    || timestamp.longValue() != lastLogin){
//                ServerResponse.noToken();
//                return false;
//            }
//        }else{
//            ServerResponse.noToken();
//            return false;
//        }
//        return true;
//    }
//
//    private Mono<Void> hasNotToken(ServerHttpResponse response, ServerResponse serverResponse) {
//        HttpHeaders respHeaders = response.getHeaders();
//        respHeaders.set("Content-type","application/json;charset=UTF-8");
//        JSONObject jsonObject = new JSONObject();
//        DataBuffer buffer = response.bufferFactory().wrap(jsonObject.toJSONString(serverResponse).getBytes(Charset.forName("UTF-8")));
//        return response.writeWith(Flux.just(buffer));
//    }

    @Override
    public int getOrder() {
        return 1;
    }
}
