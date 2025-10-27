package nnu.edu.back.common.utils;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/08/19/9:49
 * @Description:
 */
public class SSEUtil {
    private static Map<String, SseEmitter> sseCache = new ConcurrentHashMap<>();

    public static SseEmitter subscribe(String id) throws IOException {
        SseEmitter sseEmitter = new SseEmitter(1000 * 60 * 60l);
        if (sseCache.get(id) != null) {
            return sseCache.get(id);
        }
        sseCache.put(id, sseEmitter);
        sseEmitter.onTimeout(() -> {
            sseEmitter.complete();
            sseCache.remove(id);
        });
        return sseEmitter;
    }

    public static void over(String id) throws IOException {
        SseEmitter sseEmitter = sseCache.get(id);
        if (sseEmitter != null) {
            sseEmitter.send(SseEmitter.event().name("stop").data("stop!"));
            sseEmitter.complete();
            sseCache.remove(id);
        }
    }

    public static void message(String id, String content) throws IOException {
        SseEmitter sseEmitter = sseCache.get(id);
        if (sseEmitter != null) {
            sseEmitter.send(SseEmitter.event().name("msg").data(content));
        }
    }

    public static Set<String> test() {
        return sseCache.keySet();
    }
}
