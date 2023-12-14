package com.slice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.serde.annotation.Serdeable;

@JsonIgnoreProperties(ignoreUnknown=true)
@Serdeable
public class FileWithContentDto {

    private String content;
    private String encoding;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public String toString() {
        return "FileWithContentDto{" +
                "content='" + content + '\'' +
                ", encoding='" + encoding + '\'' +
                '}';
    }
}
