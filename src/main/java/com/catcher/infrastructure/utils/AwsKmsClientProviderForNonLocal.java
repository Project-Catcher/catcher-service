package com.catcher.infrastructure.utils;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"dev", "prod"})
@Configuration
public class AwsKmsClientProviderForNonLocal {

    @Bean
    public AWSKMS provideAwsKmsClient() {
        return AWSKMSClientBuilder.standard()
                .build();
    }

}
