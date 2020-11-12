package com.dili.logger.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/10 15:49
 */
@Configuration
public class ESConfig extends AbstractElasticsearchConfiguration {

    @Value("#{'${dili.logger.elasticsearch.address}'.split(',')}")
    private String[] address;

    @Value("${dili.logger.elasticsearch.max.size:10000}")
    private Integer MAX_SIZE;

    /**
     * 连接超时时间
     */
    private final static int CONNECT_TIMEOUT = 5000;
    /**
     * 连接超时时间
     */
    private final static int SOCKET_TIMEOUT = 40000;
    /**
     * 获取连接的超时时间
     */
    private final static int CONNECTION_REQUEST_TIMEOUT = 1000;
    /**
     * 最大连接数
     */
    private final static int MAX_CONNECT_NUM = 100;
    /**
     * 最大路由连接数
     */
    private final static int MAX_CONNECT_ROUTE = 100;

    //es 服务器地址
    private static ArrayList<HttpHost> hostList = null;

    private static Integer size = 0;

    @PostConstruct
    public void init() {
        size = MAX_SIZE;
        hostList = new ArrayList<>();
        for (String hostAndPort : address) {
            String[] s = hostAndPort.split(":");
            hostList.add(new HttpHost(s[0], Integer.valueOf(s[1]), "http"));
        }
    }

    /**
     * 提供一个获取es查询时允许的最大size 的方法
     * @return
     */
    public static Integer getMaxSize(){
        return size;
    }

    @Override
    public RestHighLevelClient elasticsearchClient() {
        RestClientBuilder builder = RestClient.builder(hostList.toArray(new HttpHost[0]));
        // 配置一些请求配置的参数
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(CONNECT_TIMEOUT);
            requestConfigBuilder.setSocketTimeout(SOCKET_TIMEOUT);
            requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);
            return requestConfigBuilder;
        });
        // 配置一些httpClient的参数
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(MAX_CONNECT_NUM);
            httpClientBuilder.setMaxConnPerRoute(MAX_CONNECT_ROUTE);
            return httpClientBuilder;
        });
        builder.setFailureListener(new RestClient.FailureListener(){
            @Override
            public void onFailure(Node node) {
                super.onFailure(node);
            }
        });
        return new RestHighLevelClient(builder);
    }

//    @Bean
//    @Override
//    public EntityMapper entityMapper() {
//        return new CustomEntityMapper();
//    }
//
//    /**
//     * 自定义实体映射
//     */
//    public class CustomEntityMapper implements EntityMapper {
//        private final ObjectMapper objectMapper;
//        public CustomEntityMapper() {
//            objectMapper = new ObjectMapper();
//            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//            objectMapper.registerModule(new CustomGeoModule());
//            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//            objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
//            objectMapper.registerModule(new JavaTimeModule());
//        }
//
//        @Override
//        public String mapToString(Object object) throws IOException {
//            return objectMapper.writeValueAsString(object);
//        }
//
//        @Override
//        public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
//            return objectMapper.readValue(source, clazz);
//        }
//
//        @Override
//        public Map<String, Object> mapObject(Object source) {
//            try {
//                return objectMapper.readValue(mapToString(source), HashMap.class);
//            } catch (IOException e) {
//                throw new MappingException(e.getMessage(), e);
//            }
//        }
//
//        @Override
//        public <T> T readObject(Map<String, Object> source, Class<T> targetType) {
//            try {
//                return mapToObject(mapToString(source), targetType);
//            } catch (IOException e) {
//                throw new MappingException(e.getMessage(), e);
//            }
//        }
//    }

    /**
     * 默认的converter不支持long到localdatime的转换，从elasticsearch读取的时候会报错，所以在这里添加一个。
     */
    @Bean
    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        List<Converter> converters= new ArrayList<>();
        converters.add(LongToLocalDateTimeConverter.INSTANCE);
        return new ElasticsearchCustomConversions(converters);
    }

    @ReadingConverter
    static enum LongToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {
        INSTANCE;

        private LongToLocalDateTimeConverter() {
        }
        @Override
        public LocalDateTime convert(Long source) {
            return Instant.ofEpochMilli(source).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
    }
}
