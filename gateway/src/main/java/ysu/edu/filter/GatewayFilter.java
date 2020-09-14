package ysu.edu.filter;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.Charset;

@Component
public class GatewayFilter implements GlobalFilter,Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("进入过滤器");
        ServerHttpRequest request = exchange.getRequest();
        // 获取到uri
        URI uri = request.getURI();
        StringBuilder builder = new StringBuilder();
        String oldquery = uri.getRawQuery();
        if(StringUtils.isNotBlank(oldquery)) {
            builder.append(oldquery).append("&");
        }
        builder.append("username=" + "admin");
        URI newuri = UriComponentsBuilder.fromUri(uri)
                .replaceQuery(builder.toString())
                .build(true).toUri();
        ServerHttpRequest newrequest = exchange.getRequest().mutate().uri(newuri).build();





        // 拿出请求方式 如果是OPTION 放行
        System.out.println(request.getMethod().name());

        // 通过request.getHeaders() 可以拿到请求头 headers 取出token
        HttpHeaders headers = request.getHeaders();

        // 通过 request.getPath().value() 能获取到请求地址 根据地址判断是否有权限
        System.out.println(request.getPath().value());
        // 获取response对象
  /*      ServerHttpResponse response = exchange.getResponse();
        HttpHeaders respheaders = response.getHeaders();
        respheaders.set("Content-type","application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",10);
        jsonObject.put("name","张三");
        DataBuffer buffer = response.bufferFactory().wrap(jsonObject.toJSONString().getBytes(Charset.forName("UTF-8")));
*/

        /**
         * 如果有权限  该去转发到哪个微服务就去转发到哪个微服务
         *
         *  怎样通过
         *       chain.filter(exchange);
         *  怎样不通过
         *      ServerHttpResponse response = exchange.getResponse();
         *         HttpHeaders respheaders = response.getHeaders();
         *         respheaders.set("Content-type","application/json;charset=UTF-8");
         *         JSONObject jsonObject = new JSONObject();
         *         jsonObject.put("id",10);
         *         jsonObject.put("name","张三");
         *         DataBuffer buffer = response.bufferFactory().wrap(jsonObject.toJSONString().getBytes(Charset.forName("UTF-8")));
         *         response.writeWith(Flux.just(buffer));
         * */

        System.out.println(newrequest.getURI().getRawQuery());
        return  chain.filter(exchange.mutate().request(newrequest).build());
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
