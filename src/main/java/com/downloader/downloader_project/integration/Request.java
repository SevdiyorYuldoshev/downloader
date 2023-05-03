package com.downloader.downloader_project.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Request {
    @Value("${instagram.download.API.key}")
    private String insta_download_key;

    @Value("${instagram.url}")
    private String insta_url;

    @Value("${instagram.host}")
    private String insta_host;

    @Value("${youtube.download.API.key}")
    private String youtube_download_key;

    @Value("${youtube.url}")
    private String youtube_url;

    @Value("${youtube.host}")
    private String youtube_host;

    @Value("${tiktok.download.API.key}")
    private String tiktok_download_key;

    @Value("${tiktok.url}")
    private String tiktok_url;

    @Value("${tiktok.host}")
    private String tiktok_host;
    public String youtubeRequest(String url) {
        String response;
        if(url.contains("/shorts/")){
            String urls = url.substring(url.indexOf("shorts/")+7);
            response = getResponse(urls.substring(0,urls.indexOf("?")), youtube_url, youtube_download_key, youtube_host).body();
        } else {
            response = getResponse(url.substring(url.indexOf(".be")+4), youtube_url, youtube_download_key, youtube_host).body();
        }
        String body = response.substring(response.indexOf("\"videos\":{"));
        body = body.substring(body.indexOf("\"items\":[{\"url\":\"")+17);
        return body.substring(0,body.indexOf("\",\""));
    }

    public String tiktokRequest(String url) {
        String response = null;
        if(url.contains("/video/")){
            String channelName = url.substring(url.indexOf(".com/")+5);
            String urls = channelName.substring(channelName.indexOf("video/")+6);
            response = getResponse("www.tiktok.com%2F"
                    +channelName.substring(0,channelName.indexOf("/video"))+"%2Fvideo%2F"+urls.substring(0,urls.indexOf("?")),
                            tiktok_url,
                            tiktok_download_key,
                            tiktok_host).body();
        } else if (url.contains("vt.tiktok.com/")) {
            response = getResponse("vm.tiktok.com%2F"
                    +url.substring(url.indexOf(".com")+5, url.lastIndexOf('/') == url.length()-1 ? url.length()-1 : url.length()),
                            tiktok_url,
                            tiktok_download_key,
                            tiktok_host).body();
        }
        assert response != null;
        return response.substring(response.indexOf("video\":[\"")+9, response.indexOf("\"],"));
    }

//    {"Type":"Carousel",
//    "title":"❓WHICH ONE❓ Follow @amgmajestic & @ferrariunion for more!!! . .  Credits: @mr.benz63 @rocarstv  #mercedesbenz #mercedesg #w222 #w221 #w140 #w124 #w210 #w211 #w212 #w213 #c63samg #e63samg #g63amg #g63brabus #amgg #amgg63 #g6x6 #gwagon #gclass #mercedesbenzg63 #cla45amg #slrmclaren #c63amg #e63samg #w220 #glecoupe #glccoupe #gla45 #amggt63s #rsq8 #vwamarok",
//    "media":["https://scontent-arn2-1.cdninstagram.com/v/t50.2886-16/339327031_751113780000258_2887914172989034783_n.mp4?_nc_cat=100&vs=1401012587105759_2482698900&_nc_vs=HBksFQAYJEdEZTRPUlFDOWdwT0lxc0NBQi1SMDdhajdoTW9ia1lMQUFBRhUAAsgBABUAGCRHQ05WUHhSa2hrNnk3NW9EQUpGdUwzSU9uM1JNYmtZTEFBQUYVAgLIAQAoABgAGwGIB3VzZV9vaWwBMRUAACbi2ZPTnIjcPxUCKAJDMywXQCOZFocrAgwYEmRhc2hfYmFzZWxpbmVfMV92MREAde4HAA%3D%3D&ccb=1-7&_nc_sid=59939d&_nc_aid=0&efg=eyJ2ZW5jb2RlX3RhZyI6InZ0c192b2RfdXJsZ2VuLjcyMC5jYXJvdXNlbF9pdGVtIn0%3D&_nc_ohc=7r-Ko25pv9EAX8WNwPb&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfCmiky1BOLFbnFOGznAwNe3fCYxSG6WkVeyLzY9AwEnyg&oe=64397A1C&_nc_rid=9a6e63583f",
//          "https://scontent-arn2-1.cdninstagram.com/v/t50.2886-16/337230464_253597020437054_4434128146830386527_n.mp4?_nc_cat=101&vs=549978040591694_3679840645&_nc_vs=HBksFQAYJEdJQzZHUlFfdWxFb3BlWUFBRi1sWW5nT01Jazlia1lMQUFBRhUAAsgBABUAGCRHSmo5UEJURWpVQ3lqWUFEQUpZOERoeHMxU283YmtZTEFBQUYVAgLIAQAoABgAGwGIB3VzZV9vaWwBMRUAACacyLH9%2F8H3PxUCKAJDMywXQBkUeuFHrhQYEmRhc2hfYmFzZWxpbmVfMV92MREAde4HAA%3D%3D&ccb=1-7&_nc_sid=59939d&efg=eyJ2ZW5jb2RlX3RhZyI6InZ0c192b2RfdXJsZ2VuLjcyMC5jYXJvdXNlbF9pdGVtIn0%3D&_nc_ohc=dz_w-ZbXWfsAX-PYWcb&_nc_oc=AQmkljH-yFNCfdIj8eNClnvzHS96NAGzlzvJh4wnEUU-clPSSVR66CVrDUPulz_9AGlXyrKLHKfK3sdryO8AurG-&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfDzTX8hB_iMdLW-n5hEgXTtavMkhbwI2YwKNCFMIWBtoA&oe=6438C94A&_nc_rid=838995a186"],
//    "media_with_thumb":[
//          {"media":"https://scontent-arn2-1.cdninstagram.com/v/t50.2886-16/339327031_751113780000258_2887914172989034783_n.mp4?_nc_cat=100&vs=1401012587105759_2482698900&_nc_vs=HBksFQAYJEdEZTRPUlFDOWdwT0lxc0NBQi1SMDdhajdoTW9ia1lMQUFBRhUAAsgBABUAGCRHQ05WUHhSa2hrNnk3NW9EQUpGdUwzSU9uM1JNYmtZTEFBQUYVAgLIAQAoABgAGwGIB3VzZV9vaWwBMRUAACbi2ZPTnIjcPxUCKAJDMywXQCOZFocrAgwYEmRhc2hfYmFzZWxpbmVfMV92MREAde4HAA%3D%3D&ccb=1-7&_nc_sid=59939d&_nc_aid=0&efg=eyJ2ZW5jb2RlX3RhZyI6InZ0c192b2RfdXJsZ2VuLjcyMC5jYXJvdXNlbF9pdGVtIn0%3D&_nc_ohc=7r-Ko25pv9EAX8WNwPb&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfCmiky1BOLFbnFOGznAwNe3fCYxSG6WkVeyLzY9AwEnyg&oe=64397A1C&_nc_rid=9a6e63583f",
//              "Type":"Video",
//                  "thumbnail":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339340312_238486291894066_7758089538966903226_n.jpg?stp=dst-jpg_s640x640&_nc_cat=103&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=j_FpjbLkvloAX8bbEMF&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfD1vLe8i9ZE8vPRa_iQoMA393u7cI1OxMUIa3EiAj7yEQ&oe=64399FF1"},
//          {"media":"https://scontent-arn2-1.cdninstagram.com/v/t50.2886-16/337230464_253597020437054_4434128146830386527_n.mp4?_nc_cat=101&vs=549978040591694_3679840645&_nc_vs=HBksFQAYJEdJQzZHUlFfdWxFb3BlWUFBRi1sWW5nT01Jazlia1lMQUFBRhUAAsgBABUAGCRHSmo5UEJURWpVQ3lqWUFEQUpZOERoeHMxU283YmtZTEFBQUYVAgLIAQAoABgAGwGIB3VzZV9vaWwBMRUAACacyLH9%2F8H3PxUCKAJDMywXQBkUeuFHrhQYEmRhc2hfYmFzZWxpbmVfMV92MREAde4HAA%3D%3D&ccb=1-7&_nc_sid=59939d&efg=eyJ2ZW5jb2RlX3RhZyI6InZ0c192b2RfdXJsZ2VuLjcyMC5jYXJvdXNlbF9pdGVtIn0%3D&_nc_ohc=dz_w-ZbXWfsAX-PYWcb&_nc_oc=AQmkljH-yFNCfdIj8eNClnvzHS96NAGzlzvJh4wnEUU-clPSSVR66CVrDUPulz_9AGlXyrKLHKfK3sdryO8AurG-&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfDzTX8hB_iMdLW-n5hEgXTtavMkhbwI2YwKNCFMIWBtoA&oe=6438C94A&_nc_rid=838995a186",
//              "Type":"Video",
//                  "thumbnail":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339189230_235295445642817_7172356930772873335_n.jpg?stp=dst-jpg_s640x640&_nc_cat=102&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=URCUlCtt4zAAX_7kNiH&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfCUD5IA9hLm5nA6GXyBIROheF4pJ3Feh-Mx6xIPD17-4g&oe=6439113C"}],
//    "carousel_thumb":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339189230_235295445642817_7172356930772873335_n.jpg?stp=dst-jpg_s640x640&_nc_cat=102&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=URCUlCtt4zAAX_7kNiH&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfCUD5IA9hLm5nA6GXyBIROheF4pJ3Feh-Mx6xIPD17-4g&oe=6439113C",
//    "API":"MAIN_ADV"}

//    {"Type":"Carousel",
//    "title":"One of the rarest modern Lamborghinis, the Veneno.  4 total were built, 3 for customers and this factory car that is in the Lamborghini Museum.   Noticed that this one doesn’t have the production car headlights/DRLs 😮   $4 million when new. I’ve seen the Roadsters show up for sale for at least $10 million, and they built 9 of those. How valuable does that make the coupe??  #lamborghini #veneno #lamborghiniveneno #lambo",
//    "media":["https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339481316_205697348751546_614268530292559148_n.jpg?stp=dst-jpg_s640x640&_nc_cat=1&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=7buYa_pWk00AX8ZNUQv&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfBsBd6XFnZjkuYdbUt5kfpkDg_C1qBI4UswxRkD8cPmsw&oe=643930F8",
//          "https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339451491_524177329925847_4427893196726423424_n.jpg?stp=dst-jpg_s640x640&_nc_cat=1&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=xuZ92XplW1kAX9nsTyf&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfBIaAVhz9DLhw7km-dKZXKuksx8MdWbjhzjlipXiUzY0w&oe=64392AC6",
//          "https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339342754_1446321382569354_188050401655172142_n.jpg?stp=dst-jpg_s640x640&_nc_cat=100&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=SiPKFBDNJfAAX_yK4L0&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfAQnDuDw325FfO7oRmeo-RMyC9_yxrVC8L3T8q02xdYKA&oe=6438F79B",
//          "https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339346513_1028576348117613_3981067039473747510_n.jpg?stp=dst-jpg_s640x640&_nc_cat=100&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=lvPyMapTXzIAX_cDE-Y&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfB-lLu1Lwacf5n4GJy5PQHLR4Rmg-0m-CfratIJ7hkt-A&oe=6437ED34",
//          "https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339801399_545162827653941_4548450492462931423_n.jpg?stp=dst-jpg_s640x640&_nc_cat=102&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=5_cjeZot2lQAX-FEAeR&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfAmaPMqlAeytpBL6heVzlWJyWeCZ6ZvMGNjVg6VR5vzMw&oe=643875AD",
//          "https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339707872_518957247114587_6508393525368490243_n.jpg?stp=dst-jpg_s640x640&_nc_cat=104&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=LEZP9HcmGzUAX8SiSP4&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfDkX_c5JnBm8DliUEFxqHOCNoA2eCKUpklkID5vJ4ALcA&oe=6437D93F",
//          "https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339519332_1377971176312517_1048086321069103636_n.jpg?stp=dst-jpg_s640x640&_nc_cat=100&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=YUUAwheHrXgAX-I-Ezi&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfDQ49kmnXcBKBgMZJeS91b1_kWUxDbD9pZh64WnV9tbWQ&oe=6437B00A",
//          "https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339965352_547122450837670_4108437401062838845_n.jpg?stp=dst-jpg_s640x640&_nc_cat=109&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=Kcw5HczC8aQAX-gKQbs&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfAR6i_jVZ8mNAN4Br1CWHjEtwJ7gigTaIdY5tdhI9T1Lw&oe=6437E77B",
//          "https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339506076_479703327614824_6353025864436223607_n.jpg?stp=dst-jpg_s640x640&_nc_cat=105&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=jtERrTYje1sAX_JRyyb&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfCHXMwpt6the4AEIKZwH-eBpdi3L0rhaAw4HAU98TCMBQ&oe=64386AE9",
//          "https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339500574_1689653754823415_8253180543483130708_n.jpg?stp=dst-jpg_s640x640&_nc_cat=100&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=6usrzJgVJyEAX9dlAxW&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfBXvuMgIuC5quxcSGbaR7M3_7gOnZ4rEtYlEXQ9V_Ic6g&oe=6437E9E8"],
//    "media_with_thumb":[
//           {"media":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339481316_205697348751546_614268530292559148_n.jpg?stp=dst-jpg_s640x640&_nc_cat=1&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=7buYa_pWk00AX8ZNUQv&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfBsBd6XFnZjkuYdbUt5kfpkDg_C1qBI4UswxRkD8cPmsw&oe=643930F8",
//                  "Type":"Image",
//                      "thumbnail":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339481316_205697348751546_614268530292559148_n.jpg?stp=dst-jpg_s640x640&_nc_cat=1&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=7buYa_pWk00AX8ZNUQv&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfBsBd6XFnZjkuYdbUt5kfpkDg_C1qBI4UswxRkD8cPmsw&oe=643930F8"},
//           {"media":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339451491_524177329925847_4427893196726423424_n.jpg?stp=dst-jpg_s640x640&_nc_cat=1&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=xuZ92XplW1kAX9nsTyf&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfBIaAVhz9DLhw7km-dKZXKuksx8MdWbjhzjlipXiUzY0w&oe=64392AC6",
//                  "Type":"Image",
//                      "thumbnail":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339451491_524177329925847_4427893196726423424_n.jpg?stp=dst-jpg_s640x640&_nc_cat=1&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=xuZ92XplW1kAX9nsTyf&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfBIaAVhz9DLhw7km-dKZXKuksx8MdWbjhzjlipXiUzY0w&oe=64392AC6"},
//           {"media":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339342754_1446321382569354_188050401655172142_n.jpg?stp=dst-jpg_s640x640&_nc_cat=100&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=SiPKFBDNJfAAX_yK4L0&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfAQnDuDw325FfO7oRmeo-RMyC9_yxrVC8L3T8q02xdYKA&oe=6438F79B",
//                  "Type":"Image",
//                      "thumbnail":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339342754_1446321382569354_188050401655172142_n.jpg?stp=dst-jpg_s640x640&_nc_cat=100&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=SiPKFBDNJfAAX_yK4L0&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfAQnDuDw325FfO7oRmeo-RMyC9_yxrVC8L3T8q02xdYKA&oe=6438F79B"},
//           {"media":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339346513_1028576348117613_3981067039473747510_n.jpg?stp=dst-jpg_s640x640&_nc_cat=100&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=lvPyMapTXzIAX_cDE-Y&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfB-lLu1Lwacf5n4GJy5PQHLR4Rmg-0m-CfratIJ7hkt-A&oe=6437ED34",
//                  "Type":"Image",
//                      "thumbnail":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339346513_1028576348117613_3981067039473747510_n.jpg?stp=dst-jpg_s640x640&_nc_cat=100&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=lvPyMapTXzIAX_cDE-Y&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfB-lLu1Lwacf5n4GJy5PQHLR4Rmg-0m-CfratIJ7hkt-A&oe=6437ED34"},
//           {"media":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339801399_545162827653941_4548450492462931423_n.jpg?stp=dst-jpg_s640x640&_nc_cat=102&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=5_cjeZot2lQAX-FEAeR&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfAmaPMqlAeytpBL6heVzlWJyWeCZ6ZvMGNjVg6VR5vzMw&oe=643875AD",
//                  "Type":"Image",
//                      "thumbnail":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339801399_545162827653941_4548450492462931423_n.jpg?stp=dst-jpg_s640x640&_nc_cat=102&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=5_cjeZot2lQAX-FEAeR&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfAmaPMqlAeytpBL6heVzlWJyWeCZ6ZvMGNjVg6VR5vzMw&oe=643875AD"},
//           {"media":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339707872_518957247114587_6508393525368490243_n.jpg?stp=dst-jpg_s640x640&_nc_cat=104&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=LEZP9HcmGzUAX8SiSP4&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfDkX_c5JnBm8DliUEFxqHOCNoA2eCKUpklkID5vJ4ALcA&oe=6437D93F",
//                  "Type":"Image",
//                      "thumbnail":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339707872_518957247114587_6508393525368490243_n.jpg?stp=dst-jpg_s640x640&_nc_cat=104&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=LEZP9HcmGzUAX8SiSP4&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfDkX_c5JnBm8DliUEFxqHOCNoA2eCKUpklkID5vJ4ALcA&oe=6437D93F"},
//           {"media":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339519332_1377971176312517_1048086321069103636_n.jpg?stp=dst-jpg_s640x640&_nc_cat=100&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=YUUAwheHrXgAX-I-Ezi&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfDQ49kmnXcBKBgMZJeS91b1_kWUxDbD9pZh64WnV9tbWQ&oe=6437B00A",
//                  "Type":"Image",
//                      "thumbnail":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339519332_1377971176312517_1048086321069103636_n.jpg?stp=dst-jpg_s640x640&_nc_cat=100&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=YUUAwheHrXgAX-I-Ezi&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfDQ49kmnXcBKBgMZJeS91b1_kWUxDbD9pZh64WnV9tbWQ&oe=6437B00A"},
//           {"media":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339965352_547122450837670_4108437401062838845_n.jpg?stp=dst-jpg_s640x640&_nc_cat=109&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=Kcw5HczC8aQAX-gKQbs&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfAR6i_jVZ8mNAN4Br1CWHjEtwJ7gigTaIdY5tdhI9T1Lw&oe=6437E77B",
//                  "Type":"Image",
//                      "thumbnail":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339965352_547122450837670_4108437401062838845_n.jpg?stp=dst-jpg_s640x640&_nc_cat=109&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=Kcw5HczC8aQAX-gKQbs&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfAR6i_jVZ8mNAN4Br1CWHjEtwJ7gigTaIdY5tdhI9T1Lw&oe=6437E77B"},
//           {"media":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339506076_479703327614824_6353025864436223607_n.jpg?stp=dst-jpg_s640x640&_nc_cat=105&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=jtERrTYje1sAX_JRyyb&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfCHXMwpt6the4AEIKZwH-eBpdi3L0rhaAw4HAU98TCMBQ&oe=64386AE9",
//                  "Type":"Image",
//                      "thumbnail":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339506076_479703327614824_6353025864436223607_n.jpg?stp=dst-jpg_s640x640&_nc_cat=105&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=jtERrTYje1sAX_JRyyb&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfCHXMwpt6the4AEIKZwH-eBpdi3L0rhaAw4HAU98TCMBQ&oe=64386AE9"},
//           {"media":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339500574_1689653754823415_8253180543483130708_n.jpg?stp=dst-jpg_s640x640&_nc_cat=100&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=6usrzJgVJyEAX9dlAxW&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfBXvuMgIuC5quxcSGbaR7M3_7gOnZ4rEtYlEXQ9V_Ic6g&oe=6437E9E8",
//                  "Type":"Image",
//                      "thumbnail":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339500574_1689653754823415_8253180543483130708_n.jpg?stp=dst-jpg_s640x640&_nc_cat=100&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=6usrzJgVJyEAX9dlAxW&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfBXvuMgIuC5quxcSGbaR7M3_7gOnZ4rEtYlEXQ9V_Ic6g&oe=6437E9E8"}],
//    "carousel_thumb":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339500574_1689653754823415_8253180543483130708_n.jpg?stp=dst-jpg_s640x640&_nc_cat=100&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=6usrzJgVJyEAX9dlAxW&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfBXvuMgIuC5quxcSGbaR7M3_7gOnZ4rEtYlEXQ9V_Ic6g&oe=6437E9E8",
//    "API":"MAIN_ADV"}

//    {"Type":"Carousel",
//    "title":"\"Farhod katta sababchi\" ...  #littlelarhammadankuchli #mittivine #mittime #latipovuz #latipovjahongir #intervyu #skromno",
//    "media":["https://scontent-arn2-1.cdninstagram.com/v/t50.2886-16/337903785_108825812166058_4869872577267416653_n.mp4?_nc_cat=107&vs=232649495923778_597176488&_nc_vs=HBksFQAYJEdLa0FKQlNxWmVEN19XSUFBRTJfY0sxT1E1VkRia1lMQUFBRhUAAsgBABUAGCRHUEczU3hSX3JuWDZVUHdCQUhVUWhaTnNhRHM3YmtZTEFBQUYVAgLIAQAoABgAGwGIB3VzZV9vaWwBMRUAACaogb7c8cfqPxUCKAJDMywXQERUWhysCDEYEmRhc2hfYmFzZWxpbmVfMV92MREAde4HAA%3D%3D&ccb=1-7&_nc_sid=59939d&efg=eyJ2ZW5jb2RlX3RhZyI6InZ0c192b2RfdXJsZ2VuLjcyMC5jYXJvdXNlbF9pdGVtIn0%3D&_nc_ohc=w6jyLfdHbl8AX_U6GMP&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfBz4YQA4NIhaFxEm7WfVlpefvEygZ-3wXBrXgR_UKVtRQ&oe=64394B1F&_nc_rid=979102daba",
//          "https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/340183842_705543798033418_7344162298709244894_n.webp?stp=dst-jpg_s640x640&_nc_cat=1&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=rBx8hvcU9ggAX9sRYDB&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfCheOWezErVXOU193xPqA7S0PtK4v7rVnQKOrnxyGhbVQ&oe=6437C3E8"],
//    "media_with_thumb":[
//          {"media":"https://scontent-arn2-1.cdninstagram.com/v/t50.2886-16/337903785_108825812166058_4869872577267416653_n.mp4?_nc_cat=107&vs=232649495923778_597176488&_nc_vs=HBksFQAYJEdLa0FKQlNxWmVEN19XSUFBRTJfY0sxT1E1VkRia1lMQUFBRhUAAsgBABUAGCRHUEczU3hSX3JuWDZVUHdCQUhVUWhaTnNhRHM3YmtZTEFBQUYVAgLIAQAoABgAGwGIB3VzZV9vaWwBMRUAACaogb7c8cfqPxUCKAJDMywXQERUWhysCDEYEmRhc2hfYmFzZWxpbmVfMV92MREAde4HAA%3D%3D&ccb=1-7&_nc_sid=59939d&efg=eyJ2ZW5jb2RlX3RhZyI6InZ0c192b2RfdXJsZ2VuLjcyMC5jYXJvdXNlbF9pdGVtIn0%3D&_nc_ohc=w6jyLfdHbl8AX_U6GMP&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfBz4YQA4NIhaFxEm7WfVlpefvEygZ-3wXBrXgR_UKVtRQ&oe=64394B1F&_nc_rid=979102daba",
//              "Type":"Video",
//                  "thumbnail":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/339825174_8844828065587838_5544967959358949153_n.jpg?stp=dst-jpg_s640x640&_nc_cat=111&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=O5ldzplymhQAX8i_Y8N&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfDBA5RwahpgSSgkVXwQQzEHu0aDl6jpeGHCyhXWRzh30g&oe=64389355"},
//          {"media":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/340183842_705543798033418_7344162298709244894_n.webp?stp=dst-jpg_s640x640&_nc_cat=1&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=rBx8hvcU9ggAX9sRYDB&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfCheOWezErVXOU193xPqA7S0PtK4v7rVnQKOrnxyGhbVQ&oe=6437C3E8",
//              "Type":"Image",
//                  "thumbnail":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/340183842_705543798033418_7344162298709244894_n.webp?stp=dst-jpg_s640x640&_nc_cat=1&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=rBx8hvcU9ggAX9sRYDB&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfCheOWezErVXOU193xPqA7S0PtK4v7rVnQKOrnxyGhbVQ&oe=6437C3E8"}],
//    "carousel_thumb":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/340183842_705543798033418_7344162298709244894_n.webp?stp=dst-jpg_s640x640&_nc_cat=1&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=rBx8hvcU9ggAX9sRYDB&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfCheOWezErVXOU193xPqA7S0PtK4v7rVnQKOrnxyGhbVQ&oe=6437C3E8",
//    "API":"MAIN_ADV"}

//    {"media":"https://scontent-arn2-1.cdninstagram.com/o1/v/t16/f1/m82/E24CBBC5D155616429B8A06A66D775BE_video_dashinit.mp4?efg=eyJ2ZW5jb2RlX3RhZyI6InZ0c192b2RfdXJsZ2VuLjcyMC5jbGlwcyJ9&_nc_ht=scontent-arn2-1.cdninstagram.com&_nc_cat=104&vs=965080134662700_3954904097&_nc_vs=HBksFQIYT2lnX3hwdl9yZWVsc19wZXJtYW5lbnRfcHJvZC9FMjRDQkJDNUQxNTU2MTY0MjlCOEEwNkE2NkQ3NzVCRV92aWRlb19kYXNoaW5pdC5tcDQVAALIAQAVABgkR0R1eE14UjM4SGFnY0ZVRkFIc3Faa2hhc0JaRGJxX0VBQUFGFQICyAEAKAAYABsBiAd1c2Vfb2lsATEVAAAmmJaSlffZ20AVAigCQzMsF0BCzMzMzMzNGBJkYXNoX2Jhc2VsaW5lXzFfdjERAHUAAA%3D%3D&ccb=9-4&oh=00_AfA8V-H8HoXtA8vPeJaqaIn6YjzUN0IOJRbeeVXtNgYoyw&oe=643529F2&_nc_sid=ea0b6e&_nc_rid=0e38b897dc",
//      "thumbnail":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/338762611_925658758760086_8541138060906800041_n.jpg?stp=dst-jpg_s640x640&_nc_cat=104&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=onjJQ4xn4NgAX_5tuAV&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfCFI7wlSdgCuySp2YEstPgXeqMIAJBPcEOZvufRhCfCwg&oe=643966B1",
//      "title":"Shunaqasini ko‘rganmisiz?🤯  📍Los Angeles aeroporti",
//      "Type":"Post-Video",
//      "API":"MAIN_ADV"}

//    {"media":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/338909922_1647679872336527_3853693407413407931_n.jpg?stp=dst-jpg_s640x640&_nc_cat=106&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=IOD0oQF_iFoAX9BYAnK&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfDJDsamtxJSCQgkmNVxZXDaHwFYIaYubSpEh61Ymqk8Vg&oe=643941A1",
//    "thumbnail":"https://scontent-arn2-1.cdninstagram.com/v/t51.29350-15/338909922_1647679872336527_3853693407413407931_n.jpg?stp=dst-jpg_s640x640&_nc_cat=106&ccb=1-7&_nc_sid=8ae9d6&_nc_ohc=IOD0oQF_iFoAX9BYAnK&_nc_ht=scontent-arn2-1.cdninstagram.com&oh=00_AfDJDsamtxJSCQgkmNVxZXDaHwFYIaYubSpEh61Ymqk8Vg&oe=643941A1",
//    "title":"Arnold Schwarzenegger - age 22. Follow for more 🔥  Via @n.s_puremotivation1  #bodybuilding #arnoldschwarzenegger #fyp #puremotivation #rarepicture #viral #arnold #aesthetics #oldschool #goldenera #perfection #musclemass #goldsgym",
//    "Type":"Post-Image",
//    "API":"MAIN_ADV"}
    public Map<String, List<String>> instagramRequest(String url){
        String urls;
        String  response = null;
        if (url.contains("instagram.com/stories/")) {
            urls = url.substring(url.indexOf("stories") + 8);
            String storiesNumber = urls.substring(urls.indexOf("/") + 1, urls.indexOf('?') == -1 ? urls.length() - 1 : urls.indexOf('?'));
            response = getResponse("stories%2F" + urls.substring(0, urls.indexOf('/')) + "%2F" + storiesNumber, insta_url, insta_download_key, insta_host).body();
            return new HashMap<>(Map.of("video", List.of(response.substring(response.indexOf("media\":\"") + 8, response.indexOf("\",\"")))));
        } else if(url.contains("/reel/")){
            urls = url.substring(url.indexOf("reel")+5);
            response = getResponse("reel%2F"+urls.substring(0,urls.indexOf("/")), insta_url, insta_download_key, insta_host).body();
        } else if (url.contains("/p/")) {
            urls = url.substring(url.indexOf("/p/")+3, url.lastIndexOf('/') == url.length()-1 ? url.length()-1 : url.length());
            response = getResponse("p%2F"+urls, insta_url, insta_download_key, insta_host).body();
        }

        assert response != null;
        if(response.startsWith("{\"Type\":\"Carousel\"")) {
            Map<String, List<String>> res = new HashMap<>(Map.of("Image", new ArrayList<>(), "Video", new ArrayList<>()));

            for (String s :
                    response.substring
                                    (
                                            response.indexOf("\"media_with_thumb\":[{\"media\":\"") + 30,
                                            response.indexOf("],\"carousel_thumb\"")
                                    )
                    .split("media\":\"")
            ) {
                res.get(s.substring(s.indexOf("Type\":\"")+7, s.indexOf("Type\":\"")+12)).add(s.substring(0,s.indexOf("\",\"")));
            }
            return res;
        }

        return new HashMap<>(
                Map.of(
                        response.substring
                                (
                                        response.indexOf("\"Type\":\"Post-")+13,
                                        response.indexOf("\"Type\":\"Post-")+18
                                ),
                List.of(
                        response.substring
                                (
                                response.indexOf("media\":\"") + 8,
                                response.indexOf("\",\"")
                                )
                        )
                )
        );
    }

    private HttpResponse<String> getResponse(String link, String url, String key, String host){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url+link))
                    .header("X-RapidAPI-Key", key)
                    .header("X-RapidAPI-Host", host)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        }catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
