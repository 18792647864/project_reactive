package club.manhuang.project_reactive.utils;

import club.manhuang.project_reactive.rabbitmq.client.CwWebClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
public abstract class AbstractToolsUtil {


    protected CwWebClient cwWebClient;

    @Autowired
    public void setCwWebClient(CwWebClient cwWebClient) {
        this.cwWebClient = cwWebClient;
    }
}
