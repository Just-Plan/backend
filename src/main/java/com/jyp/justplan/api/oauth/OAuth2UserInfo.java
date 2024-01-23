package com.jyp.justplan.api.oauth;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();

    String getImage();
}
