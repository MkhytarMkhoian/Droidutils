package com.droidutils.http.builder;

/**
 * Created by Misha on 07.09.2014.
 */
public class Url {

    private Url(){

    }

    public static class Builder {

        private StringBuilder mUrlBuilder;

        public Builder(String baseUrl){
            mUrlBuilder = new StringBuilder();
            mUrlBuilder.append(baseUrl);
        }

        public Builder addParameter(String key, String value){

            if (mUrlBuilder.charAt(mUrlBuilder.length() - 1) != '?'){
                mUrlBuilder.append("&");
            }
            mUrlBuilder.append(key).append("=").append(value);
            return this;
        }

        public String build() {
            return mUrlBuilder.toString();
        }
    }
}
