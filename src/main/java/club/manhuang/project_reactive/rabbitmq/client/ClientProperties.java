package club.manhuang.project_reactive.rabbitmq.client;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientProperties {

    @NotEmpty(message = "Params [appId] can not be empty")
    private String appId;

    @NotEmpty(message = "Params [appId] can not be empty")
    private String site;

    @NotEmpty(message = "Params [appId] can not be empty")
    private String publicKey;

    @NotEmpty(message = "Params [appId] can not be empty")
    private String privateKey;

    @NotEmpty(message = "Params [appId] can not be empty")
    private Long tenantId;

}
