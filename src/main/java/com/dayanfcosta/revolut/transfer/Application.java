package com.dayanfcosta.revolut.transfer;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
    info = @Info(
        title = "Money Transactions API",
        version = "1.0",
        description = "API for money transactions (debit, credit and transfers)",
        license = @License(name = "GPLv3"),
        contact = @Contact(
            url = "https://github.com/dayanfcosta",
            name = "Dayan Costa",
            email = "dayanfcosta@gmail.com")
    )
)
public class Application {

  public static void main(String[] args) {
    Micronaut.run(Application.class);
  }

}