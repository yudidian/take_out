package com.dhy.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.io.IOException;
import java.math.BigInteger;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * 对象映射器:基于jackson将Java对象转为json，或者将json转为Java对象
 * 将JSON解析为Java对象的过程称为 [从JSON反序列化Java对象]
 * 从Java对象生成JSON的过程称为 [序列化Java对象到JSON]
 */
public class JacksonObjectMapper extends ObjectMapper {

  public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
  public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

  public JacksonObjectMapper() {
    super();
    //收到未知属性时不报异常
    this.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

    //反序列化时，属性不存在的兼容处理
    this.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


    SimpleModule simpleModule = new SimpleModule()
        .addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
          @Override
          public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String dateTimeStr = jsonParser.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
            return dateTime.atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
          }
        })
        .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
        .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))
        .addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
          @Override
          public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String dateStr = jsonParser.getText();
            try {
              // 尝试将输入解析为时间戳
              long timestamp = Long.parseLong(dateStr);
              return Instant.ofEpochMilli(timestamp).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
            } catch (NumberFormatException e) {
              // 如果解析失败，则尝试将输入解析为时间字符串
              try {
                return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
              } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Invalid date format: " + dateStr);
              }
            }
          }
        })
        .addSerializer(BigInteger.class, ToStringSerializer.instance)
        .addSerializer(Long.class, ToStringSerializer.instance)
        .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
        .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
        .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

    //注册功能模块 例如，可以添加自定义序列化器和反序列化器
    this.registerModule(simpleModule);
  }
}
