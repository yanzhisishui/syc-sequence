package com.syc.sequence;

import com.syc.sequence.consts.SequenceTypes;
import com.syc.sequence.service.SequenceService;
import com.syc.sequence.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class SycSequenceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SycSequenceApplication.class, args);
        SequenceService sequenceService = context.getBean(SequenceService.class);
        for (int i = 0; i < 10000; i++) {
            long l = sequenceService.next(SequenceTypes.TEST_SEQUENCE_ID);
            String seqId = StringUtil.subOrLefPad(String.valueOf(l), 6);//补 0
            log.info("Sequence Id:{}", seqId);
        }
    }

}
