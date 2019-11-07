package com.amazon.aws.partners.saasfactory;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.*;

@JsonDeserialize(builder = CognitoUser.Builder.class)
public class CognitoUser {

    private String username;
    private String status;
    private Map<String, String> attributes = new HashMap<>();
    private List<?> mfaOptions = new ArrayList<>();

    private CognitoUser(Builder builder) {
        this.username = builder.username;
        this.status = builder.status;
        this.attributes = builder.attributes;
        this.mfaOptions = builder.mfaOptions;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public List<?> getMfaOptions() {
        return mfaOptions;
    }

    public void setMfaOptions(List<?> mfaOptions) {
        this.mfaOptions = mfaOptions;
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public final static class Builder {

        private String username;
        private String status;
        private Map<String, String> attributes = new HashMap<>();
        private List<?> mfaOptions = new ArrayList<>();

        private Builder() {
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder attributes(Map<String, String> attributes) {
            this.attributes = attributes;
            return this;
        }

        public Builder attribute(String key, String value) {
            if (this.attributes == null) {
                this.attributes = new HashMap<>();
            }
            this.attributes.put(key, value);
            return this;
        }

        public Builder mfaOptions(List<?> mfaOptions) {
            this.mfaOptions = mfaOptions;
            return this;
        }

        public CognitoUser build() {
            return new CognitoUser(this);
        }
    }
}
