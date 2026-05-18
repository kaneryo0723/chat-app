package in.tech_camp.chat_app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry){

    // /uploads/** というURLパターンで表示するリクエストが発生した場合は、src/main/resources/static/uploads/ディレクトリの内容を見る
    registry.addResourceHandler("/uploads/**")
    //先頭にfile:をつけると、ファイルシステム上の特定ディレクトリから直接読み込むようになる。
            .addResourceLocations("file:src/main/resources/static/uploads/");
  }
  
}
