package com.sparrowzoo.coder.domain;

import com.sparrowzoo.coder.repository.ProjectConfigRepository;
import com.sparrowzoo.coder.repository.TableConfigRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@Getter
@Slf4j
public class CoderDomainRegistry {
    public CoderDomainRegistry() {
        log.info("CoderDomainRegistry init");
    }

    @Inject
    private TableConfigRepository tableConfigRepository;

    @Inject
    private ProjectConfigRepository projectConfigRepository;
}
