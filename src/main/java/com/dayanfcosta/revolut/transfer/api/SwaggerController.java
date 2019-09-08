package com.dayanfcosta.revolut.transfer.api;

import com.dayanfcosta.revolut.transfer.config.SwaggerConfig;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.views.View;
import io.swagger.v3.oas.annotations.Hidden;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;


@Hidden
@Controller("/api")
public class SwaggerController {

  @Inject
  SwaggerConfig config;

  @View("swagger/index")
  @Get
  public SwaggerConfig index() {
    return config;
  }

  @View("swagger/index")
  @Get("/{url}")
  public SwaggerConfig renderSpec(@NotNull String url) {
    return new SwaggerConfig.Builder()
        .withDeepLinking(config.isDeepLinking())
        .withLayout(config.getLayout())
        .withVersion(config.getVersion())
        .withUrls(Collections.singletonList(new SwaggerConfig.URIConfig.Builder()
            .withName(url)
            .withURI(url)
            .build()))
        .build();
  }

  @View("swagger/index")
  @Post
  public SwaggerConfig renderSpecs(@Body @NotEmpty List<SwaggerConfig.URIConfig> urls) {
    return new SwaggerConfig.Builder()
        .withDeepLinking(config.isDeepLinking())
        .withLayout(config.getLayout())
        .withVersion(config.getVersion())
        .withUrls(urls)
        .build();
  }
}