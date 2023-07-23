package kr.co.finote.backend.src.article.dto.response;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChatgptResponse {

    private String id;
    private String object;
    private Long created;
    private String model;
    private List<Choice> choices;
    private ChatgptResponse.Usage usage;

    @Getter
    @NoArgsConstructor
    public static class Usage {
        public int promptTokens;
        public int completionTokens;
        public int totalTokens;
    }

    @Getter
    @NoArgsConstructor
    public static class Choice {
        public ChatgptResponse.Choice.Message message;
        public String finishReason;
        public int index;

        @Getter
        @NoArgsConstructor
        public static class Message {
            public String role;
            public String content;
        }
    }
}
