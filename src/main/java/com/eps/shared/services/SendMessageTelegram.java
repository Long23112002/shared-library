package com.eps.shared.services;

import com.eps.shared.config.Config;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendMessageTelegram {

  @Async
  public void send(String traceId, String msg) {

    String appName = Config.getEnvironmentProperty("spring.application.name");
    String environment = Config.getEnvironmentProperty("spring.application.environment");
    String apiToken = Config.getEnvironmentProperty("telegram.apiToken");
    String chatId = Config.getEnvironmentProperty("telegram.chatId");

    String msg2 =
        "**** <b>" + environment + " - " + appName + " - " + traceId + "</b> **** \n" + msg;
    try {
      URI uri =
          new URI(
              "https",
              "api.telegram.org",
              "/bot" + apiToken + "/sendMessage",
              "chat_id=" + chatId + "&parse_mode=HTML&text=" + msg2,
              "");
      URL url = uri.toURL();
      URLConnection conn = url.openConnection();
      InputStream is = new BufferedInputStream(conn.getInputStream());
    } catch (Exception e) {

    }
  }
}
